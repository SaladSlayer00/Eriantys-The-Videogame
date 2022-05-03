package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.playerBoard.Row;

/* the player that summons this card can choose a color and every player (themselves included) has to take
* three students from the row of the chosen color and put them back in the sack
* KEEP IN MIND that if a player hasn't got enough students of that color they have to put back all the students thay got
 */
public class RemoveAColorCard extends Character{

    //constructor
    public RemoveAColorCard(){
        super(3);
    }

    //this is the method that get the students of the chosen color
    public void removeStudents(Dashboard d){
        /* I still think that honestly handling the rows with an array is a very silly idea
        * i mean literally: how someone is supposed to understand some basic things without
        * passing the entire array???
         */
    }
}
/* BIG QUESTION!!!!!
* all the studetns means all the students of that color or ALL the students???
* i think the latter but who knows...
 */