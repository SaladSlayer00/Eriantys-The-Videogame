package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

/*when a player summons this card at the moment of the calculation of the influence the towers on the island
* are not to be taken in consideration
* JUST KEEP IN MIND: if the influence is calculated on a group of island all the towers on it are not considered
 */
public class NoTowerCard extends Character{
    private ExpertDeck name = ExpertDeck.CUSTOMER;
    private GameController gameController;
    private TurnController turnController;


    public NoTowerCard(GameController gameController, TurnController turnController){
        super(3);
        this.gameController = gameController;
        this.turnController = turnController;
    }




    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Towers on the island are irrelevant!\n");
        turnController.getToReset().add(this);
    }

    @Override
    public void removeEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Tower effect removed!\n");
        turnController.getToReset().remove(this);
    }
}
