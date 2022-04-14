package it.polimi.ingsw.model;


import java.util.ArrayList;

// Player class represents the user
public class Player{

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
    //I assume that the input passed to the function is the integer indicating the index that allows us to find the desired card within the array representing the deck
    //Tbh i do not like it
    public Assistant drawCard(int indexCard) {
        try {
            Assistant drawnCard = deck.draw(indexCard);
            return drawnCard;
        } catch (EmptyDecktException e) {
            System.out.println("Invalid operation : Your deck is empty. You cannot draw any more cards");
            return null;

        }

    }

    //it allows the player to choose the island
    public int chooseCloud() {

    }

    //it adds a coin
    public void addCoin() {
        coins = coins + 1;
    }

    //it remove a coin
    public void removeCoin() throws LowerLimitException {
        if (coins > 0) {
            coins = coins - 1;

        } else {
            throw new LowerLimitException();
        }
    }

    //it change the player's state
    public void changeState() {


    }

    public void setCoins(int coins) {
        this.coins = coins;
    }


    //exception to manage the fact that you have reached the minimum limit of coins
    public class LowerLimitException extends Exception {
        LowerLimitException() {
            super("You have reached the minimum coin limit");
        }
    }

    class Trashpile {

        private ArrayList<Assistant> trash;

        public void addCard(Assistant card) {
            trash.add(card);
        }
    }
}