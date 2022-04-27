package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.Assistant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class AssistantTest {

    Assistant assistanTest;

    //initialization of the test class
    @BeforeEach
    void startingSetUp(){
        assistanTest = new Assistant(3, 4);
    }

    //testing for the getter of the move
    @Test
    @DisplayName("Tests the assistant's move getter")
    void getMoveTest() {
        assertEquals(4, assistanTest.getMove());
    }

    //testing for the getter of the order
    @Test
    @DisplayName("Tests the assistant's order getter")
    void getNumOrderTest() {
        assertEquals(3, assistanTest.getNumOrder());
    }
}