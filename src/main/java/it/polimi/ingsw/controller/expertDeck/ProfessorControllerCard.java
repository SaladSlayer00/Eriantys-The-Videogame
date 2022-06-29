package it.polimi.ingsw.controller.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.alreadyAProfessorException;
import it.polimi.ingsw.exceptions.noProfessorException;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/** Class for COOK card: this card allows the player who summons it to control the professor even if they have the same number
* students of the player who has it in that very moment
 */
public class ProfessorControllerCard extends Character{
    private ExpertDeck name = ExpertDeck.COOK;
    private List<Color> chosen = new ArrayList<>();
    private final GameController gameController;
    private final TurnController turnController;
    private Player caller;
    //constructor of the card
    public ProfessorControllerCard(GameController gameController, TurnController turnController){
        super(2);
        this.gameController = gameController;
        this.turnController = turnController;
    }


    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Checking for professors\n");
        chosen = new ArrayList<>();
        boolean result = false;
        caller = gameController.getGame().getPlayerByNickname(turnController.getActivePlayer());
        ArrayList<Professor> temp = new ArrayList<>();
        temp.addAll(gameController.getGame().getGameBoard().getProfessors());
        for (Professor p : temp) {
            for(Player player : gameController.getGame().getPlayers()) {
                if (player != caller) {
                    if (player.getDashboard().getRow(p.getColor()).getNumOfStudents() == caller.getDashboard().getRow(p.getColor()).getNumOfStudents() && caller.getDashboard().getRow(p.getColor()).getNumOfStudents() > 0) {
                        result = true;
                    }
                }
            }
            if(result) {
                chosen.add(p.getColor());
                try {
                    gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getRow(p.getColor()).addProfessor();
                } catch (alreadyAProfessorException e) {
                    e.printStackTrace();
                }
                gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getProfessors().add(p.getColor());
                gameController.getGame().getGameBoard().removeProfessor(p.getColor());
                result = false;
            }

        }
    }
    @Override
    public void removeEffect(){
        VirtualView vv = gameController.getVirtualViewMap().get(caller.getName());
        vv.showGenericMessage("Your expert effect was removed!\n");
        for(Color c : chosen){
            try {
                caller.getDashboard().getRow(c).removeProfessor();
            } catch (noProfessorException e) {
                e.printStackTrace();
            }
            caller.getProfessors().remove(c);
            gameController.getGame().getGameBoard().addProfessor(c);
        }
    }
    //TODO rimuovere a fine turno

    /**
     * Method that checks if the player has enough money to play the card
     * @param p the player that called the card's effect
     * @return boolean value to notify the operation's outcome
     */
    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost()+turnController.getPrice().get(this.getName());
    }
}
