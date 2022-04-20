package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.model.*;

import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class GameHandler {
    private static final String PLAYER = "Player";
    private final Server server;
    private final GameController controller;
    private Mode game;
    private final GameFactory gameFactory;
    private final PropertyChangeSupport controllerListener = new PropertyChangeSupport(this);
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Random rnd = new Random();
    private int started;
    private int playersNumber;
    private modeEnum gameMode;

    public GameHandler(Server server) {
        this.server = server;
        this.gameFactory = new GameFactory();
        started = 0;
        controller = new GameController((this));
        controllerListener.addPropertyChangeListener(controller);
    }

    //il socket setta questo parametro
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    //il socket setta questo parametro
    public void setGameMode(modeEnum gameMode) {
        this.gameMode = gameMode;
    }

    public void createGame() throws invalidNumberException {
        game = gameFactory.getMode(this.gameMode, this.playersNumber);
    }

    public void createGameController(){
        controller.setGame(this.game, this.gameMode);
    }

    public void createPlayer(String nickname, int playerID){
        game.initializePlayer(new Player(nickname, playerID));
    }

    //loro accedevano tramite nich
    public void deckSetup() {
        if (started == 0) started = 1;
        DeckTypeRequest req = new DeckMessage("Please choose your Deck.");
        req.addRemaining(Mage.notChosen());
        if (playersNumber == 2 && Mage.notChosen().size() > 1) {
            String nickname = game.getActivePlayers().get(playersNumber - Mage.notChosen().size() + 1).
                    getNickname();
            singleSend(req, server.getIDByNickname(nickname));
            sendAllExcept(new CustomMessage("User " + nickname + " is choosing his color!", false),
                    server.getIDByNickname(nickname));
            return;
        } else if (playersNumber == 3 && !Mage.notChosen().isEmpty()) {
            String nickname = game.getActivePlayers().get(playersNumber - Mage.notChosen().size()).
                    getName();
            if (Mage.notChosen().size() == 1) {
                game.getPlayerByNickname(nickname).setDeck(Mage.notChosen().get(0));
                singleSend(new CustomMessage("\nThe society decides for you! You have the " +
                        Mage.notChosen().get(0) + " color!\n", false), server.getIDByNickname(nickname));
                singleSend(new DeckMessage(null, Mage.notChosen().get(0).toString()),
                        server.getIDByNickname(nickname));
                Mage.choose(Mage.notChosen().get(0));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            } else {
                server.getClientByID(server.getIDByNickname(nickname)).send(req);
                sendAllExcept(new CustomMessage("User " + nickname + " is choosing his color!", false),
                        server.getIDByNickname(nickname));
                return;
            }
        }
    }

        public void teamSetup() {
            if(started==0) started=1;
            TeamTypeRequest req = new TeamMessage("Please choose your Team.");
            req.addRemaining(Type.notChosen());
            if(playersNumber==2 && Type.notChosen().size()>1) {
                String nickname = game.getActivePlayers().get(playersNumber - Type.notChosen().size() + 1).
                        getName();
                singleSend(req, server.getIDByNickname(nickname));
                sendAllExcept(new CustomMessage("User " + nickname + " is choosing his color!", false),
                        server.getIDByNickname(nickname));
                return;
            }
            else if(playersNumber==3 && !Type.notChosen().isEmpty()) {
                String nickname = game.getActivePlayers().get(playersNumber - Type.notChosen().size()).
                        getName();
                if(Type.notChosen().size()==1) {
                    game.getPlayerByNickname(nickname).setColor(Type.notChosen().get(0));
                    singleSend(new CustomMessage("\nThe society decides for you! You have the " +
                            Type.notChosen().get(0) + " color!\n", false), server.getIDByNickname(nickname));
                    singleSend(new TeamMessage(null, Type.notChosen().get(0).toString()),
                            server.getIDByNickname(nickname));
                    Type.choose(Type.notChosen().get(0));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                }
                else {
                    server.getClientByID(server.getIDByNickname(nickname)).send(req);
                    sendAllExcept(new CustomMessage("User " + nickname + " is choosing his color!", false),
                            server.getIDByNickname(nickname));
                    return;
                }
            }






}
