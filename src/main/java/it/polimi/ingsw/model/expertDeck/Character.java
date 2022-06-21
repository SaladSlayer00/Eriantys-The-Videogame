package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;


//class representing the character cards of the expert game mode
public abstract class Character{
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


    public void addCoin() {
        this.cost = this.cost +1;
    }
//TODO non rimuovere le carte da cui si pesca

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

