package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Color;
import java.util.ArrayList;

public class Player {

    private String name;
    private Deck deck;
    private State state;
    private final Dashboard dashboard = new Dashboard();
    private int coins;
    private Assistant cardChosen;

    public Player(){

    }

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

    public boolean hasProfessor(Color color){
        return false;
    }
}
