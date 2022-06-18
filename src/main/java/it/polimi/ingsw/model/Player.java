package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.State;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.exceptions.*;

public class Player implements Serializable {
    private static final long serialVersionUID = -3704504226997118508L;
    private int playerID;
    private String name;
    private Deck deck;
    private State state;
    private Dashboard dashboard;
    private int coins = 0;
    private Assistant cardChosen;
    private ArrayList<Color> professors = new ArrayList<>();
    private int group = 0;

    public Player(String name, int playerID){
        this.name = name;
        this.playerID = playerID;
        this.state = State.START;
    }

    public void setDashboard(Dashboard playerDashboard){
        dashboard = playerDashboard;
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
    public void setCard(Assistant card) {
        cardChosen = card;
    }

    public Assistant getCardChosen() {
        return cardChosen;
    }

    public void addCoin(int num) {
        coins = coins+num;
    }

    public void removeCoin(int num) {
        coins = coins-num;
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

    public Student takeStudent (Color c) throws noStudentException {
        return dashboard.takeStudent(c);
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

    //no oggetti solo colori ????
    public ArrayList<Color> getProfessors() {
        return professors;
    }
}
