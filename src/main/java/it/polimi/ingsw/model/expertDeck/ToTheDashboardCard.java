package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.ExpertDeck;

import java.util.ArrayList;

/* the player that summons this card can pick one of the four students that are on this card
* NOTE that the four students are already on the card at the beginning of the game
 */
public class ToTheDashboardCard extends Character{

    private ExpertDeck name = ExpertDeck.TAVERNER;
    private ArrayList<Student> students;
    private Sack sack;

    //constructor
    public ToTheDashboardCard() throws noMoreStudentsException {
        super(2);
        for(Student s : students){
            s = sack.drawStudent();
        }
    }

    //this is for getting the sack used in the game
    public void getSack(Sack s){
        this.sack = s;
    }

    public ExpertDeck getName() {
        return name;
    }
}
