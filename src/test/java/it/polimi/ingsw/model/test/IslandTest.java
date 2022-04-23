package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.board.Island;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


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
    }

    @Test
    void getInfluence() {
    }

    @Test
    void setInfluence() {
    }

    @Test
    void setMotherNature() {
    }

    @Test
    void getDimension() {
    }

    @Test
    void isMotherNature() {
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

    @Test
    void addMother() {
    }

    @Test
    void removeMother() {
    }

    @Test
    void changeDimension() {
    }
}