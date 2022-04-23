package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import java.util.ArrayList;

/* At the beginning of the match six students must be drawn and put on this card
* The summoner can exchange as much as three students in their hall with three students on the card
 */
public class ExchangeStudentsCard extends Character{

    //ArrayList for the students of the card
    private ArrayList<Student> students = new ArrayList<Student>(6);
    Sack sack;

    //constructor
    public ExchangeStudentsCard() throws noMoreStudentsException {
        super(1);
        for(Student s : students){
            s = sack.drawStudent();
        }
    }

    //method that associates the sack to this sack
    public void setSack(Sack gameSack){
        this.sack = gameSack;
    }

    //this is the method that replaces the students
    public void replace(Color wanted, Student playerChoice){

    }
}
