package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Professor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class ProfessorTest {

    Professor profTest;

    //initialization for the tests
    @BeforeEach
    void startingSetUp(){
        profTest = new Professor(Color.valueOf("blue"));
    }

    //testing the getter for the color
    @Test
    @DisplayName("Tests the professor's color getter")
    void getColorTest() {
        assertEquals(Color.valueOf("blue"), profTest.getColor());
    }
}