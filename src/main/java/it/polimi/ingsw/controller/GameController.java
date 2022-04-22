package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.fullTowersException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.GameHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


//il game controller può occuparsi delle azioni che riguardano l'azione sul gioco complessivo
public class GameController implements PropertyChangeListener {
    public static final String TURN_CONTROLLER= "turnController";
    private final GameHandler gameHandler;
    private Mode game;
    private TurnController turnController;
    private modeEnum gameMode;
    private final PropertyChangeSupport controllerListeners = new PropertyChangeSupport(this);

    public GameController(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.turnController = new TurnController(this, gameHandler, new ActionController(game.getGameBoard())
        );
        controllerListeners.addPropertyChangeListener(TURN_CONTROLLER, turnController);

    }



    public void setGame(Mode game, modeEnum gameMode){

        this.game = game;
        this.gameMode = gameMode;
    }

    public void setDeck(int playerID, Mage deck){
        game.getPlayerByID(playerID).setDeck(deck);
    }

    public void setTeam(int playerID, Type team){
        game.getPlayerByID(playerID).getDashboard().setTeam(team);
    }

    public void initializeGameboard() throws noMoreStudentsException {
        game.initializeGameboard();
    }


    //mette il numero di torri giosto in base al numero che sarà assegnato in fase di inizializzazione
    public void initializeDashboards() throws fullTowersException {
        for(Player p : game.getPlayers()){
            for(int i = 0 ; i < p.getDashboard().getNumTowers(); i++){
                p.getDashboard().putTower();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case "gameboardSetup" ->
        }

    }
}
