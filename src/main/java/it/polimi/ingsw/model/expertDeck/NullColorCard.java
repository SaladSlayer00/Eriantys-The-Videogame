package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.Color;
//TODO
/* when a player summons this card they can choose a color that will have no influence in the calculation
* of the influence
 */
public class NullColorCard extends Character{

    public NullColorCard(){
        super(3);
    }

    //this is the method for the effect (ish..?)
    //the parameter is the color that the player have chosen to "block"
    public void useEffect(Color color){
        /* THIS MAY BE AN IDEA
        * what can it be done???
        * it csn be called the method that calculate the influence as usual
        * AND THEN the method count the number of students of the color chosen and then
        * INFLUENCE - NUMBER JUST COUNTED
         */
    }
}
