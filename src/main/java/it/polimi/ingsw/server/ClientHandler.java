package it.polimi.ingsw.server;

import it.polimi.ingsw.message.Message;

/**
 * Interface for handling the messages sent to the client and its disconnection.
 */

public interface ClientHandler {
    /**
     * Returns the connection status.
     *
     * @return {@code true} if the client is still connected and reachable, {@code false} otherwise.
     */
    boolean isConnected();

    /**
     * Disconnects from the client.
     */
    void disconnect();

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent.
     */
    void sendMessage(Message message);
}
