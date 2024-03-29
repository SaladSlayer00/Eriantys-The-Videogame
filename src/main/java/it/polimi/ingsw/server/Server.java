package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;
import java.util.logging.Logger;

/**
 * Main class that implements the network utility for the server side. Handles connections,
 * disconnections and sends messages toand from the client and server side.
 */
public class Server {
    private final GameController gameController;

    private final Map<String, ClientHandler> clientHandlerMap;

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final Object lock;

    public Server(GameController gameController) {
        this.gameController = gameController;
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<String, ClientHandler>());
        this.lock = new Object();
    }

    /**
     * Adds a client to be managed by the server instance.
     *
     * @param nickname      the nickname associated with the client.
     * @param clientHandler the ClientHandler associated with the client.
     */

    public void addClient(String nickname, int id, ClientHandler clientHandler) throws noMoreStudentsException {
        VirtualView vv = new VirtualView(clientHandler);

        if (!gameController.isGameStarted()) {
            if (gameController.checkLoginNickname(nickname, vv)) {
                clientHandlerMap.put(nickname, clientHandler);
                gameController.loginHandler(nickname, id, vv);
            }
        }
        else if(gameController.getGame().getPlayerByNickname(nickname)!=null){
            Server.LOGGER.info(() -> "Received: " + nickname);
            gameController.loginHandler(nickname, gameController.getGame().getPlayerByNickname(nickname).getPlayerID(), vv);
            vv.showLoginResult(true, true, nickname);
        }else {
            vv.showLoginResult(true, false, null);
            clientHandler.disconnect();
        }

    }

    /**
     * Removes a client given his nickname.
     *
     * @param nickname      the VirtualView to be removed.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     */
    public void removeClient(String nickname, boolean notifyEnabled) {
        //clientHandlerMap.remove(nickname);
        //gameController.removeVirtualView(nickname, notifyEnabled);
        gameController.removeNickname(nickname);
        Server.LOGGER.info(() -> "Removed " + nickname + " from the client list.");
    }

    /**
     * Forwards a received message from the client to the GameController.
     *
     * @param message the message to be forwarded.
     */
    public void onMessageReceived(Message message) throws noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException, emptyDecktException {
        gameController.onMessageReceived(message);
    }

    /**
     * Handles the disconnection of a client.
     *
     * @param clientHandler the client disconnecting.
     */
    public void onDisconnect(ClientHandler clientHandler) {
        synchronized (lock) {
            String nickname = getNicknameFromClientHandler(clientHandler);

            if (nickname != null) {

                boolean gameStarted = gameController.isGameStarted();
                removeClient(nickname, !gameStarted); // enable lobby notifications only if the game didn't start yet.

                if(gameController.getTurnController() != null &&
                        !gameController.getTurnController().getNicknameQueue().contains(nickname)) {
                    return;
                }

                // Resets server status only if the game was already started.
                // Otherwise the server will wait for a new player to connect.
                if (gameStarted) {
                    gameController.broadcastDisconnectionMessage(nickname, " disconnected from the server. GAME ENDED.");

                    //gameController.endGame();
                    //clientHandlerMap.clear();
                }
            }
        }
    }


    /**
     * Returns the corresponding nickname of a ClientHandler.
     *
     * @param clientHandler the client handler.
     * @return the corresponding nickname of a ClientHandler.
     */
    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


}




