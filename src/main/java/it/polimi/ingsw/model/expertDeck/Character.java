package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.Player;

//class representing the character cards of the expert game mode
public abstract class Character{
    //attributes of the class Character
    private int cost;
    private int numCoins;

    //methods of the class Character
    public Character(int cost){
       this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public void addCoin() {
        numCoins += numCoins;
    }

    //this method is completely useless!!!
    public void useEffect(){

    }

    public boolean checkMoney(Player p){
        if(p.getCoins() < getCost()){
            return false;
        }
        else return true;
    }
}
