package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


class StudentTest{

    Student studenTest;

    //initialization for the test
    @BeforeEach
    void startingSetUp(){
        studenTest = new Student(Color.valueOf("green"));
    }

    //testing for the one and only method of this class
    @Test
    @DisplayName("Tests for the getter of the color of the student")
    void getColorTest() {
        assertEquals(Color.valueOf("green"), studenTest.getColor());
    }
}