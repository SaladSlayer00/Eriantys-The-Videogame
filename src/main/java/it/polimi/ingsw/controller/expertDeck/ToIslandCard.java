package it.polimi.ingsw.controller.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

/** Class for TAVERNER card: this cards allows the player to transfer a student from this card to an
 * island they can choose.
 */
public class ToIslandCard extends Character{
    private ExpertDeck name = ExpertDeck.TAVERNER;
    private ArrayList<Student> students = new ArrayList<>();
    private Sack sack;
    private Student chosen = null;
    private GameController gameController;
    private TurnController turnController;
    private final String text = "The player that summons this card can pick one of the four students that are on this card\n";

    //constructor
    public ToIslandCard(GameController gameController, TurnController turnController) throws noMoreStudentsException {
        super(1);
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
        vv.showGenericMessage("Please choose a student to pick from the card!\n");
        for(Student s : students){
            vv.showGenericMessage(s.getColor().getText()+"\n");
        }
        vv.askColor();
    }

    @Override
    public void removeEffect() {
        chosen = null;
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.TAVERNER);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect was removed!\n");
        gameController.getGame().updateGameboard();
        vv.askMoves(gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall(), gameController.getGame().getGameBoard().getIslands());

    }

    public String getText() {
        return text;
    }

    /**
     * Method to receive the selected student from the card and ask for the island the player
     * will put it on.
     *
     * @param c the selected student color
     */
    public void getStudent(Color c) {
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
            vv.showGenericMessage("Student "+c.getText()+ " selected!\n");
            this.chosen = st;
            students.remove(st);
            vv.askMoves(gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall(), gameController.getGame().getGameBoard().getIslands());
        }


    }

    /**
     * Method to set the chosen island and move the student on it.
     *
     * @param index the selected island index
     */
    public void getIsland(int index){
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage(("Island chosen: "+index+"\n"));
        gameController.getGame().getGameBoard().getIslands().get(index).addStudentOnIsland(chosen.getColor(), chosen);
        students.add(gameController.getGame().getGameBoard().getSack().drawStudent());
        removeEffect();

    }

    public Student getChosen() {
        return chosen;
    }

    /**
     * Method that checks if the player has enough money to play the card
     * @param p the player that called the card's effect
     * @return boolean value to notify the operation's outcome
     */
    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost()+turnController.getPrice().get(this.getName());
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
