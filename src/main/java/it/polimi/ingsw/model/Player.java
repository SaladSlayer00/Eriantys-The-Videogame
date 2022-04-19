package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Color;
import java.util.ArrayList;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.exceptions.*;

public class Player {
    private int playerID;
    private String name;
    private Deck deck;
    private State state;
    private final Dashboard dashboard = new Dashboard();
    private int coins;
    private Assistant cardChosen;
    private ArrayList<Color> professors;
    private int group;

    public Player(){

    }

    //may be an idea to replace all these setters with the constructor???
    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setDeck(Mage mage){
        deck = new Deck(mage);
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

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getGroup() {
        return group;
    }

    //we assume that the card is chosen through the passage of the index of the card inside the deck
    public void drawCard(int cardIndex) throws emptyDecktException{
        cardChosen = deck.draw(cardIndex);
    }

    public void chooseCloud( ) {

    }

    //should be a good idea to add an exception in case of wrong game mode??
    public void addCoin() {
        coins += coins;
    }

    public void removeCoin() throws lowerLimitException {
        if (coins > 0)
        {
            coins -= coins;
        }else{
            throw new lowerLimitException();
        }
    }

    public void changeState(State nextState) {
            if(!(state.equals(nextState)))
            {
                state = nextState;
            }
    }

    //methods of the class dashboard
    public void addStudent (Student students) throws maxSizeException {
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

        if(this.professors.contains(color)){
            return true;
        }
        else
            return false;
    }

}
