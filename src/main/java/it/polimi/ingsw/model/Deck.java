package it.polimi.ingsw.model;
import java.util.ArrayList;

public class Deck {
    private Mage mage;
    private ArrayList<Assistant> cards;
    private int numCards;


    public int getNumCards() {
        return numCards;
    }

    public Assistant draw() {

    }

    public Mage getMage() {
        return mage;
    }
}

enum Mage {
    MAGE,
    ELF,
    FAIRY,
    DRAGON;

}