package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.noStudentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class DashboardTest {

    Dashboard dTest;

    //initial set up of the dashboard
    @BeforeEach
    void startingSetUp(){
        dTest = new Dashboard();
    }


    //this is for the HallDimension methods
    @Test
    @DisplayName("Tests the getter and setter for the dashboard")
    void HallDimensionTest(){
        dTest.setHallDimension(7);
        assertEquals(7, dTest.getHallDimension());
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
        dTest.setHallDimension(7);
        Student s = new Student(Color.valueOf("green"));
        dTest.addToHall(s);
        //should add a way to check the number of students in the hall!!! TODO
        dTest.takeStudent(Color.valueOf("green"));
        //should add a way to check the number of students again!!!
    }

    //this is for the towers
    @Test
    @DisplayName("getter, setter and these things for the towers")
    void towerTest(){
        dTest.setNumTowers(7);
        //TODO
    }
}