package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.Player;

public class ActionController {
    private final Gameboard gameBoard;
    protected int phase;
    private Player player;

    public ActionController(Gameboard gameboard){
        this.gameBoard = gameboard;
    }
}
