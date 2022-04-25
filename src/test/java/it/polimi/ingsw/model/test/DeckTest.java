package it.polimi.ingsw.model.test;
import it.polimi.ingsw.model.Mage;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import it.polimi.ingsw.exceptions.emptyDecktException;
import it.polimi.ingsw.model.Assistant;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    Deck dTest;
    Mage mTest;

    //initial set up of the deck
    @BeforeEach
    void startingSetup() {
        dTest = new Deck(mTest);
    }

    //This is for the method that returns the mage
    @Test
    @DisplayName("Test the methods for the mage")
    void getMageTest() {
        assertEquals(mTest, dTest.getMage());
    }

    //This is for the method that returns the number of cards in the deck
    //and for the method that allows you to draw a card from the deck
    @Test
    @DisplayName("Test the getter of the number of cards and the action of drawing a card from the deck")
    void drawTest() throws emptyDecktException {
        Assistant a;
        for (int i = 0; i < 11; i++) {
            a = dTest.draw(i);
            assertNotNull(a);
            assertEquals(11 - (i + 1), dTest.getNumCards());
        }
        assertThrows(emptyDecktException.class,()->dTest.draw(7));

    }
}
//NOTE :
//Do we have to test also the exceptions that can be raised ?
/*
void drawTest() throws emptyDecktException {
        int index1  = 3;
        int index2 = 6;
        assertEquals(11 , dTest.getNumCards());
        Assistant a1 = dTest.draw(index1);
        Assistant a2 = dTest.draw(index2);
        assertNotNull(a1);
        assertNotNull(a2);
        assertEquals(9,dTest.getNumCards());

    }
 */

