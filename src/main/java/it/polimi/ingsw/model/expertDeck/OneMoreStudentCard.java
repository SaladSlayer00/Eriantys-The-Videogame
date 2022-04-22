package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.exceptions.studentUnavailableException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;

/* At the beginning of a match four students should be drawn from the sack and put on this card
* When a player uses this card, they can choose one of the student and put it on an island (anyone)
* Then they have to draw a student from the sack to replace the one they have chosen
 */
public class OneMoreStudentCard extends Character{
    //this card has an array as attribute for the four students
    private Student[] students = new Student[3];
    private Sack sack;

    //initialization of the card: the initial cost is 1 PLUS the four students are added randomly
    public OneMoreStudentCard() throws noMoreStudentsException {
        super(1);
        for(Student s : students){
            s = sack.drawStudent();
        }
    }

    //this is needed for the association sack and proper sack of the game
    public void getSack(Sack sack){
        this.sack = sack;
    }

    //need this for picking up one of the student when the player summons the card
    public Student pickUpStudent(int i) throws studentUnavailableException {
        if(i < 0 || i > 3){
            throw new studentUnavailableException();
        }
        else{
            return students[i];
        }
    }

    //this is the method that handle the effect of the card
    //should there be also the island's index and the part where the student is put o the island of choice???
    public Student useEffect(Player p, int i) throws notEnoughMoneyException, studentUnavailableException, noMoreStudentsException {
        if(p.getCoins() < this.getCost()){
            throw new notEnoughMoneyException();
        }
        else{
            Student s = pickUpStudent(i);
            students[i] = sack.drawStudent();
            addCoin();
            return s;
        }
    }
}
