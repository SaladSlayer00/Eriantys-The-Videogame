package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;

/* when a player summons this card they can choose a color that will have no influence in the calculation
* of the influence
 */
public class NullColorCard extends Character{
    private ExpertDeck name = ExpertDeck.SELLER;
    public NullColorCard(){
        super(3);
    }

    //this is the method for the effect (ish..?)
    //the parameter is the color that the player have chosen to "block"
    public void useEffect(Color color, Island mnPosition){
        //HOW THE HECK DO WE DO THIS THINNNNG
        //the idea of the calculation of the influence as a subtraction may be a cool idea
        //BUT maybe we should discuss this stuff cuz I'm not sure how to implement it...
        //TODO
    }

    public ExpertDeck getName() {
        return name;
    }
}
