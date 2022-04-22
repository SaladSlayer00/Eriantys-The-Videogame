package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Island;

/* This card allows the player who summons it to decide an island where they can calculate the
* influence even if Mother Nature has not finished there her movement
* !!!KEEP NOTICE!!!
* It STILL must be calculated the influence on the island where Mother Nature ends up to be!!!
 */
public class ImproperInfluenceCard extends Character{

    //constructor
    public ImproperInfluenceCard(){
        super(3);
    }

    public int useEffect(Player p, Island chosenIsland) throws notEnoughMoneyException {
        if(checkMoney(p) == false){
            throw new notEnoughMoneyException();
        }
        else{
            addCoin();
            return chosenIsland.getInfluence();
        }
    }
}
