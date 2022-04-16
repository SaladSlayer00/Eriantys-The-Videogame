package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Color;
import java.util.ArrayList;
import it.polimi.ingsw.model.Dashboard;

public class Player {

    private String name;
    private Deck deck;
    private State state;
    private final Dashboard dashboard = new Dashboard();
    private int coins;
    private Assistant cardChosen;
    private ArrayList<Color> professors;

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



    //we assume that the card is chosen through the passage of the index of the card inside the deck
    public void drawCard(int cardIndex) throws EmptyDecktException{
        cardChosen = deck.draw(cardIndex);
    }

    public void chooseCloud( ) {

    }

    public void addCoin() {
        coins += coins;
    }

    public void removeCoin() throws LowerLimitException {
        if (coins > 0)
        {
            coins -= coins;
        }else{
            throw new LowerLimitException();
        }
    }

    public void changeState(State nextState) {
            if(!(state.equals(nextState)))
            {
                state = nextState;
            }
    }

    //methods of the class dashboard
    public void addStudent (Student students) throws MaxSizeException {
        dashboard.addStudent(students);
    }

    public void takeStudent (Color c) throws noStudentException {
        dashboard.takeStudent(c);

    }
    public void putTower () throws fullTowersException {
        dashboard.putTower();

    }

    public void getTower () throws noTowersException {
        dashboard.getTower();
    }

    public boolean hasProfessor(Color color){
        return false;
    }

    public class  LowerLimitException extends Exception {
        LowerLimitException(){super("you have reached the minimum number of coins");}

    }



}
