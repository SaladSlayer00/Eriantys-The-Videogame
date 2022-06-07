package it.polimi.ingsw.model.test;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.exceptions.*;
import java.util.List;
import java.awt.*;


/*
  Class EasyGame Test EasyGame Class
 */


public class EasyGameTest {
    EasyGame egTest;
    Player p1;
    Player p2;
    Player p3;

    @BeforeEach
    void startingSetup() throws noMoreStudentsException , maxSizeException {
        egTest = new EasyGame(2);
        p1 = new Player("Paperino",0);
        p2 = new Player("Topolino",1);
        p3 = new Player("Gastone" ,2);
        egTest.initializePlayer(p1);
        egTest.initializePlayer(p2);
        egTest.initializeGameboard();
        egTest.initializeDashboards();

    }
    /*
      Method playerGettingByIdTest aims to verify the correctness of the player information getters,
      like the nickname and the ID ones.
     */

    @Test
    void getChosePlayersNumberTest() {
        assertEquals(2,egTest.getChosenPlayerNumber());
    }

    @Test
    @DisplayName("getPlayerByNickname method test - Nickname found ")
    void getPlayerByNickname_NicknameFound(){
        assertNotNull(egTest.getPlayerByNickname("Paperino"));

    }
    @Test
    @DisplayName("getPlayerByNickname method test - Nickname not found ")
    void getPlayerByNickname_NicknameNotFound() {
        assertNull(egTest.getPlayerByNickname("Gastone"));
    }

    @Test
    @DisplayName("Tests the state of the game ")
    void testGameState(){
        GameState gameState = GameState.LOGIN;
        assertEquals(GameState.LOGIN,gameState);
    }

    @Test
    @DisplayName("-inserire una descrizione bella-")
    void getPlayersNicknamesTest() {
        assertEquals(List.of("Paperino" ,"Topolino") , egTest.getPlayersNicknames());
    }


    @Test
    void getBoardTest() {
        assertNotNull(egTest.getGameBoard());
    }


    @Test
    void getPlayersTest() {
        assertTrue(egTest.getPlayers().contains(p1));
        assertTrue(egTest.getPlayers().contains(p2));
        assertFalse(egTest.getPlayers().contains(p3));
        assertEquals(2,egTest.getNumCurrentPlayers());
    }

    @Test
    void getActivePlayersTest() {
        assertTrue(egTest.getActivePlayers().contains(p1));
        assertTrue(egTest.getActivePlayers().contains(p2));
        assertFalse(egTest.getActivePlayers().contains(p3));
        assertEquals(2,egTest.getNumCurrentActivePlayers());
    }

    @Test
    void isNicknameTaken_True() {
        assertTrue(egTest.isNicknameTaken("Topolino"));
    }

    @Test
    public void isNicknameTaken_False() {
        assertFalse(egTest.isNicknameTaken("antonio"));
    }

    @Test
    @DisplayName("Player getById and getByNickName method binding test")
    void playerGettingIdTest() {
        assertEquals(egTest.getPlayerByID(0),egTest.getPlayerByNickname("Paperino"));
        assertEquals(egTest.getPlayerByID(1), egTest.getPlayerByNickname("Topolino"));
        assertNull(egTest.getPlayerByID(5));

    }

    @Test
    void emptyCloudTest() {
       assertEquals(2 ,egTest.getEmptyClouds().size());
       egTest.getGameBoard().getCloud(0).addStudent(new Student(Color.PINK));
       egTest.getGameBoard().getCloud(0).addStudent(new Student(Color.BLUE));
        egTest.getGameBoard().getCloud(0).addStudent(new Student(Color.RED));
       assertEquals(1 , egTest.getEmptyClouds().size());
       List<Student> students = egTest.getGameBoard().getCloud(0).removeStudents();
       assertEquals(2,egTest.getEmptyClouds().size());
    }

    @Test
    void initializationTest() {
        assertNotNull(p1.getDashboard());
        assertNotNull(p2.getDashboard());
        assertNull(p3.getDashboard());
        assertNotNull(egTest.getGameBoard());
    }




    /*
    @Test
    @DisplayName("Player clockwise rotation test")
    void simulationTurn() throws emptyDecktException, noMoreStudentsException, noStudentException {
        egTest.setCurrentPLayer(egTest.getActivePlayers().get(0));
        assertEquals("Paperino",egTest.getCurrentPlayer().getName());
        for(Cloud c : egTest.getGameBoard().getClouds()) {
            for (int i = 0; i < 3; i++) {
                c.addStudent(egTest.getGameBoard().getSack().drawStudent());
            }
        }
        //Servono dei metodi per assicurarci che ci sia a priori lo studente che poi viene scelto
        egTest.getGameBoard().getIslands().get(5).addStudent(egTest.getCurrentPlayer().takeStudent(Color.RED));
        egTest.getCurrentPlayer().getDashboard().addStudent(egTest.getCurrentPlayer().takeStudent(Color.PINK);
        egTest.getGameBoard().getIslands().get(3).addStudent(egTest.getCurrentPlayer().takeStudent(Color.BLUE));







    }
*/


}
