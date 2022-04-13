package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Deck;


// Player class represents the user
public class Player {

    //attributes of the class Player
    private String name;
    private Deck deck;
    private State state;
    private Dashboard dashboard;
    private int coins;

    //methods of the Player
    //it returns the player's name
    public String getName() {
        return name;
    }
    //it returns the player's deck
    public Deck getDeck() {
        return deck;
    }
    //it returns the player's dashboard
    public Dashboard getDashboard() {
        return dashboard;
    }
    //it returns the player's number of coins
    public int getCoins() {
        return coins;
    }
    //it returns the player's state
    public State getState() {
        return state;
    }


    //it draws a card from the assistant deck
    public Assistant drawCard() {


    }
    //it allows the player to choose the island
    public void chooseCloud( ) {

    }
    //it adds a coin
    public void addCoin() {

    }
    //it remove a coin
    public void removeCoin() {

    }
    //it change the player's state
    public void changeState() {

    }
}
