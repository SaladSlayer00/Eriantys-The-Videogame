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

    /* this method modifies the game pattern but it doesn't fully replace the normal game logic!!
    * note that the controller must call the calculation of the influence on the right island anyway!!!
    * this is just the ""extra"" part caused by the summoning of the expert card...
     */
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
