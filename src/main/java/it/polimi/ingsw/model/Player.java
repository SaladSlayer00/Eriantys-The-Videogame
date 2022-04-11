package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Deck;

public class Player {

    private String name;
    private Deck deck;
    private State state;
    private Dashboard dashboard;
    private int coins;

    public String getName() {
        return name;
    }

    public Deck getDeck() {
        return deck;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public int getCoins() {
        return coins;
    }

    public State getState() {
        return state;
    }



    public Assistant drawCard() {

    }

    public void chooseCloud( ) {

    }

    public void addCoin() {

    }

    public void removeCoin() {

    }

    public void changeState() {

    }
}
