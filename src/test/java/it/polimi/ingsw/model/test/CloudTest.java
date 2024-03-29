package it.polimi.ingsw.model.test;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.board.Cloud;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import it.polimi.ingsw.model.Student;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;


class CloudTest {
    private Cloud c1Test;
    private Cloud c2Test;
    //initial setup
    @BeforeEach
    void startingSetup(){
        c1Test = new Cloud(2,0);
        c2Test = new Cloud(3,0);

    }
    @Test
    @DisplayName("Tests the initialization of the clouds")
    void initializationTest(){
        assertEquals(3,c1Test.getDimension());
        assertEquals(4,c2Test.getDimension());
        assertEquals(0,c1Test.getIndex());
        assertEquals(0,c1Test.getIndex());
    }
    //This is for the method that adds students and
    //for the method that removes students
    @Test
    @DisplayName("Tests the adder of the students")
    void addStudentTest() {

        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.YELLOW);
        Student s4 = new Student(Color.RED);
        Student s5 = new Student(Color.PINK);

        ArrayList<Student> s1Test = new ArrayList<Student>() {{
            add(s1);
            add(s2);
            add(s3);}};
        ArrayList<Student> s2Test = new ArrayList<Student>() {{
            add(s2);
            add(s5);
            add(s3);
            add(s4);}};

        for(Student s : s1Test){
            c1Test.addStudent(s);
        }
        for(Student s : s2Test){
            c2Test.addStudent(s);
        }
        assertTrue(c1Test.getStudents().containsAll(s1Test));
        assertTrue(c2Test.getStudents().containsAll(s2Test));



    }

    @Test
    @DisplayName("Test the method that removes all the students from the cloud ")
    void removeStudentsTest() {
        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.YELLOW);
        Student s4 = new Student(Color.RED);
        Student s5 = new Student(Color.PINK);

        ArrayList<Student> s1Test = new ArrayList<Student>() {{
            add(s1);
            add(s2);
            add(s3);}};
        ArrayList<Student> s2Test = new ArrayList<Student>() {{
            add(s2);
            add(s5);
            add(s3);
            add(s4);}};

        for(Student s : s1Test){
            c1Test.addStudent(s);
        }
        for(Student s : s2Test){
            c2Test.addStudent(s);
        }
        assertTrue(c1Test.removeStudents().containsAll(s1Test));
        assertTrue(c2Test.removeStudents().containsAll(s2Test));
        assertTrue(c1Test.emptyCloud());
        assertTrue(c2Test.emptyCloud());
    }


    }





