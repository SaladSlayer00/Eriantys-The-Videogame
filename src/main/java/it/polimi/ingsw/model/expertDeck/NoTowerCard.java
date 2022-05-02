package it.polimi.ingsw.model.expertDeck;

/*when a player summons this card at the moment of the calculation of the influence the towers on the island
* are not to be taken in consideration
* JUST KEEP IN MIND: if the influence is calculated on a group of island all the towers on it are not considered
 */
public class NoTowerCard extends Character{

    public NoTowerCard(){
        super(3);
    }

    //the effect is a modified version of the calculation of the influence...

    public void useEffect(){

    }
}
