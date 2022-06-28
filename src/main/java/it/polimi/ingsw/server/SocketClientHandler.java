package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Socket client handler receives the messages from the client side and sends them to the server,
 * while also communicating with the client the server's updates and requests via socket.
 */
public class SocketClientHandler implements ClientHandler, Runnable{
    private ArrayList<Object> ids;
    private final Socket client;
    private final SocketServer socketServer;

    private boolean connected;

    private final Object inputLock;
    private final Object outputLock;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    /**
     * Constructor for the class, uses output and input locks.
     * @param socketServer the instance of the server socket.
     * @param client the client to listen to.
     */
    public SocketClientHandler(SocketServer socketServer, Socket client) {
        this.socketServer = socketServer;
        this.client = client;
        this.connected = true;
        this.ids = new ArrayList<>();
        this.inputLock = new Object();
        this.outputLock = new Object();
        ids.add(new Object());
        ids.add(new Object());
        try {
            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    /**
     * True if the client's connected
     *
     * @return boolean value for the operation's outcome.
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Disconnects client from server and notifies the controller.
     */
    @Override
    public void disconnect() {
        if (connected) {
            try {
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                Server.LOGGER.severe(e.getMessage());
            }
            connected = false;
            Thread.currentThread().interrupt();

            socketServer.onDisconnect(this);
        }
    }

    /**
     * Sends message to the client from stream.
     *
     * @param message the message to be sent.
     */
    @Override
    public void sendMessage(Message message) {
        try {
            synchronized (outputLock) {
                output.writeObject(message);
                output.reset();
                Server.LOGGER.info(() -> "Sent: " + message);
            }
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
            disconnect();
        }
    }

    /**
     * Method to activate socket and listen to the client
     */
    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe("Client " + client.getInetAddress() + " connection dropped.");
            disconnect();
        }
    }

    /**
     * Handles the connection of a new client and keep listening to the socket for new messages.
     *
     * @throws IOException any of the usual Input/Output related exceptions.
     */
    private void handleClientConnection() throws IOException {
        Server.LOGGER.info("Client connected from " + client.getInetAddress());

        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    Message message = (Message) input.readObject();

                    if (message != null && message.getMessageType() != MessageType.PING) {
                        if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
                            socketServer.addClient(message.getNickname(), ids.size(), this);
                            ids.add(new Object());
                        } else {
                            Server.LOGGER.info(() -> "Received: " + message);
                            socketServer.onMessageReceived(message);
                        }
                    }
                }
            }
        } catch (ClassCastException | ClassNotFoundException e) {
            Server.LOGGER.severe("Invalid stream from client");
        } catch (emptyDecktException e) {
            e.printStackTrace();
        } catch (noMoreStudentsException e) {
            e.printStackTrace();
        } catch (fullTowersException e) {
            e.printStackTrace();
        } catch (noStudentException e) {
            e.printStackTrace();
        } catch (noTowerException e) {
            e.printStackTrace();
        } catch (invalidNumberException e) {
            e.printStackTrace();
        } catch (maxSizeException e) {
            e.printStackTrace();
        } catch (noTowersException e) {
            e.printStackTrace();
        }
        client.close();
    }
}
