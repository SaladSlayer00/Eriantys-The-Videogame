package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.State;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    //things that will be quite essential for the tests
    Player playerT;
    private final int playerID = 0;
    private final String name = "nikeTest";
    private String chosenMage = "fairy";

    //set up for the Player
    @BeforeEach
    void startingSetUp(){
        playerT = new Player(name, playerID);
        playerT.setDeck(Mage.FAIRY);
        playerT.setDashboard(new Dashboard(2));
    }

    //tests the getter of the name
    @Test
    @DisplayName("Name getter and ID getter test")
    void getNameTest() {
        assertEquals(name, playerT.getName());
        assertEquals(playerID, playerT.getPlayerID());
    }

    //tests the getter of the deck
    @Test
    void getDeckTest() {
        assertEquals(playerT.getDeck().getMage(),Mage.FAIRY);
    }

    //it returns the player's dashboard
    @Test
    @DisplayName("Player's dashboard test")
    void getDashboardTets() {
       //should find a way to test this because in this way it's impossible...
        assertNotNull(playerT.getDashboard());
        //metodo indiretto per controllare che sia stata creata una dashboard coerente per il player che sia coerente con il numero di giocatori
        assertEquals(8,playerT.getDashboard().getNumTowers());
        assertEquals(7,playerT.getDashboard().getHallDimension());
    }


    //it resturns the number of coins
    @Test
    @DisplayName("Player's coins setter and getter test")
    void CoinsTest() throws lowerLimitException {
        playerT.addCoin(1);
        assertEquals(1, playerT.getCoins());
        playerT.removeCoin(1);
        assertEquals(0, playerT.getCoins());
        assertThrows(lowerLimitException.class,()->playerT.removeCoin(1));
    }

    /* dunno if this could be tested here...
    @Test
    void getStateTest() {
    }
    */

    //tests the getter and setter of the player's group
    @Test
    @DisplayName("Setter and getter of the player's group")
    void groupTest(){
        playerT.setGroup(1);
        assertEquals(1, playerT.getGroup());
    }

    //tests the setter and the getter for the Assistant
    @Test
    void AssistantTest() {
        Assistant aTest = new Assistant(4,3);
        playerT.setCard(aTest);
        assertEquals(aTest,playerT.getCardChosen());
    }
    /* this method uses dashboard so probably not very useful to test it here ???
    @Test
    void takeStudent() {
    }
    */
    //it tests :

    @Test
    void StateTest(){
        assertEquals(State.START,playerT.getState());
        playerT.changeState(State.ACTIVE);
        assertEquals(State.ACTIVE,playerT.getState());
    }
    /*
    @Test
    void changeStateTest() {}
    @Test
    void setStateTest(){}
     */

    //returns a boolean true if the player has the professor of the color's parameter
    @Test
    void hasProfessorTets() {
        playerT.getProfessors().add(Color.BLUE);
        playerT.getProfessors().add(Color.RED);
        assertTrue(playerT.hasProfessor(Color.BLUE));
        assertTrue(playerT.hasProfessor(Color.RED));
        assertFalse(playerT.hasProfessor(Color.GREEN));

    }

    @Test
    void StudentTest() throws maxSizeException, noStudentException {
        Student s1 = new Student(Color.GREEN);
        playerT.addStudent(s1);
        assertTrue(playerT.getDashboard().getRow(s1.getColor()).getStudents().contains(s1));
        s1 = new Student(Color.RED);
        playerT.getDashboard().getHall().add(s1);
        playerT.takeStudent(Color.RED);
        assertFalse(playerT.getDashboard().getHall().contains(s1));
        assertThrows(noStudentException.class,()->playerT.takeStudent(Color.GREEN));
        for(int i = 0; i < 10;i++){
            playerT.addStudent(new Student(Color.GREEN));
        }
        assertThrows(maxSizeException.class,()->playerT.addStudent(new Student(Color.GREEN)));
    }
    @Test
    void TowerTest() throws noTowersException, fullTowersException {
        playerT.getTower();
        assertEquals(7,playerT.getDashboard().getNumTowers());
        playerT.putTower();
        assertEquals(8,playerT.getDashboard().getNumTowers());
        assertThrows(fullTowersException.class,()->playerT.putTower());
        for(int i = 0; i < 8 ; i++){
            playerT.getTower();
        }
        assertThrows(noTowersException.class,()->playerT.getTower());
    }
    /*

    static class RowTest {

        @Test
        void getName() {
        }

        @Test
        void addProfessor() {
        }

        @Test
        void addStudent() {
        }

        @Test
        void removeProfessor() {
        }
    }

     */
}