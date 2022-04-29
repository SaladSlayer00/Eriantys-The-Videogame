package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.fullTowersException;
import it.polimi.ingsw.exceptions.noStudentException;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class DashboardTest {

    Dashboard d1Test;
    Dashboard d2Test;

    //initial set up of the dashboard
    @BeforeEach
    void startingSetUp(){
        d1Test = new Dashboard(2);
        d2Test = new Dashboard(3);

    }



    //this is for the HallDimension methods
    @Test
    @DisplayName("Tests the getter and setter for the dashboard")
    void HallDimensionTest(){
        assertEquals(7, d1Test.getHallDimension());
        assertEquals(9, d2Test.getHallDimension());
        assertEquals(8 ,d1Test.getNumTowers());
        assertEquals(6,d2Test.getNumTowers());
    }

    /*this is for the methods involved with the hall BUT I don't know if this should be here,
    * maybe testing the row methods is enough
    @Test
    @DisplayName("Tests the method that have to do with the rows and the students in it")
    void hallTest() {
    * }
     */

    //this is for the method that take the students form the hall
    @Test
    @DisplayName("Tests the method that have to do with the hall and the students")
    void StudentsInTheHallTest() throws noStudentException {
        //adding a student to the hall
        Student s = new Student(Color.valueOf("green"));
        int l1 = d1Test.getHall().size();
        int l2 = d1Test.getHall().size();
        d1Test.addToHall(s);
        d2Test.addToHall(s);
        assertTrue(d1Test.getHall().contains(s));
        assertTrue(d2Test.getHall().contains(s));
        assertEquals(l1+1,d1Test.getHall().size());
        assertEquals(l2-1,d2Test.getHall().size());
        //taking a student from the hall
        s = d1Test.takeStudent(Color.GREEN);
        assertEquals(s.getColor(),Color.GREEN);
        assertEquals((l1),d1Test.getHall().size());
        s = d2Test.takeStudent(Color.GREEN);
        assertEquals(s.getColor(),Color.GREEN);
        assertEquals(l2,d2Test.getHall().size());



        /*
        dTest.setHallDimension(7);
        Student s = new Student(Color.valueOf("green"));
        dTest.addToHall(s);
        //should add a way to check the number of students in the hall!!! TODO
        dTest.takeStudent(Color.valueOf("green"));
        //should add a way to check the number of students again!!!

         */
    }

    //this is for the towers
    @Test
    @DisplayName("getter, setter and these things for the towers")
    void towerTest() throws noTowersException, fullTowersException {
        /*
        dTest.setNumTowers(7);
        //TODO
         */
       Type t1 = d1Test.getTower();
       assertEquals(7,d1Test.getNumTowers());
       d1Test.putTower();
       assertEquals(8,d1Test.getNumTowers());

        Type t2 = d2Test.getTower();
        assertEquals(5,d2Test.getNumTowers());
        d2Test.putTower();
        assertEquals(6,d2Test.getNumTowers());

    }

    @Test
    @DisplayName("fullTowerException launch")
    void fullTowerEx() throws fullTowersException {
        assertThrows(fullTowersException.class, () -> {
            d1Test.putTower();
        });

        assertThrows(fullTowersException.class, () -> {
            d2Test.putTower();
        });
    }

    @Test
    @DisplayName("noTowerException launch")
    void noTowerEx() throws noTowersException {
        Type t;
        for(int i = 0; i < 8; i++){
            t = d1Test.getTower();
        }

        for(int i = 0; i < 6; i++){
            t = d2Test.getTower();
        }
        assertThrows(noTowersException.class, () -> {
            d1Test.getTower();
        });

        assertThrows(noTowersException.class, () -> {
            d2Test.getTower();
        });

    }

    @Test
    @DisplayName("getter , setter for the attribute Team ")
    void teamTest(){
        d1Test.setTeam(Type.BLACK);
        d2Test.setTeam(Type.GREY);
        assertEquals(Type.BLACK,d1Test.getTeam());
        assertEquals(Type.GREY,d2Test.getTeam());
    }
}