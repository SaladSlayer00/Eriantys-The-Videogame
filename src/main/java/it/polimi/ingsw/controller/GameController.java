package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameController implements PropertyChangeListener {
    public static final String TURN_CONTROLLER= "turnController";
    private Mode game;
    private TurnController turnController;
    private final GameFactory gameFactory = new GameFactory();
    private modeEnum gameMode;
    private final Server server;
    private final PropertyChangeSupport controllerListeners = new PropertyChangeSupport(this);
    private int playerNumber;

    public GameController() {
    }

    public void setPlayersNumber(int playerNumber) {
        this.playerNumber = playerNumber;
        game.getNumPlayers(playerNumber);
    }

    public void setGameMode(modeEnum gameMode){
        this.gameMode = gameMode;
    }

    public void setupPlayer(String nickname, int clientID) {
        game.initializePlayer(new Player(nickname, clientID));
    }

    public void 

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
