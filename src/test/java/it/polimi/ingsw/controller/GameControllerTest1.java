package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        //assertFalse(Type.notChosen().contains(Type.WHITE));
        //init_gameboard

        StartMessage startMessage = new StartMessage(player2.getName(),"yes");
        gameController.onMessageReceived(startMessage);
        assertEquals(gameController.getGameState(), GameState.IN_GAME);

        //Scegliere una tessera nuvola e aggiungere tre studenti alla hall
        gameController.getTurnController().setActivePlayer(player1.getName());
        PickCloudMessage playerOneCloud = new PickCloudMessage(player1.getName(),1);
        gameController.onMessageReceived(playerOneCloud);
        gameController.getTurnController().setActivePlayer(player1.getName());
        PickCloudMessage playerTwoCloud = new PickCloudMessage(player1.getName(), 0);
        gameController.onMessageReceived(playerTwoCloud);
        assertEquals(0,gameController.getGame().getEmptyClouds().size());

        //Passiamo alla scelta degli assistenti
        gameController.getTurnController().setActivePlayer(player1.getName());
        Assistant assistantOne = new Assistant(2,1);
        AssistantMessage playerOneChoice = new AssistantMessage(player1.getName(), assistantOne);
        gameController.onMessageReceived(playerOneChoice);
        Assistant assistantTwo = new Assistant(3,1);
        AssistantMessage playerTwoChoice = new AssistantMessage(player2.getName(), assistantTwo);
        gameController.onMessageReceived(playerTwoChoice);
        //controllo
        assertEquals(gameController.getGame().getPlayerByNickname(player1.getName()).getCardChosen().getNumOrder(), assistantOne.getNumOrder());
        assertEquals(gameController.getGame().getPlayerByNickname(player1.getName()).getDeck().getNumCards(), 9);
        assertEquals(gameController.getGame().getPlayerByNickname(player2.getName()).getCardChosen().getNumOrder(), assistantTwo.getNumOrder());
        assertEquals(gameController.getGame().getPlayerByNickname(player2.getName()).getDeck().getNumCards(), 9);

        //Fase di azione
        //Turno del primo giocatore
        //Il giocatore sceglie 3 studenti da spostare su una isola oppure nella sua sala
        Student chosenStudent1Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().get(3);
        Student chosenStudent2Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().get(4);
        Student chosenStudent3Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().get(5);
        int previousLengthIsland1Ettore = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent1Ettore.getColor()).size();
        int previousLehghtRow1Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getRow(chosenStudent2Ettore.getColor()).getNumOfStudents();
        int previousLenghtRow2Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getRow(chosenStudent3Ettore.getColor()).getNumOfStudents();

        //Questa azione deve essere fatta tre volte (isola oppure sala)
        MoveMessage playerOneMove = new MoveMessage(player1.getName(), chosenStudent1Ettore.getColor(),4 ,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerOneMove);
        MoveMessage playerOneMove2 = new MoveMessage(player1.getName(),chosenStudent2Ettore.getColor(),chosenStudent2Ettore.getColor());
        gameController.onMessageReceived(playerOneMove2);
        MoveMessage playerOneMove3 = new MoveMessage(player1.getName(),chosenStudent3Ettore.getColor(),chosenStudent3Ettore.getColor());
        gameController.onMessageReceived(playerOneMove3);

        //Spostare madre natura su una isola
        int currentPositioneMother = gameController.getGame().getGameBoard().getMotherNature();
        MoveMotherMessage playerOneMotherMove = new MoveMotherMessage(player1.getName(),1,assistantOne);
        gameController.onMessageReceived(playerOneMotherMove);
        //controllo sullo spostamento di madre natura
        assertEquals(currentPositioneMother + 1 ,gameController.getGame().getGameBoard().getMotherNature());
        assertTrue(gameController.getGame().getGameBoard().getIslands().get(currentPositioneMother+1).isMotherNature());
        assertFalse(gameController.getGame().getGameBoard().getIslands().get(currentPositioneMother).isMotherNature());




    }




}
