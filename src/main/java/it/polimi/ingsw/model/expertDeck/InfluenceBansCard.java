package it.polimi.ingsw.model.expertDeck;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

/** Class for HERBALIST card: this card has four ban paws on it
* when a player summon the card they can put one of these paw on an island of their choice
* when Mother Nature ends her journey on that island, the paw is put again on the card and
* the influence is NOT calculated
 */

public class InfluenceBansCard extends Character{

    private ExpertDeck name = ExpertDeck.HERBALIST;
    private GameController gameController;
    private TurnController turnController;
    private int index;
    //constructor
    public InfluenceBansCard(GameController gameController, TurnController turnController){
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
        vv.showGenericMessage("Island chosen: " + index + "\n");
        int num = 0;
        for(Island i : gameController.getGame().getGameBoard().getIslands()){
            if(i.isBlocked())
                num+=1;
        }
        if(num>=4){
            vv.showGenericMessage("Too many blocked already!\n");
            return;
        }
        gameController.getGame().getGameBoard().getIslands().get(index).setBlocked(true);
    }

    public void removeEffect(int island){
        Island active = gameController.getGame().getGameBoard().getIslands().get(island);
        active.setBlocked(false);
        turnController.getToReset().remove(this);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect's over!\n");
    }
    @Override
    public void removeEffect() {
        Island active = gameController.getGame().getGameBoard().getIslands().get(gameController.getGame().getGameBoard().getMotherNature());
        active.setBlocked(false);
        turnController.getToReset().remove(this);
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Effect's over!\n");
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost()+turnController.getPrice().get(this.getName());
    }
}
