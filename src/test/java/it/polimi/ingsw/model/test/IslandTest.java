package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.alreadyATowerException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Type;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import it.polimi.ingsw.model.Player;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;


class IslandTest {

    Island islandTest;
    Player userTest;

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
    void influenceTest() {
        islandTest.setInfluence(3);
        assertEquals(3,islandTest.getInfluence());
    }

    @Test
    void calculateInfluence() {
       userTest = new Player("user",0);
        List<Color> pawn = new ArrayList<>(Arrays.asList(Color.RED,Color.RED,Color.RED,Color.BLUE,Color.BLUE));
        for(Color c :pawn){
            islandTest.addStudentOnIsland(c,new Student(c));
        }
        //Calcoliamo l'influenza avendo solamente un professore (quello rosso)
        userTest.getProfessors().add(Color.RED);
        assertEquals(3,islandTest.calculateInfluence(userTest));
        //Calcoliamo l'inflenza avendo entrambi  professori (rosso e blu)
        userTest.getProfessors().add(Color.BLUE);
        assertEquals(5,islandTest.calculateInfluence(userTest));


    }
/*
    @Test
    void getInfluence() {}
    @Test
    void setInfluence() {}
*/
    @Test
    void StudentsTest() {
        Student s1 = new Student(Color.RED);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.YELLOW);
        Student s4 = new Student(Color.BLUE);
        Student s5 = new Student(Color.PINK);
        Map<Color, ArrayList<Student>> students = new HashMap<>();
        students.put(s1.getColor(),new ArrayList<>());
        students.get(s1.getColor()).add(s1);
        students.put(s2.getColor(),new ArrayList<>());
        students.get(s2.getColor()).add(s2);
        students.put(s3.getColor(),new ArrayList<>());
        students.get(s3.getColor()).add(s3);
        students.put(s4.getColor(),new ArrayList<>());
        students.get(s4.getColor()).add(s4);
        students.put(s5.getColor(),new ArrayList<>());
        students.get(s5.getColor()).add(s5);

        islandTest.addStudentOnIsland(s1.getColor(),s1);
        islandTest.addStudentOnIsland(s2.getColor(),s2);
        islandTest.addStudentOnIsland(s3.getColor(),s3);
        islandTest.addStudentOnIsland(s4.getColor(),s4);
        islandTest.addStudentOnIsland(s5.getColor(),s5);

        assertEquals(students,islandTest.getStudents());

    }
    /*
    @Test
    void getStudents() {}
    @Test
    void addStudentOnIsland() {}
    @Test
    void getColor() {}
*/


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
    void getDimension() {}
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
    void setMotherNature() {}
    @Test
    void addMother() {}
    @Test
    void removeMother() {}
    @Test
    void isMotherNature() {}
 */
    @Test
    void towerTest() throws noTowerException, alreadyATowerException {
        assertFalse(islandTest.getTower());
        islandTest.addTower(Type.WHITE);
        assertEquals(Type.WHITE,islandTest.getTeam());
        assertTrue(islandTest.getTower());
        islandTest.setTower(Type.BLACK);
        assertTrue(islandTest.getTower());

    }
    /*
    @Test
    void addTower() {}
     */

}


