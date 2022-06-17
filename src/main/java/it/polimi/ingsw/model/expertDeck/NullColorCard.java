package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/* when a player summons this card they can choose a color that will have no influence in the calculation
* of the influence
 */
public class NullColorCard extends Character{
    private ExpertDeck name = ExpertDeck.SELLER;
    private GameController gameController;
    private TurnController turnController;
    private Color color;

    public NullColorCard(GameController gameController, TurnController turnController){
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
        vv.showGenericMessage("You can choose a color that will not take part in the influence count!\n");
        vv.askColor();
    }

    @Override
    public void removeEffect() {
        turnController.getToReset().remove(this);
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.SELLER);
    }

    public void setColor(Color color) {
        this.color = color;
        turnController.getBanned().add(color);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("The color " + color.getText() + " is irrelevant!\n");
        removeEffect();
    }

    public Color getColor(){
        return color;
    }

}
