package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;

/*when a player summons this card at the moment of the calculation of the influence the towers on the island
* are not to be taken in consideration
* JUST KEEP IN MIND: if the influence is calculated on a group of island all the towers on it are not considered
 */
public class NoTowerCard extends Character{

    public NoTowerCard(){
        super(3);
    }
    private ExpertDeck name = ExpertDeck.CUSTOMER;
    //the effect is a modified version of the calculation of the influence...

    public void useEffect(Player p, Island i) throws noTowerException {
         if(i.getTower() == true){
             /* i don't really know right now how to do it
             * but we should find a way to check the color of the player
             * if the player passed as a variable in the method actually has a tower on the island
             * then we should subtract minus one to the influence of that player on that island
             * in this way it's like the island isn't calculate in the player's influence! (?)
              */
             //TODO
         }
    }

    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {

    }

    @Override
    public void removeEffect() {

    }
}
