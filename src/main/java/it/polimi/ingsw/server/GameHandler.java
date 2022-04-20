package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.model.GameFactory;
import it.polimi.ingsw.model.Mode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.modeEnum;

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






}
