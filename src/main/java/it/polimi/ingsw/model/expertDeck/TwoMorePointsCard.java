package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

/* this card gives two more influence points to the summoner
* many ways to implement this and maybe it would be better to discuss it before just writing
* stuffs and then delete everything because it doesn't work
 */
public class TwoMorePointsCard extends Character{
    private GameController gameController;
    private TurnController turnController;
    private ExpertDeck name = ExpertDeck.KNIGHT;

    //constructor
    public TwoMorePointsCard(GameController gameController, TurnController turnController){
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
        vv.showGenericMessage("You have two more points of influence!\n");
        turnController.getToReset().add(this);
    }

    @Override
    public void removeEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Two additional points removed!\n");
        turnController.getToReset().remove(this);
    }
}
