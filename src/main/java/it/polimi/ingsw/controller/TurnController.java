package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class TurnController implements PropertyChangeListener {
    private final NamePhase[] phases = {NamePhase.INITIALIZING, NamePhase.PLANNING, NamePhase.ACTION, NamePhase.ENDING};
    private ArrayList<Player> playerOrder;
    private Player activePlayer;
    int activePhase = 0;
    public TurnController(GameController controller, GameHandler gameHandler, ActionController actionController){}




    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
