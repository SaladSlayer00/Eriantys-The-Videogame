package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.LoginRequest;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.PlayerNumberReply;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    void startingSetUp(){
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

    }

    @Test
    public void onMessageReceived_MatchOne(){
        //the first player choose a
    }

}
