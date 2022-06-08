package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;

/* TODO
* GUYS. THE FACADE PATTER WON'T WORK HERE (or at least i think...)
* the idea might be to use an adapter or maybe a wrapper???
* we should probably discuss this thing more deeply but i think the adapter pattern might actually work
* i hope so at least, because in other cases the only thing that might work that comes to my mind is an enormous case switch
 */

//class representing the character cards of the expert game mode
public abstract class Character{
    //attributes of the class Character
    private int cost;
    private int numCoins;
    private boolean isCalled;
    private ExpertDeck name;

    //methods of the class Character
    public Character(int cost){
       this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public boolean getUsage(){
        return isCalled;
    }

    public void addCoin() {
        if(getUsage() == false){
            cost += cost;
        }
    }


    public boolean checkMoney(Player p){
        if(p.getCoins() < getCost()){
            return false;
        }
        else return true;
    }

    public ExpertDeck getName() {
        return name;
    }
}

