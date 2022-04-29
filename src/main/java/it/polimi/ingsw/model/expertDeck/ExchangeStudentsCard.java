package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.notPresentStudentException;
import it.polimi.ingsw.model.enums.Color;
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
    public Student swap(Color wanted, Student playerChoice) throws notPresentStudentException {
        for(Student s : students){
            if(s.getColor().equals(wanted)){
                Student temp = new Student(s.getColor());
                s = playerChoice;
                return temp;
            }
        }
        throw new notPresentStudentException();
    }

    /* is useEffect() really necessary???
    * an idea may be to pass the student that has been swapped to the controller and then
    * it becomes a controller's problem
    * TODO (added this only to remember to discuss it next time)
     */
}
