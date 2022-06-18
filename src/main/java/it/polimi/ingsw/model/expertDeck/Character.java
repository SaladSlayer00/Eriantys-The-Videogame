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

    public boolean checkMoney(Player p){
        return p.getCoins() >= getCost();
    }

    public ExpertDeck getName() {
        return name;
    }


    public abstract void useEffect();

    public abstract void removeEffect();
}

