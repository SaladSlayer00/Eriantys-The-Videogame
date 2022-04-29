package it.polimi.ingsw.model;
import java.util.ArrayList;
import it.polimi.ingsw.exceptions.emptyDecktException;
import it.polimi.ingsw.model.enums.Mage;

//This class represents the player's deck
public class Deck {
    //attributes
    private final Mage mage;
    private ArrayList<Assistant> cards;
    private int numCards;


    public Deck(Mage m){
        this.mage = m;
        int moves = 1;
        for(int i = 1;i<11;i++){
            Assistant a = new Assistant(i, moves);
            cards.add(a);
            if(i%2 == 0){
                moves++;
            }
        }
    }
    //methods
    public int getNumCards() {
        return numCards;
    }

    public Assistant draw(int indexCard) throws emptyDecktException{
        if (numCards > 0 )
        {
            Assistant drawnCard = cards.get(indexCard);
            cards.remove(indexCard);
            numCards = numCards-1;
            return drawnCard;
        }else{
            throw new emptyDecktException();
        }

    }

    public Mage getMage() {
        return mage;
    }

    public ArrayList<Assistant> getCards() {
        ArrayList<Assistant> copiedCards = new ArrayList<Assistant>(cards);
        return cards;
    }
}

