package it.polimi.ingsw.model;
import java.util.ArrayList;
//This class represents the player's deck
public class Deck {
    //attributes
    private Mage mage;
    private ArrayList<Assistant> cards;
    private int numCards;

    //methods
    public int getNumCards() {
        return numCards;
    }

    public Assistant draw(int indexCard) throws EmptyDecktException{
        if (numCards > 0 )
        {
            Assistant drawnCard = cards.get(indexCard);
            cards.remove(indexCard);
            numCards = numCards-1;
            return drawnCard;
        }else{
            throw new EmptyDecktException();
        }

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

class EmptyDecktException extends Exception{
    EmptyDecktException(){
        super("Your deck is empty. You cannot draw any more cards");
    }
}