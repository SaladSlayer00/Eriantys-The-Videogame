package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public abstract class Phase {
    private NamePhase name;
    private ArrayList<Player> playerOrder;
    private Player activePlayer;
}
