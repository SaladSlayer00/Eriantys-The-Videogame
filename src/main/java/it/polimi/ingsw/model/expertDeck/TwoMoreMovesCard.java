package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.view.VirtualView;

/** Class for GAMBLER card: this card allows the summoner to move Mother Nature
 * of two more islands than the number that is
* written on the Assistant card they have played
 */
public class TwoMoreMovesCard extends Character{
    private ExpertDeck name = ExpertDeck.GAMBLER;
    private GameController gameController;
    private TurnController turnController;
    //constructor
    public TwoMoreMovesCard(GameController gameController, TurnController turnController){
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
        vv.showGenericMessage("You have two more moves for mother nature!\n");
        turnController.getToReset().add(this);
    }

    @Override
    public void removeEffect() {
        VirtualView vv = gameController.getVirtualViewMap().get(turnController.getActivePlayer());
        vv.showGenericMessage("Your 2 more moves effect was removed!\n");
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
