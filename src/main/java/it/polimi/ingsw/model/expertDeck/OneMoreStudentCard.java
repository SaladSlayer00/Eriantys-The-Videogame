package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.exceptions.studentUnavailableException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;

import javax.naming.ContextNotEmptyException;
import java.util.ArrayList;

/* At the beginning of a match four students should be drawn from the sack and put on this card
* When a player uses this card, they can choose one of the student and put it on an island (anyone)
* Then they have to draw a student from the sack to replace the one they have chosen
 */
public class OneMoreStudentCard extends Character{
    //this card has an array as attribute for the four students
    private ArrayList<Student> students = new ArrayList<Student>(3);
    private Sack sack;
    private ExpertDeck name = ExpertDeck.BARBARIAN;

    //initialization of the card: the initial cost is 1 PLUS the four students are added randomly
    public OneMoreStudentCard() throws noMoreStudentsException {
        super(1);
        for(Student s : students){
            s = sack.drawStudent();
        }
    }

    //this is needed for the association sack and proper sack of the game
    public void setSack(Sack sack){
        this.sack = sack;
    }

    //need this for picking up one of the student when the player summons the card
    public Student pickUpStudent(Color c) throws studentUnavailableException {
        for(Student s : students){
            if(s.getColor().equals(c)){
                return s;
            }
        }
            throw new studentUnavailableException();
        }


    //this is the method that handle the effect of the card
    //should there be also the island's index and the part where the student is put o the island of choice???
    public Student useEffect(Player p, Color c) throws notEnoughMoneyException, studentUnavailableException, noMoreStudentsException{
        if(checkMoney(p) == true){
            Student s = pickUpStudent(c);
            students.add(sack.drawStudent());
            addCoin();
            return s;
        }
        else throw new notEnoughMoneyException();
    }

    public ExpertDeck getName() {
        return name;
    }
}
