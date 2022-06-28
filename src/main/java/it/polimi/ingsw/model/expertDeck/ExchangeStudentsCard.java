package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.noStudentException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/** Class for JOKER card: At the beginning of the match six students must be drawn and put on this card
* The summoner can exchange as much as three students in their hall with three students on the card
 */
public class ExchangeStudentsCard extends Character{

    private ArrayList<Student> students = new ArrayList<Student>();
    private ExpertDeck name = ExpertDeck.JOKER;
    private GameController gameController;
    private TurnController turnController;
    private int calls =0;
    private Color hall=null;
    private Color card = null;
    //constructor
    public ExchangeStudentsCard(GameController gameController, TurnController turnController) throws noMoreStudentsException {
        super(1);
        this.gameController = gameController;
        this.turnController = turnController;
        for(int i=0;i<6;i++){
            students.add(gameController.getGame().getGameBoard().getSack().drawStudent());
        }
    }

    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Please choose a student to pick from the hall!\n");
        for(Student s : gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall()){
            vv.showGenericMessage(s.getColor()+"\n");
        }
        vv.askColor();
    }

    @Override
    public void removeEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect was removed!\n");
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.JOKER);
        gameController.getGame().updateGameboard();
        vv.askMoves(gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall(), gameController.getGame().getGameBoard().getIslands());

    }

    /**
     * This method allows for the player to choose a student in their hall and asks the client
     * to input a student to choose from the card
     *
     * @param c the selected color for the hall student
     */

    public void getColorHall(Color c){
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        List<Student> students = gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall();
        List<Color> colors = new ArrayList<>();
        for(Student s : students){
            colors.add(s.getColor());
        }
        if(!colors.contains(c)){
            vv.showGenericMessage("There's no "+ c.getText()+ " student in the hall!\n");
            useEffect();
        }

        else {
            hall = c;
            vv.showGenericMessage("You picked a " + hall.getText() + " student!\n");
            vv.showGenericMessage("Please choose a student to pick from the card!\n");
            for(Student s : this.students){
                vv.showGenericMessage(s.getColor().getText()+"\n");
            }
            vv.askColor();
        }

    }

    /**
     * This method allows for the swap between the student on the card and the one on the hall
     *
     * @param c the color of the selected student to take from the card
     */
    public void swapStudent(Color c){
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
            card = c;
            vv.showGenericMessage("Swapping the " + card.getText() + " student with a " + hall.getText() + " one!\n");
            gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall().add(st);
            students.remove(st);
            try {
                students.add(gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().takeStudent(hall));
            } catch (noStudentException e) {
                e.printStackTrace();
            }
        }
        hall=null;
        calls+=1;
        if (calls < 3) {
            vv.showGenericMessage("Would you like to choose another?\n");
            vv.askStart(turnController.getActivePlayer(), "START");
        } else
            removeEffect();
    }

    public Color getHall() {
        return hall;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * Method that checks if the player has enough money to play the card
     * @param p the player that called the card's effect
     * @return boolean value to notify the operation's outcome
     */
    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost()+turnController.getPrice().get(this.getName());
    }

}
