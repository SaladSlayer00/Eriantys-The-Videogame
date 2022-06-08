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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
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
       gameController.endGame();
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
        gameController.getTurnController().setActivePlayer(player1.getName());
        Student chosenStudent1Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().get(3);
        Student chosenStudent2Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().get(4);
        Student chosenStudent3Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().get(5);
        int previousLengthIsland1Ettore = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent1Ettore.getColor()).size();
        int previousLengthRow1Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getRow(chosenStudent2Ettore.getColor()).getNumOfStudents();
        int previousLengthRow2Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getRow(chosenStudent3Ettore.getColor()).getNumOfStudents();
        //Questa azione deve essere fatta tre volte (isola oppure sala)
        MoveMessage playerOneMove = new MoveMessage(player1.getName(), chosenStudent1Ettore.getColor(),4 ,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerOneMove);
        MoveMessage playerOneMove2 = new MoveMessage(player1.getName(),chosenStudent2Ettore.getColor(),chosenStudent2Ettore.getColor());
        gameController.onMessageReceived(playerOneMove2);
        MoveMessage playerOneMove3 = new MoveMessage(player1.getName(),chosenStudent3Ettore.getColor(),chosenStudent3Ettore.getColor());
        gameController.onMessageReceived(playerOneMove3);
        //controlli
        //controllo sugli studenti spostati
        int currentLengthIsland1Ettore = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent1Ettore.getColor()).size();
        assertEquals(previousLengthIsland1Ettore +1, currentLengthIsland1Ettore);
        int currentLengthRow1Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getRow(chosenStudent2Ettore.getColor()).getNumOfStudents();
        if(chosenStudent2Ettore.getColor().equals(chosenStudent3Ettore.getColor()))
        {
            assertEquals(currentLengthRow1Ettore, previousLengthRow1Ettore +2);
        }else{
            assertEquals(currentLengthRow1Ettore, previousLengthRow1Ettore +1);
            int currentLengthRow2Ettore = gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getRow(chosenStudent3Ettore.getColor()).getNumOfStudents();
            assertEquals(previousLengthRow2Ettore +1 , currentLengthRow2Ettore);
        }


        //Spostare madre natura su una isola
        int currentPositionMother = gameController.getGame().getGameBoard().getMotherNature();
        MoveMotherMessage playerOneMotherMove = new MoveMotherMessage(player1.getName(),1,assistantOne);
        gameController.onMessageReceived(playerOneMotherMove);
        //controllo sullo spostamento di madre natura
        if(currentPositionMother==11){
            assertEquals( 0 ,gameController.getGame().getGameBoard().getMotherNature());
        }else{
            assertEquals(currentPositionMother + 1 ,gameController.getGame().getGameBoard().getMotherNature());
        }

        assertTrue(gameController.getGame().getGameBoard().getIslands().get(gameController.getGame().getGameBoard().getMotherNature()).isMotherNature());
        assertFalse(gameController.getGame().getGameBoard().getIslands().get(currentPositionMother).isMotherNature());

        //Selezione della nuvola che contiene gli studenti da aggiungere alla hall
        List<Cloud> cloudOne = (ArrayList<Cloud>)gameController.getGame().getGameBoard().getCloud(1).getStudents().clone();
        gameController.getTurnController().setActivePlayer(player1.getName());
        playerOneCloud = new PickCloudMessage(player1.getName(),1);
        gameController.onMessageReceived(playerOneCloud);
        assertEquals(1,gameController.getGame().getEmptyClouds().size());
        assertTrue(gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().containsAll(cloudOne));

        //turno secondo giocatore
        gameController.getTurnController().setActivePlayer(player2.getName());
        Student chosenStudent1Stupor = gameController.getGame().getPlayerByNickname(player2.getName()).getDashboard().getHall().get(3);
        Student chosenStudent2Stupor = gameController.getGame().getPlayerByNickname(player2.getName()).getDashboard().getHall().get(4);
        Student chosenStudent3Stupor = gameController.getGame().getPlayerByNickname(player2.getName()).getDashboard().getHall().get(1);
        int previousLengthIsland1Stupor = gameController.getGame().getGameBoard().getIslands().get(2).getStudents().get(chosenStudent1Stupor.getColor()).size();
        int previousLength2RowStupor = gameController.getGame().getPlayerByNickname(player2.getName()).getDashboard().getRow(chosenStudent2Stupor.getColor()).getNumOfStudents();
        int previousLength2islandStupor = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent3Stupor.getColor()).size();
        //Questa azione deve essere fatta tre volte (isola oppure sala)
        MoveMessage playerTwoMove = new MoveMessage(player2.getName(), chosenStudent1Stupor.getColor(),2,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerTwoMove);
        MoveMessage playerTwoMove2 = new MoveMessage(player2.getName(),chosenStudent2Stupor.getColor(), chosenStudent2Stupor.getColor());
        gameController.onMessageReceived(playerTwoMove2);
        MoveMessage playerTwoMove3 = new MoveMessage(player2.getName() ,chosenStudent3Stupor.getColor(),4,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerTwoMove3);

        //controlli
        //controllo sugli studenti spostati
        int currentLength1IslandStupor = gameController.getGame().getGameBoard().getIslands().get(2).getStudents().get(chosenStudent1Stupor.getColor()).size();
        assertEquals(previousLengthIsland1Stupor +1, currentLength1IslandStupor);
        int currentLength2RowStupor = gameController.getGame().getPlayerByNickname(player2.getName()).getDashboard().getRow(chosenStudent2Stupor.getColor()).getNumOfStudents();
        assertEquals(previousLength2RowStupor +1 , currentLength2RowStupor);
        int currentLength2IslandStupor = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent3Stupor.getColor()).size();
        assertEquals(previousLength2islandStupor +1 , currentLength2IslandStupor);


        //Spostare madre natura su una isola
        currentPositionMother = gameController.getGame().getGameBoard().getMotherNature();
        MoveMotherMessage playerTwoMotherMove = new MoveMotherMessage(player2.getName(),1,assistantTwo);
        gameController.onMessageReceived(playerTwoMotherMove);
        //controllo sullo spostamento di madre natura
        if(currentPositionMother==11){
            assertEquals( 0 ,gameController.getGame().getGameBoard().getMotherNature());
        }else{
            assertEquals(currentPositionMother + 1 ,gameController.getGame().getGameBoard().getMotherNature());
        }
        assertTrue(gameController.getGame().getGameBoard().getIslands().get(gameController.getGame().getGameBoard().getMotherNature()).isMotherNature());
        assertFalse(gameController.getGame().getGameBoard().getIslands().get(currentPositionMother).isMotherNature());

        //Selezione della nuvola che contiene gli studenti da aggiungere alla hall
        List<Cloud> cloudTwo = (ArrayList<Cloud>)gameController.getGame().getGameBoard().getCloud(0).getStudents().clone();
        gameController.getTurnController().setActivePlayer(player1.getName());
        playerOneCloud = new PickCloudMessage(player1.getName(),0);
        gameController.onMessageReceived(playerOneCloud);
        assertEquals(2,gameController.getGame().getEmptyClouds().size());
        assertTrue(gameController.getGame().getPlayerByNickname(player1.getName()).getDashboard().getHall().containsAll(cloudTwo));



    }
    @Test
    public void addVirtualView(){
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname",virtualView);
        assertNotNull(gameController.getVirtualViewMap().get("testNickname"));

    }

    @Test
    public void removeVirtualView() throws noMoreStudentsException {
        gameController.getGame().initializeGameboard();
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname",virtualView);
        gameController.removeVirtualView("testNickname",true);
        assertNull(gameController.getVirtualViewMap().get("testNickname"));
    }

    @Test
    public void getVirtualViewMap() {
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname",virtualView);
        assertNotNull(gameController.getVirtualViewMap());
    }

    @Test
    public void numberOfPlayerTest() {
        assertEquals(gameController.getGame().getNumCurrentPlayers(), 2);
        assertEquals(gameController.getGame().getPlayers().size(),2);
        assertEquals(gameController.getGame().getActivePlayers().size(),2);
        assertEquals(gameController.getGame().getNumCurrentActivePlayers(),2);
    }




}
