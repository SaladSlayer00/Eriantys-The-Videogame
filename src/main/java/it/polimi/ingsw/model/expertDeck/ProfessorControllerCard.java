package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.playerBoard.Dashboard;

import java.util.ArrayList;
import java.util.List;

/* This card allows the player who summons it to control the professor even if they have the same number
* students of the player who has it in that very moment
* This is quite ambiguous BUT probably the player needs to have THE SAME number of students, not less!!!
 */
public class ProfessorControllerCard extends Character{
    private ExpertDeck name = ExpertDeck.COOK;
    private List<Color> chosen = new ArrayList<>();
    private GameController gameController;
    private TurnController turnController;
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
        for (Professor p : gameController.getGame().getGameBoard().getProfessors()) {
            for(Player player : gameController.getGame().getPlayers()){
                if(player.getDashboard().getRow(p.getColor()).getNumOfStudents()==gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getDashboard().getRow(p.getColor()).getNumOfStudents())
                    chosen.add(p.getColor());
                    gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getProfessors().add(p.getColor());
                    gameController.getGame().getGameBoard().removeProfessor(p.getColor());
            }
        }
        addCoin();
        gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).removeCoin(getCost());
    }
    @Override
    public void removeEffect(){
        for(Color c : chosen){
            gameController.getGame().getPlayerByNickname(turnController.getActivePlayer()).getProfessors().remove(c);
            gameController.getGame().getGameBoard().addProfessor(c);
            chosen.remove(c);
        }
    }
}
