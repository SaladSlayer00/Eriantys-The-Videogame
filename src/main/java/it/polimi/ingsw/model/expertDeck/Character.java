package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;

import java.io.Serializable;


/**Class representing the character cards of the expert game mode
 * it has turnController and gameController attributes to allow every card operation,
 * and keeps track of the card's cost and name.
 *
 * The effect of every card is called by the useEffect method that every subclass implements,
 * together with the removeEffect that resets the action of the card
 */
public abstract class Character implements Serializable {
    //attributes of the class Character
    private int cost;
    private int numCoins;
    private ExpertDeck name;
    private TurnController turnController;
    private GameController gameController;


    //methods of the class Character
    public Character(int cost){
       this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public ExpertDeck getName() {
        return name;
    }

    public abstract void useEffect();

    public abstract void removeEffect();

    public void setController(GameController gameController, TurnController turnController) {
        this.gameController = gameController;
        this.turnController = turnController;
    }
}

