package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

/* This card allows the player who summons it to decide an island where they can calculate the
* influence even if Mother Nature has not finished there her movement
* !!!KEEP NOTICE!!!
* It STILL must be calculated the influence on the island where Mother Nature ends up to be!!!
 */
public class ImproperInfluenceCard extends Character{
    private ExpertDeck name = ExpertDeck.HERALD;
    private GameController gameController;
    private TurnController turnController;
    private int index = 0;
    //constructor
    public ImproperInfluenceCard(GameController gameController, TurnController turnController){
        super(3);
        this.gameController = gameController;
        this.turnController = turnController;
        index = 0;
    }


    public ExpertDeck getName() {
        return name;
    }

    @Override
    public void useEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Island chosen: " + index + "\n");
        try {
            turnController.checkInfluence(index);
        } catch (noTowerException e) {
            e.printStackTrace();
        } catch (noTowersException e) {
            e.printStackTrace();
        }
        try {
            turnController.islandMerger(gameController.getGame().getGameBoard().getIslands().get(index));
        } catch (noTowerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeEffect() {
        turnController.getToReset().remove(this);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect's over!\n");
        gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.HERALD);
    }

    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost()+turnController.getPrice().get(this.getName());
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
