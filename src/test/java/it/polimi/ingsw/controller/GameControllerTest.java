package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.*;
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
    private Player player1 = new Player("EttoreMajorana", 1);
    private Player player2 = new Player("StuporMundi", 2);

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
    public void tearDownEverything() {
        gameController = null;
        clientHandler = null;
        gameController.endGame();
    }

    private void connectAndSetupTestMatch() throws  noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, noTowersException {
        gameController.loginHandler(player1.getName(), player1.getPlayerID(), new VirtualView(clientHandler));
        GameModeReply gameModeReply = new GameModeReply(player1.getName(), modeEnum.EASY);
        gameController.onMessageReceived(gameModeReply);
        PlayerNumberReply playerNumberReply = new PlayerNumberReply(player1.getName(), 2);
        gameController.onMessageReceived(playerNumberReply);
        Server server = new Server(gameController);
        server.addClient(player1.getName(), player1.getPlayerID(), clientHandler);
        server.addClient(player2.getName(), player2.getPlayerID(), clientHandler);


    }

    @Test
    public void addVirtualView() {
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname", virtualView);
        assertNotNull(gameController.getVirtualViewMap().get("testNickname"));

    }

    @Test
    public void removeVirtualView() throws noMoreStudentsException {
        gameController.getGame().initializeGameboard();
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname", virtualView);
        gameController.removeVirtualView("testNickname", true);
        assertNull(gameController.getVirtualViewMap().get("testNickname"));
    }

    @Test
    public void getVirtualViewMap() {
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname", virtualView);
        assertNotNull(gameController.getVirtualViewMap());
    }

    @Test
    public void numberOfPlayerTest() {
        assertEquals(gameController.getGame().getNumCurrentPlayers(), 2);
        assertEquals(gameController.getGame().getPlayers().size(), 2);
        assertEquals(gameController.getGame().getActivePlayers().size(), 2);
        assertEquals(gameController.getGame().getNumCurrentActivePlayers(), 2);
    }


}