package it.polimi.ingsw.model.test;

/* TODO
* SHOULDN'T WE ADD A SORT OF THING TO CHECK THAT THERE ARE STILL STUDENT OF A CERTAIN COLOR???
* like when we add a student to the sack, there should be a limit of students for a certain color???
* maybe this should be done in the controller???
 */

import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Sack;
import it.polimi.ingsw.model.enums.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Student s = new Student(Color.BLUE);
        sack.putStudent(s);
        assertTrue(sack.getStudents().contains(s));

    }

    @Test
    void drawStudent() {
        //Sappiamo che il numero di studenti per ogni colore è 26
        Student s = sack.drawStudent();
        int numberOfPawn = 0;
        for(Student student: sack.getStudents()){
                if(s.getColor().equals(student.getColor()))
                    numberOfPawn++;
        }
        assertEquals(25,numberOfPawn);
        assertFalse(sack.getStudents().contains(s));
    }

    @Test
    void getNum() {
        assertEquals(130 , sack.getNum());

    }

    /*
    @Test
    void getColors() {

    }
    */

}