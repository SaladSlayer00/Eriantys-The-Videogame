package it.polimi.ingsw.model.test;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.playerBoard.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.exceptions.*;


/*
  Class EasyGame Test EasyGame Class
 */


public class EasyGameTest {
    EasyGame egTest;

    @BeforeEach
    void startingSetup() throws noMoreStudentsException , maxSizeException {
        egTest = new EasyGame(2);
        egTest.initializePlayer(new Player("Paperiono",0));
        egTest.initializePlayer(new Player("Topolino",1));
        egTest.initializeGameboard();
        egTest.initializeDashboards();
    }
    /*
      Method playerGettingByIdTest aims to verify the correctness of the player information getters,
      like the nickname and the ID ones.
     */
    @Test
    @DisplayName("Player getById and getByNickName method binding test")
    void playerGettingIdTest() {
        assertEquals(egTest.getPlayerByID(0),egTest.getPlayerByNickname("Paperino"));
        assertEquals(egTest.getPlayerByID(1), egTest.getPlayerByNickname("Topolino"));
        assertNull(egTest.getPlayerByID(5));
        assertNull(egTest.getPlayerByNickname("Gastone"));
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
