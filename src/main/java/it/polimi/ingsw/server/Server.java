package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.InvalidPlayersException;
import it.polimi.ingsw.exceptions.fullTowersException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.Mage;
import it.polimi.ingsw.model.Type;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class Server {
    private final SocketServer socketServer;
    private final Map<Integer, VirtualClient> idMapClient;
    private final Map<VirtualClient, SocketInstance> clientToConnection;
    private final Map<String, Integer> nameMapId;
    private int nextClientID;
    private GameHandler currentGame;
    private final List<SocketInstance> waiting = new ArrayList<>();
    private int totalPlayers;




    //da sincronizzare
    public void lobby(SocketInstance c) throws InterruptedException, noMoreStudentsException, fullTowersException {
        waiting.add(c);
        if (waiting.size() == 1) {
            c.setMode(new RequestMode(
                    idMapClient.get(c.getClientID()).getNickname()
                            + ", you are"
                    + "the lobby host. \nChoose the game mode! [EASY/EXPERT]", false));

            c.setPlayers(
                    new RequestPlayersNumber(
                            idMapClient.get(c.getClientID()).getNickname()
                                    + ", you are"
                                    + " the lobby host.\nChoose the number of players! [2/3]",
                            false));

        } else if (waiting.size() == totalPlayers) {
            System.err.println(
                    Constants.getInfo() + "Minimum player number reached. The match is starting.");
            for (int i = 3; i > 0; i--) {
                currentGame.sendAll(new CustomMessage("Match starting in " + i, false));
                TimeUnit.MILLISECONDS.sleep(500);
            }
            currentGame.sendAll(new CustomMessage("The match has started!", false));
            waiting.clear();
            Mage.reset();
            Type.reset();
            currentGame.setup();
        } else {
            currentGame.sendAll(
                    new CustomMessage((totalPlayers - waiting.size()) + " slots left.", false));
        }
    }

    public void setTotalPlayers(int totalPlayers) throws InvalidPlayersException {
        if (totalPlayers < 2 || totalPlayers > 4) {
            throw new InvalidPlayersException();
        } else {
            this.totalPlayers = totalPlayers;
        }
    }

    public int getIDByNickname(String nickname) {
        return nameMapId.get(nickname);
    }

}

