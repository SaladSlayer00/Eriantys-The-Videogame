package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

/**
 * Class for BARBARIAN card: this cards allows a player to transfer one of the students on it
 * to their row. Another student is picked from the sack to replace it.
 */
public class OneMoreStudentCard extends Character{
    //this card has an array as attribute for the four students
    private ArrayList<Student> students = new ArrayList<Student>();
    private GameController gameController;
    private TurnController turnController;
    private ExpertDeck name = ExpertDeck.BARBARIAN;

    public OneMoreStudentCard(GameController gameController, TurnController turnController) throws noMoreStudentsException {
        super(2);
        this.gameController = gameController;
        this.turnController = turnController;
        for(int i=0;i<4;i++){
            students.add(gameController.getGame().getGameBoard().getSack().drawStudent());
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
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.BARBARIAN);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect's over!\n");
    }

    /**
     * Method that adds the student to the row from the card, calls for check on influence.
     *
     * @param c the selected student from the card
     * @throws emptyDecktException if the player's deck is empty
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if the tower's number is full
     * @throws noStudentException if there's no student on the island
     * @throws noTowerException if there's no tower on the island
     * @throws invalidNumberException if the chose number is not allowed
     * @throws maxSizeException if the max size's reached
     */
    public void addStudent(Color c) throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException {
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
            try {
                gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().addStudent(st);
            } catch (maxSizeException e) {
                vv.showGenericMessage("Row is full!\n");
                return;
            }
            try {
                turnController.checkProfessors(c);
            } catch (noTowersException e) {
                gameController.win();
            }
            students.remove(st);
            students.add(gameController.getGame().getGameBoard().getSack().drawStudent());
            removeEffect();
        }
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
