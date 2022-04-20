package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class DashboardTest {

    Dashboard dTest;

    //initial set up of the dashboard
    @BeforeEach
    void startingsetup(){
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
    void StudentsInTheHallTest(){
        dTest.setHallDimension(7);
        Student s = new Student(Color.valueOf("green"));
        dTest.addToHall(s);
    }

    @Test
    void putTower() {
    }

    @Test
    void getTower() {
    }

    @Test
    void setNumTowers() {
    }
}