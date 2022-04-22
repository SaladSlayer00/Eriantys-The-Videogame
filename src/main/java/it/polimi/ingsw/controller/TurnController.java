package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import it.polimi.ingsw.controller.ActionController;

public class TurnController implements PropertyChangeListener {
//    private final NamePhase[] phases = {NamePhase.INITIALIZING, NamePhase.PLANNING, NamePhase.ACTION, NamePhase.ENDING};
//    private ArrayList<Player> playerOrder;
//    private Player activePlayer;
    private final GameController controller;

    private final ActionController actionController;

    private final GameHandler gameHandler;

    public TurnController(GameController controller, GameHandler gameHandler, ActionController actionController){
        this.controller = controller;
        this.actionController = actionController;
        this.gameHandler = gameHandler;
    }


    private boolean isPhaseCorrect(Action action) {
        //da implementare in modo che non so ancora
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
