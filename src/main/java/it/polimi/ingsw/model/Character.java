package it.polimi.ingsw.model;
//class representing the character cards of the expert game mode
public class Character{
    //attributes of the class Character
    private int cost;
    private int numCoins;
    //methods of the class Character
    public Character(int cost){
        this.cost = cost;
        numCoins = cost;
    }
    public void addCoin() {
        numCoins += numCoins;
    }
    public void useEffect(){

    }
}
