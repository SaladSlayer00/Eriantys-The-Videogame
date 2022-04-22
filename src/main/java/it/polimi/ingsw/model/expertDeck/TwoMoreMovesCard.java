package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.model.Player;

/* This card allows the summoner to move Mother Nature of two more islands than the number that is
* written on the Assistant card they have played
 */
public class TwoMoreMovesCard extends Character{

    //constructor
    public TwoMoreMovesCard(){
        super(1);
    }

    public void useEffect(Player p) throws notEnoughMoneyException {
        if(checkMoney(p) == false){
            throw new notEnoughMoneyException();
        }
        else{
            addCoin();
            //some method to allow the card to modify the number of moves of the assistant card!!!
            //TODO
        }
    }
}
