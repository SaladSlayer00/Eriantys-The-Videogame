package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.playerBoard.Dashboard;

/* the player that summons this card can swap two of their students from the row to the dashboard
* NOTE is AT LEAST TWO which means that they can also swap JUST ONE student (which is not very smart i guess...)
 */
public class SwapTwoStudentsCard extends Character{

    //constructor
    public SwapTwoStudentsCard(){
        super(1);
    }

    //swaps the students
    public void swapFromHall(Student hallStudent, Color row, Player summoner){
        Dashboard summonerDash = summoner.getDashboard();
       // TODO if(summonerD.)
    }
}
