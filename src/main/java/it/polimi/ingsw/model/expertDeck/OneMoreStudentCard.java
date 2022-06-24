package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.exceptions.studentUnavailableException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

/* carta che fa passare uno studente da lei alla hall
 */
public class OneMoreStudentCard extends Character{
    //this card has an array as attribute for the four students
    private ArrayList<Student> students = new ArrayList<Student>();
    private GameController gameController;
    private TurnController turnController;
    private ExpertDeck name = ExpertDeck.BARBARIAN;

    //initialization of the card: the initial cost is 1 PLUS the four students are added randomly
    public OneMoreStudentCard(GameController gameController, TurnController turnController) throws noMoreStudentsException {
        super(2);
        this.gameController = gameController;
        this.turnController = turnController;
        for(int i=0;i<4;i++){
            students.add(gameController.getGame().getGameBoard().getSack().drawStudent());
        }
        gameController.getGame().getGameBoard().setActiveCards(turnController.getToReset());
        gameController.getGame().updateGameboard();
    }


    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Please choose a student to pick from the card!\n");
        for(Student s : students){
            vv.showGenericMessage(s.getColor().getText()+"\n");
        }
        vv.askColor();
    }

    @Override
    public void removeEffect() {
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.BARBARIAN);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect's over!\n");
    }

    public void addStudent(Color c){
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        Student st=null;
        for(Student s : students){
            if(s.getColor().equals(c)){
                st=s;
            }
        }
        if(st==null){
            vv.showGenericMessage("Student not present!\n");
            useEffect();
        }
        else{
            gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall().add(st);
            students.remove(st);
            students.add(gameController.getGame().getGameBoard().getSack().drawStudent());
        }
    }

    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost()+turnController.getPrice().get(this.getName());
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
