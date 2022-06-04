package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import it.polimi.ingsw.exceptions.emptyDecktException;
import it.polimi.ingsw.model.enums.Mage;

//This class represents the player's deck
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
    //methods
    public int getNumCards() {
        return numCards;
    }

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
//        {
//            Assistant drawnCard = cards.get(indexCard-1);
//            cards.remove(indexCard-1);
//            numCards = numCards-1;
//            return drawnCard;
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

