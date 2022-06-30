package it.polimi.ingsw.controller.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

/**Class for CUSTOMER card: when a player summons this card at the moment of the
 * calculation of the influence the towers on the island
* are not to be taken in consideration
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
        gameController.broadcastGenericMessage("Towers on the island are irrelevant!\n");
        turnController.getToReset().add(this);
    }

    @Override
    public void removeEffect() {
        gameController.broadcastGenericMessage("Tower effect removed!\n");
        turnController.getToReset().remove(this);
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
