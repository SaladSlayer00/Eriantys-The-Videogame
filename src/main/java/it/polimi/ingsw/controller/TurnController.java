package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class TurnController {
    private final NamePhase[] phases = {NamePhase.INITIALIZING, NamePhase.PLANNING, NamePhase.ACTION, NamePhase.ENDING};
    private ArrayList<Player> playerOrder;
    private Player activePlayer;
    int activePhase = 0;

    public void switchPhase(){
        switch (activePhase){
            case 0:
                handleInit();
                break;
            case 1:
                handlePlanning();
                break;
            case 2:
                handleAction();
                break;
            case 3:
                handleEnding();
                break;
        }
    }
    public void handleInit(){
        Phase initialization = new Phase();
    }

    public void handlePlanning(){}

    public void handleAction(){}

    public void handleEnding(){}


}
