package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Trashpile {

    private ArrayList<Assistant> trash;

    public void addCard(Assistant card){
        trash.add(card);
    }
}
