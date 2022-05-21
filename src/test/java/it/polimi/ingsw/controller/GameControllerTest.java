package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * This class tests the {@link GameController} behaviour by simulating more matches with different status.
 * Methods are also tested.
 */
public class GameControllerTest {
    private EasyGame easyGame;
    private GameController gameController;
    private ClientHandler clientHandler;

    String player1 = "EttoreMajorana";
    int id1 = 1;
    String player2 = "StuporMundi";
    int id2 = 2;

    //is this a @BeforeAll or a @BeforeEach??? quite sure a @BeforeEach tho
    @BeforeAll
    void startingSetUp() throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        gameController = new GameController();
        easyGame = new EasyGame(2);

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
    @AfterAll
    //this clears all so to close the game safely
    public void tearDownEverything(){
        gameController = null;
        clientHandler = null;
        easyGame = null;
    }

    private void connectAndSetUpTestMatch(String player1, String player2) throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        LoginRequest loginRequest = new LoginRequest(player1);
        gameController.onMessageReceived(loginRequest);
        PlayerNumberReply pnr = new PlayerNumberReply(player1, 2);
        gameController.onMessageReceived(pnr);
        LoginRequest secondLoginRequest = new LoginRequest(player2);
        gameController.onMessageReceived(secondLoginRequest);

        Server server = new Server(gameController);
        server.addClient(player1, id1, clientHandler);
        server.addClient(player2, id2, clientHandler);

        assertEquals(easyGame.getNumCurrentPlayers(), 2);

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
        assertEquals(easyGame.getPlayerByNickname(player1), Type.valueOf("black"));
        assertFalse(Type.notChosen().contains("black"));

        TowerMessage playerTwoTower = new TowerMessage(player2, Type.valueOf("white"));
        gameController.onMessageReceived(playerTwoTower);
        //testing
        assertEquals(easyGame.getPlayerByNickname(player2), Type.valueOf("white"));
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

}
