package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/* the player that summons this card can swap two of their students from the row to the dashboard
* NOTE is AT LEAST TWO which means that they can also swap JUST ONE student (which is not very smart i guess...)
 */
public class SwapTwoStudentsCard extends Character{
    private ExpertDeck name = ExpertDeck.MUSICIAN;
    private GameController gameController;
    private TurnController turnController;
    private Color hall=null;
    private Color row=null;
    private int calls = 0;
    //constructor
    public SwapTwoStudentsCard(GameController gameController, TurnController turnController){
        super(1);
        this.gameController = gameController;
        this.turnController = turnController;
    }


    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Please choose a student to take from the hall!\n");
        for(Student s : gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall()){
            vv.showGenericMessage(s.getColor()+"\n");
        }
        vv.askColor();

    }

    @Override
    public void removeEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect was removed!\n");
        turnController.getToReset().remove(this);
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.MUSICIAN);
        gameController.getGame().updateGameboard();
        vv.askMoves(gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getHall(), gameController.getGame().getGameBoard().getIslands());

    }

    //al prossimo askmoves si resetta
    //gli posso mandare un null, sceltanellhandler di turn
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
            vv.showGenericMessage("Please choose a student to pick from the row!\n");
            vv.askColor();
        }

    }

    public void getColorRow(Color c) throws noStudentException, maxSizeException {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        Dashboard d = gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard();
        if(d.getRow(c).getNumOfStudents()<1){
            vv.showGenericMessage("You haven't enough "+ c.getText()+" students!\n");
            vv.askColor();
        }
        else {
            row = c;
            vv.showGenericMessage("Swapping the " + row.getText() + " student with a " + hall.getText() + " one!\n");
            calls += 1;
            d.addStudent(d.takeStudent(hall));
            d.addToHall(d.getRow(row).removeStudent());
            try {
                turnController.checkProfessors(row);
                turnController.checkProfessors(hall);
            } catch (emptyDecktException e) {
                e.printStackTrace();
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            } catch (fullTowersException e) {
                e.printStackTrace();
            } catch (noTowerException e) {
                e.printStackTrace();
            } catch (invalidNumberException e) {
                e.printStackTrace();
            } catch (noTowersException e) {
                e.printStackTrace();
            }
            hall = null;
            if (calls < 2) {
                vv.showGenericMessage("Would you like to choose another?\n");
                vv.askStart(turnController.getActivePlayer(), "START");
            } else
                removeEffect();
        }
    }

    public Color getHall() {
        return hall;
    }
}
