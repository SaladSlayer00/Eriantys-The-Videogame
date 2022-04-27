package it.polimi.ingsw.model.test;

/* TODO
* SHOULDN'T WE ADD A SORT OF THING TO CHECK THAT THERE ARE STILL STUDENT OF A CERTAIN COLOR???
* like when we add a student to the sack, there should be a limit of students for a certain color???
* maybe this should be done in the controller???
 */

import it.polimi.ingsw.model.board.Sack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class SackTest {

    Sack sack = new Sack();

    //this is the proper initialization of the class sack
    @BeforeEach
    void startingSetUp(){
        sack.initializeSack();
    }

    //this is the method that put a student inside the sack
    @Test
    void putStudent() {
    }

    @Test
    void drawStudent() {
    }

    @Test
    void getNum() {
    }

    @Test
    void getColors() {
    }
}