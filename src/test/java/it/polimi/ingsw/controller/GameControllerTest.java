package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.VirtualView;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * This class tests the {@link GameController} behaviour by simulating more matches with different status.
 * Methods are also tested.
 */
public class GameControllerTest {
    private GameController gameController;
    private ClientHandler clientHandler;

    String player1 = "EttoreMajorana";
    int id1 = 1;
    String player2 = "StuporMundi";
    int id2 = 2;



    //is this a @BeforeAll or a @BeforeEach??? quite sure a @BeforeEach tho
    @BeforeEach
    void startingSetUp() throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        gameController = new GameController();
        //easyGame = new EasyGame(2);
        clientHandler = new ClientHandler(){
            @Override
            public boolean isConnected(){
                return true;
            }

            @Override
            public void disconnect(){

            }

            @Override
            public void sendMessage(Message message){

            }

        };

        connectAndSetUpTestMatch(player1, player2);
    }

    //same thing of the @Before????
    @After
    //this clears all so to close the game safely
    public void tearDownEverything(){
        gameController = null;
        clientHandler = null;
        easyGame = null;
    }

    private void connectAndSetUpTestMatch(String player1, String player2) throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        GameModeReply gameModeReply= new GameModeReply(player1, modeEnum.EASY);
        gameController.onMessageReceived(gameModeReply);
        LoginRequest loginRequest = new LoginRequest(player1);
        gameController.onMessageReceived(loginRequest);
        PlayerNumberReply pnr = new PlayerNumberReply(player1, 2);
        gameController.onMessageReceived(pnr);
        LoginRequest secondLoginRequest = new LoginRequest(player2);
        gameController.onMessageReceived(secondLoginRequest);
        gameController.getGame().initializeGameboard();
        Server server = new Server(gameController);
        server.addClient(player1, id1, clientHandler);
        server.addClient(player2, id2, clientHandler);



        assertEquals(gameController.getGame().getNumCurrentPlayers(), 2);

    }

    @Test
    public void onMessageReceived_MatchOne() throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        //TODO
        /* okay i might have some problems with the creation of the setting of the game
         */
        DeckMessage playerOneDeck = new DeckMessage(player1, Mage.valueOf("mage"));
        gameController.onMessageReceived(playerOneDeck); //??? i think this is super wrong

        /* best way to do now to test the correspondence between the value chosen and the real value
        * is to check the parameter with an assertEqual on the set variable
        * if the variable match with the one chosen by the player that we can say that the test is passed i guess
        * DOUBLE CHECK ON THE PLAYER ATTRIBUTE AND ON THE LIST OF NOT CHOSEN DECKS
         */
        assertEquals(easyGame.getPlayerByNickname(player1).getDeck(), Mage.valueOf("mage"));
        assertFalse(Mage.notChosen().contains(Mage.valueOf("mage")));

        //same thing for the other player
        DeckMessage playerTwoDeck = new DeckMessage(player2, Mage.valueOf("fairy"));
        gameController.onMessageReceived(playerTwoDeck);
        //same test as the one done on the first player
        assertEquals(easyGame.getPlayerByNickname(player2).getDeck(), Mage.valueOf("fairy"));
        assertFalse(Mage.notChosen().contains(Mage.valueOf("fairy")));

        //now the players choose their teams (the towers' color)
        TowerMessage playerOneTower = new TowerMessage(player1, Type.valueOf("black"));
        gameController.onMessageReceived(playerOneTower);
        //testing
        assertEquals(easyGame.getPlayerByNickname(player1).getDashboard().getTeam(), Type.valueOf("black"));
        assertFalse(Type.notChosen().contains("black"));

        TowerMessage playerTwoTower = new TowerMessage(player2, Type.valueOf("white"));
        gameController.onMessageReceived(playerTwoTower);
        //testing
        assertEquals(easyGame.getPlayerByNickname(player2).getDashboard().getTeam(), Type.valueOf("white"));
        assertFalse(Type.notChosen().contains("white"));

        //this is the proper start of the game
        StartMessage readyPlayerOne = new StartMessage(player1, "YES");
        gameController.onMessageReceived(readyPlayerOne);
        StartMessage readyPlayerTwo = new StartMessage(player2, "YES");
        gameController.onMessageReceived(readyPlayerTwo);



        //this should be the thing that return the actual first player???
        /* THERE ARE TWO (2) CONTRUCTOR????? I CHOOSE THE SECOND ONE SINCE IT'S THE ONE CREATE
        * BY @SALAD_SLAYER BUT DUNNO IF IT'S RIGHT
        * TO CHECK
        * TODO
         */
        MatchInfoMessage firstPlayerToChoose = new MatchInfoMessage(1, 2);
        gameController.onMessageReceived(firstPlayerToChoose);

        //actual choice of the assistant's card
        /* WHAT'S TESTED HERE?
        * here the testing check that:
        *  - the card is the actual card chosen
        *  - the card chosen ISN'T in the deck anymore
        *  - the number of cards in the deck is 9 since the card chosen has been removed
         */
        Assistant assistantOne = easyGame.getPlayerByNickname(player1).getDeck().draw(2);
        AssistantMessage playerOneChoice = new AssistantMessage(player1, assistantOne);
        gameController.onMessageReceived(playerOneChoice);
        assertEquals(easyGame.getPlayerByNickname(player1).getCardChosen(), assistantOne);
        assertFalse(easyGame.getPlayerByNickname(player1).getDeck().getCards().contains(assistantOne));
        assertEquals(easyGame.getPlayerByNickname(player1).getDeck().getNumCards(), 9);

        //now it's the turn of the second player
        Assistant assistantTwo = easyGame.getPlayerByNickname(player2).getDeck().draw(3);
        AssistantMessage playerTwoChoice = new AssistantMessage(player2, assistantTwo);
        gameController.onMessageReceived(playerTwoChoice);
        assertEquals(easyGame.getPlayerByNickname(player2).getCardChosen(), assistantTwo);
        assertFalse(easyGame.getPlayerByNickname(player2).getDeck().getCards().contains(assistantTwo));
        assertEquals(easyGame.getPlayerByNickname(player2).getDeck().getNumCards(), 9);

    }



    //Codice già integrato con il codice sopra
    @Test
    public void onMessageReceived_MatchTwo()  throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException{
        //TODO : NON SONO SICURO DI QUESTA PARTE. E' SOLAMENTE UNA IPOTESI
        //Fase iniziale del gioco
        //inizializzazione player
        Player ettore = gameController.getGame().getPlayerByID(1);
        Player stupor = gameController.getGame().getPlayerByID(2);
        //inizializzazione gameboard
        gameController.getGame().getGameBoard().initializeIslands();
        gameController.getGame().getGameBoard().placeMother();
        //inizializzazione delle dashboard
        gameController.getGame().initializeDashboards();
        //inizializzazione delle nuvole
        //Scelta delle torri
        TowerMessage playerOneTower = new TowerMessage(player1 , Type.BLACK);
        gameController.onMessageReceived(playerOneTower);
        TowerMessage playerTwoTower = new TowerMessage(player2 , Type.WHITE);
        gameController.onMessageReceived(playerTwoTower);
        //Scelta del deck
        DeckMessage playerOneDeck = new DeckMessage(player1, Mage.MAGE);
        gameController.onMessageReceived(playerOneDeck);
        DeckMessage playerTwoDeck = new DeckMessage(player2 , Mage.FAIRY);
        gameController.onMessageReceived(playerTwoDeck);
        //controllo
        assertEquals(gameController.getGame().getPlayerByNickname(ettore.getName()).getDeck().getMage(), Mage.MAGE);
        assertFalse(Mage.notChosen().contains(Mage.MAGE));
        assertEquals(gameController.getGame().getPlayerByNickname(stupor.getName()).getDeck().getMage(), Mage.FAIRY);
        assertFalse(Mage.notChosen().contains(Mage.FAIRY));
        assertEquals(gameController.getGame().getPlayerByNickname(player1), Type.BLACK);
        assertFalse(Type.notChosen().contains(Type.BLACK));
        assertEquals(gameController.getGame().getPlayerByNickname(player2), Type.WHITE);
        assertFalse(Type.notChosen().contains(Type.WHITE));
        //Fase di pianificazione
        //scegliamo a caso il primo giocatore per mettere gli studenti sulle nuvole
        gameController.getGame().initializeGameboard();
        gameController.getGame().getGameBoard().createClouds();
        gameController.getTurnController().cloudInitializer(0);
        gameController.getTurnController().cloudInitializer(1);
        //Passiamo alla scelta degli assistenti
        Assistant assistantOne = gameController.getGame().getPlayerByNickname(player1).getDeck().draw(2);
        AssistantMessage playerOneChoice = new AssistantMessage(player1, assistantOne);
        gameController.onMessageReceived(playerOneChoice);
        Assistant assistantTwo = gameController.getGame().getPlayerByNickname(player2).getDeck().draw(3);
        AssistantMessage playerTwoChoice = new AssistantMessage(player2, assistantTwo);
        gameController.onMessageReceived(playerTwoChoice);
        List<Assistant> chosenAssistants = new ArrayList<>();
        chosenAssistants.add(assistantOne);
        chosenAssistants.add(assistantTwo);
        //controllo
        assertEquals(gameController.getTurnController().getChosen() ,chosenAssistants);
        assertEquals(gameController.getGame().getPlayerByNickname(player1).getCardChosen(), assistantOne);
        assertFalse(gameController.getGame().getPlayerByNickname(player1).getDeck().getCards().contains(assistantOne));
        assertEquals(gameController.getGame().getPlayerByNickname(player1).getDeck().getNumCards(), 9);
        assertEquals(gameController.getGame().getPlayerByNickname(player2).getCardChosen(), assistantTwo);
        assertFalse(gameController.getGame().getPlayerByNickname(player2).getDeck().getCards().contains(assistantTwo));
        assertEquals(gameController.getGame().getPlayerByNickname(player2).getDeck().getNumCards(), 9);

        /*
        //this is the proper start of the game
        StartMessage readyPlayerOne = new StartMessage(player1, "YES");
        gameController.onMessageReceived(readyPlayerOne);
        StartMessage readyPlayerTwo = new StartMessage(player2, "YES");
        gameController.onMessageReceived(readyPlayerTwo);

        //this should be the thing that return the actual first player???
        /* THERE ARE TWO (2) CONTRUCTOR????? I CHOOSE THE SECOND ONE SINCE IT'S THE ONE CREATE
         * BY @SALAD_SLAYER BUT DUNNO IF IT'S RIGHT
         * TO CHECK
         * TODO
         */
        MatchInfoMessage firstPlayerToChoose = new MatchInfoMessage(1, 2);
        gameController.onMessageReceived(firstPlayerToChoose);

        //Fase di azione
        //Turno del primo giocatore
        //Il giocatore sceglie 3 studenti da spostare su una isola oppure nella sua sala
        Student chosenStudent1Ettore = ettore.getDashboard().getHall().get(3);
        Student chosenStudent2Ettore = ettore.getDashboard().getHall().get(4);
        Student chosenStudent3Ettore = ettore.getDashboard().getHall().get(5);
        int previousLengthIsland1Ettore = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent1Ettore.getColor()).size();
        int previousLehghtRow1Ettore = ettore.getDashboard().getRow(chosenStudent2Ettore.getColor()).getNumOfStudents();
        int previousLenghtRow2Ettore = ettore.getDashboard().getRow(chosenStudent3Ettore.getColor()).getNumOfStudents();

        //Questa azione deve essere fatta tre volte (isola oppure sala)
        MoveMessage playerOneMove = new MoveMessage(ettore.getName(), chosenStudent1Ettore.getColor(),4 ,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerOneMove);
        MoveMessage playerOneMove2 = new MoveMessage(ettore.getName(),chosenStudent2Ettore.getColor(),chosenStudent2Ettore.getColor());
        gameController.onMessageReceived(playerOneMove2);
        MoveMessage playerOneMove3 = new MoveMessage(ettore.getName(),chosenStudent3Ettore.getColor(),chosenStudent3Ettore.getColor());
        gameController.onMessageReceived(playerOneMove3);

        //Spostare madre natura su una isola
        int currentPositioneMother = gameController.getGame().getGameBoard().getMotherNature();
        MoveMotherMessage playerOneMotherMove = new MoveMotherMessage(ettore.getName(),2,assistantOne);
        gameController.onMessageReceived(playerOneMotherMove);
        //controllo sullo spostamento di madre natura
        assertEquals(currentPositioneMother + 2,gameController.getGame().getGameBoard().getMotherNature());
        assertTrue(gameController.getGame().getGameBoard().getIslands().get(gameController.getGame().getGameBoard().getMotherNature()).isMotherNature());
        assertFalse(gameController.getGame().getGameBoard().getIslands().get(currentPositioneMother).isMotherNature());

        //turno secondo giocatore
        Student chosenStudent1Stupor = stupor.getDashboard().getHall().get(3);
        Student chosenStudent2Stupor = stupor.getDashboard().getHall().get(4);
        Student chosenStudnet3Stupor = stupor.getDashboard().getHall().get(1);
        int previousLengthIsland1Stupor = gameController.getGame().getGameBoard().getIslands().get(2).getStudents().get(chosenStudent1Ettore.getColor()).size();
        int previousLength2RowStupor = stupor.getDashboard().getRow(chosenStudent2Stupor.getColor()).getNumOfStudents();
        int previousLength2islandStupor = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudnet3Stupor.getColor()).size();
        //Questa azione deve essere fatta tre volte (isola oppure sala)
        MoveMessage playerTwoMove = new MoveMessage(stupor.getName(), chosenStudent1Stupor.getColor(),2,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerTwoMove);
        MoveMessage playerTwoMove2 = new MoveMessage(stupor.getName(),chosenStudent2Stupor.getColor(), chosenStudent2Stupor.getColor());
        gameController.onMessageReceived(playerTwoMove2);
        MoveMessage playerTwoMove3 = new MoveMessage(stupor.getName() ,chosenStudent2Stupor.getColor(),4,gameController.getGame().getGameBoard().getIslands());
        gameController.onMessageReceived(playerTwoMove3);
        //Spostare madre natura su una isola
        currentPositioneMother = gameController.getGame().getGameBoard().getMotherNature();
        MoveMotherMessage playerTwoMotherMove = new MoveMotherMessage(stupor.getName(),1,assistantTwo);
        gameController.onMessageReceived(playerTwoMotherMove);
        //controllo sullo spostamento di madre natura
        assertEquals(currentPositioneMother + 1,gameController.getGame().getGameBoard().getMotherNature());
        assertTrue(gameController.getGame().getGameBoard().getIslands().get(gameController.getGame().getGameBoard().getMotherNature()).isMotherNature());
        assertFalse(gameController.getGame().getGameBoard().getIslands().get(currentPositioneMother).isMotherNature());



        //controlli
        //controllo sugli studenti spostati
        int currentLengthIsland1Ettore = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudent1Ettore.getColor()).size();
        assertEquals(previousLengthIsland1Ettore +1, currentLengthIsland1Ettore);
        int currentLength1IslandStupor = gameController.getGame().getGameBoard().getIslands().get(2).getStudents().get(chosenStudent1Ettore.getColor()).size();
        assertEquals(previousLengthIsland1Stupor +1, currentLength1IslandStupor);
        int currentLenghRow1Ettore = ettore.getDashboard().getRow(chosenStudent2Ettore.getColor()).getNumOfStudents();
        assertEquals(currentLenghRow1Ettore, previousLehghtRow1Ettore +1);
        int currentLenghtRow2Ettore = ettore.getDashboard().getRow(chosenStudent3Ettore.getColor()).getNumOfStudents();
        assertEquals(previousLenghtRow2Ettore +1 , currentLenghtRow2Ettore);
        int currentLenght2RowStupor = stupor.getDashboard().getRow(chosenStudent2Stupor.getColor()).getNumOfStudents();
        assertEquals(previousLength2RowStupor +1 , currentLenght2RowStupor);
        int currentLenght2IslandStupor = gameController.getGame().getGameBoard().getIslands().get(4).getStudents().get(chosenStudnet3Stupor.getColor()).size();
        assertEquals(previousLength2islandStupor +1 , currentLenght2IslandStupor);

        //Scegliere una tessera nuvola e aggiungere tre studenti alla hall
        gameController.getTurnController().setActivePlayer(player1);
        PickCloudMessage playerOneCloud = new PickCloudMessage(player1,1);
        List<Cloud> cloudOne = new ArrayList<Cloud>((Collection<? extends Cloud>) easyGame.getGameBoard().getCloud(1));
        gameController.onMessageReceived(playerOneCloud);
        gameController.getTurnController().setActivePlayer(player2);
        PickCloudMessage playerTwoCloud = new PickCloudMessage(player2,0);
        List<Cloud> cloudTwo = new ArrayList<Cloud>((Collection<? extends Cloud>) easyGame.getGameBoard().getCloud(0));
        gameController.onMessageReceived(playerTwoCloud);
        //controlli
        assertTrue(gameController.getGame().getGameBoard().getCloud(0).emptyCloud());
        assertTrue(gameController.getGame().getGameBoard().getCloud(1).emptyCloud());
        assertTrue(gameController.getGame().getPlayerByNickname(player1).getDashboard().getHall().containsAll(cloudOne));
        assertTrue(gameController.getGame().getPlayerByNickname(player2).getDashboard().getHall().containsAll(cloudTwo));



    }

    @Test
    public void addVirtualView(){
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname",virtualView);
        assertNotNull(gameController.getVirtualViewMap().get("testNickname"));

    }

    @Test
    public void removeVirtualView(){
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




}
