package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.Player;

public class ActionController {
    private final Gameboard gameBoard;
    private final NamePhase[] phases = {NamePhase.INITIALIZING, NamePhase.PLANNING, NamePhase.ACTION, NamePhase.ENDING};
    private final NamePhase activePhase;
    private Player player;

    public NamePhase getPhase() {
        return activePhase;
    }

    public ActionController(Gameboard gameboard){
        this.gameBoard = gameboard;
        this.activePhase = NamePhase.INITIALIZING;
        this.player = null;
    }


}
