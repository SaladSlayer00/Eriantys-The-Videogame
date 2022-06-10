package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

/* the player that summons this card can pick one of the four students that are on this card
* NOTE that the four students are already on the card at the beginning of the game
 */
public class ToTheDashboardCard extends Character{

    private ExpertDeck name = ExpertDeck.TAVERNER;
    private ArrayList<Student> students;
    private Sack sack;
    private GameController gameController;
    private TurnController turnController;
    private final String text = "the player that summons this card can pick one of the four students that are on this card\n" +
            "* NOTE that the four students are already on the card at the beginning of the game";

    //constructor
    public ToTheDashboardCard(GameController gameController, TurnController turnController) throws noMoreStudentsException {
        super(2);
        this.gameController = gameController;
        this.turnController = turnController;
        sack = gameController.getGame().getGameBoard().getSack();
        for(int i=0;i<4;i++){
            students.add(sack.drawStudent());
        }
    }

    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Select a student from :");
    }

    @Override
    public void removeEffect() {

    }

    public String getText() {
        return text;
    }


    //TODO se ritorna null chiedere ancora
    public Student getStudent(Color c) {
        for(Student s : students){
            if(s.getColor().equals(c)){
                Student result = s;
                students.remove(s);
                return result;
            }
        }
        return null;
    }
}
