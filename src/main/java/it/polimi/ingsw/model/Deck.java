package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import it.polimi.ingsw.exceptions.emptyDecktException;
import it.polimi.ingsw.model.enums.Mage;

/**
 * class deck represents the single instance of a deck that the player can use,
 * contains a List of Assistants and the name of the chosen mage
 */
public class Deck implements Serializable {
    //attributes
    private static final long serialVersionUID = -3704504226997118508L;
    private final Mage mage;
    private ArrayList<Assistant> cards = new ArrayList<Assistant>();
    private int numCards;


    public Deck(Mage m){
        this.numCards = 10;
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

    public int getNumCards() {
        return numCards;
    }

    /**
     * method to draw the chosen assistant from deck
     * @param indexCard the number of the card chosen
     * @return the chosen assistant or throws exception
     * @throws emptyDecktException
     */

    public Assistant draw(int indexCard) throws emptyDecktException{
        if (numCards > 0 ){
            for(Assistant a : cards){
                if(a.getNumOrder()==indexCard){
                    Assistant drawnCard = a;
                    cards.remove(a);
                    numCards = numCards-1;
                    return drawnCard;
                }
            }
        }else{
            throw new emptyDecktException();
        }
        return null;
    }

    public Mage getMage() {
        return mage;
    }

    public ArrayList<Assistant> getCards() {
        ArrayList<Assistant> copiedCards = new ArrayList<Assistant>(cards);
        return copiedCards;
    }

}

