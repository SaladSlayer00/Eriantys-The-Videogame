package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.alreadyAProfessorException;
import it.polimi.ingsw.exceptions.noProfessorException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.playerBoard.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class RowTest {

    Row rTest;

    //initial set up of the dashboard
    @BeforeEach
    void startingSetUp(){
        rTest = new Row(Color.valueOf("blue"));
    }

    //this is for the color of the row and its getter
    @Test
    @DisplayName("Tests the getter of the color")
    void getNameTest(){
        assertEquals("blue", rTest.getName());
    }

    //this is for the methods that have to do with the professors
    @Test
    @DisplayName("Tests the methods for the professors")
    void professorTest() throws alreadyAProfessorException, noProfessorException {
        rTest.addProfessor();
        assertTrue(rTest.checkProfessor());
        rTest.removeProfessor();
        assertFalse(rTest.checkProfessor());
    }

    //this is for the method that adds students
    @Test
    @DisplayName("Tests the adder of the students")
    void addStudentTest(){
        /* SHOULD FIND A WAY TO CHECK THE NUMBER OF STUDENTS???
        * THIS CLASS IS PRIVATE AND IT HASN'T GOT A SUITABLE WAY TO CONTROL THE LENGTH OF THE ROW!!!
         */
        //TODO
    }

}