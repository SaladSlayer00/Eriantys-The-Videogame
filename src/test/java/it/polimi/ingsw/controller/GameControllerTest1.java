package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.VirtualView;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameControllerTest1 {
    private GameController gameController;
    private ClientHandler clientHandler;
    private Player  player1 = new Player("EttoreMajorana" , 1);
    private Player player2 = new Player("StuporMundi" , 2);

    @BeforeEach
    void startingSetup() throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        gameController = new GameController();
        clientHandler = new ClientHandler() {
            @Override
            public boolean isConnected() {
                return true;
            }

            @Override
            public void disconnect() {

            }

            @Override
            public void sendMessage(Message message) {

            }
        };
        connectAndSetupTestMatch();
    }
    @After
    public void tearDownEverything(){
        gameController = null;
        clientHandler = null;
    }

    private void connectAndSetupTestMatch() throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        gameController.loginHandler(player1.getName(),player1.getPlayerID(),new VirtualView(clientHandler));
        GameModeReply gameModeReply = new GameModeReply(player1.getName(), modeEnum.EASY);
        gameController.onMessageReceived(gameModeReply);
        PlayerNumberReply playerNumberReply = new PlayerNumberReply(player1.getName(),2);
        gameController.onMessageReceived(playerNumberReply);
        Server server = new Server(gameController);
        server.addClient(player1.getName(),player1.getPlayerID(),clientHandler);
        server.addClient(player2.getName(),player2.getPlayerID(),clientHandler);

    }
    @Test
    public void onMessageReceived_MatchOne() throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        //initState
        //init_deck
        gameController.getTurnController().setActivePlayer(player1.getName());
        DeckMessage playerOneDeck = new DeckMessage(player1.getName(), Mage.MAGE);
        gameController.onMessageReceived(playerOneDeck);
        gameController.getTurnController().setActivePlayer(player2.getName());
        DeckMessage playerTwoDeck = new DeckMessage(player2.getName(), Mage.FAIRY);
        gameController.onMessageReceived(playerTwoDeck);
        //check
        assertEquals(gameController.getGame().getPlayerByNickname(player1.getName()).getDeck().getMage(), Mage.MAGE);
        assertFalse(Mage.notChosen().contains(Mage.MAGE));
        assertEquals(gameController.getGame().getPlayerByNickname(player2.getName()).getDeck().getMage(), Mage.FAIRY);
        assertFalse(Mage.notChosen().contains(Mage.FAIRY));
        //init_tower
        gameController.getTurnController().setActivePlayer(player1.getName());
        TowerMessage playerOneTower = new TowerMessage(player1.getName(), Type.BLACK);
        gameController.onMessageReceived(playerOneTower);
        gameController.getTurnController().setActivePlayer(player2.getName());
        TowerMessage playerTwoTower = new TowerMessage(player2.getName() , Type.WHITE);
        gameController.onMessageReceived(playerTwoTower);
        //check
        assertEquals(gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getTeam(), Type.BLACK);
        assertFalse(Type.notChosen().contains(Type.BLACK));
        assertEquals(gameController.getGame().getPlayerByNickname(player2.getName()).getDashboard().getTeam(), Type.WHITE);
        assertFalse(Type.notChosen().contains(Type.WHITE));
        //init_gameboard
        /*
        StartMessage startMessage = new StartMessage(player1.getName(),"yes");
        gameController.onMessageReceived(startMessage);
        assertEquals(gameController.getGameState(), GameState.IN_GAME);

         */



    }




}
