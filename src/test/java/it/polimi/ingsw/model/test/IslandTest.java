package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertFalse;


class IslandTest {

    Island islandTest;

    //initialization for the test
    @BeforeEach
    void startingSetUp(){
        islandTest = new Island(4);
    }

    //this is for the getter of the index
    @Test
    @DisplayName("Tests the getter of the index")
    void getIndexTest(){
        assertEquals(4, islandTest.getIndex());
    }

    @Test
    void addStudent() {
        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.BLUE);
        islandTest.addStudent(s1);
        islandTest.addStudent(s2);
        assertEquals(2,islandTest.getStudents().get(s1.getColor()).size());
    }

    @Test
    void getInfluence() {
    }

    @Test
    void setInfluence() {
    }


    @Test
    void getStudents() {
    }

    @Test
    void addTower() {
    }

    @Test
    void getColor() {
    }

    @Test
    void calculateInfluence() {
    }


    //it tests all the methods related to the island's dimension
    @Test
    void islandDimensionTest() {
        int currentDimension = islandTest.getDimension();
        int newDimension = 2;
        islandTest.changeDimension(newDimension);
        assertEquals(currentDimension +newDimension , islandTest.getDimension());

    }
    /*
    @Test
    void getDimension() {
    }
     */

    //it tests all the methods related to motherNature
    @Test
    void motherNatureTest(){
        assertFalse(islandTest.isMotherNature());
        islandTest.setMotherNature(true);
        assertTrue(islandTest.isMotherNature());
        islandTest.removeMother();
        assertFalse(islandTest.isMotherNature());
        islandTest.addMother();
        assertTrue(islandTest.isMotherNature());
    }
/*
    @Test
    void setMotherNature() {
    }
    @Test
    void addMother() {
    }

    @Test
    void removeMother() {}

    @Test
    void isMotherNature() {
    }


 */

}


