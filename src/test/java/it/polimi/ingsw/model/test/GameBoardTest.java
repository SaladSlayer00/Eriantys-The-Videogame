package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private Gameboard gameboardTest;
    @BeforeEach
    void startingSetup() {
        this.gameboardTest = new Gameboard(2);
    }
    @Test
    void getSackTest() {
        assertNotNull(gameboardTest.getSack());

    }
    @Test
    void initializeIslandTest() throws noMoreStudentsException {
        gameboardTest.initializeIslands();
        boolean motherNature = false;
        for(Island i : gameboardTest.getIslands()){
            if(i.isMotherNature()){
                motherNature = true;
                break;
            }
        }
        assertTrue(motherNature);
        for(int i = 0 ; i < 12 ; i++){
            assertNotNull(gameboardTest.getIslands().get(i));
        }
        assertEquals(12,gameboardTest.getIslands().size());
        gameboardTest.setMotherNature(3);
        assertEquals(3,gameboardTest.getMotherNature());

    }

    @Test
    void cloudTest() {
        assertNotNull(gameboardTest.getClouds());
        assertEquals(0,gameboardTest.getClouds().size());
        gameboardTest.createClouds();
        assertEquals(2,gameboardTest.getClouds().size());
        for(int i = 0; i <2 ; i++){
            assertNotNull(gameboardTest.getCloud(i));
        }
        Cloud c1 = gameboardTest.chooseCloud(0);
        assertNotNull(c1);
        assertFalse(gameboardTest.getClouds().contains(c1));
        assertEquals(1,gameboardTest.getClouds().size());
        c1 = gameboardTest.chooseCloud(0);
        assertNotNull(c1);
        assertFalse(gameboardTest.getClouds().contains(c1));
        assertEquals(0,gameboardTest.getClouds().size());

    }

    @Test
    void placeStudentTest() throws noMoreStudentsException {
        gameboardTest.initializeIslands();
        Student s = new Student(Color.RED);
        gameboardTest.placeStudent(s.getColor(),s ,4);
        assertTrue(gameboardTest.getIslands().get(4).getStudents().get(s.getColor()).contains(s));
    }

    @Test
    void mergeIslandsTest() throws noMoreStudentsException, noTowerException, alreadyATowerException {
        Student s1 = new Student(Color.RED);
        Student s2 = new Student(Color.GREEN);
        gameboardTest.initializeIslands();
        gameboardTest.placeStudent(s1.getColor(),s1 ,5);
        gameboardTest.placeStudent(s2.getColor(),s2, 6);
        gameboardTest.getIslands().get(5).addTower(Type.BLACK);
        gameboardTest.getIslands().get(6).addTower(Type.BLACK);;
        assertEquals(12,gameboardTest.getIslands().size());
        gameboardTest.mergeIslands(gameboardTest.getIslands().get(5));
        assertEquals(11,gameboardTest.getIslands().size());
        assertTrue(gameboardTest.getIslands().get(5).getStudents().get(s1.getColor()).contains(s1));
        assertTrue(gameboardTest.getIslands().get(5).getStudents().get(s2.getColor()).contains(s2));
        assertFalse(gameboardTest.getIslands().get(6).getStudents().get(s2.getColor()).contains(s2));

        //eccezioni
        assertThrows(alreadyATowerException.class,()->gameboardTest.getIslands().get(5).addTower(Type.BLACK));


    }
    @Test
    void professorTest(){
        assertEquals(Color.RED , gameboardTest.getProfessors().get(0).getColor());
        assertEquals(Color.BLUE , gameboardTest.getProfessors().get(1).getColor());
        assertEquals(Color.GREEN, gameboardTest.getProfessors().get(2).getColor());
        assertEquals(Color.PINK, gameboardTest.getProfessors().get(3).getColor());
        assertEquals(Color.YELLOW , gameboardTest.getProfessors().get(4).getColor());

        gameboardTest.removeProfessor(Color.RED);
        assertEquals(4,gameboardTest.getProfessors().size());
        gameboardTest.addProfessor(Color.RED);
        assertEquals(5,gameboardTest.getProfessors().size());

    }

    @Test
    void coinTest(){
        assertEquals(20,gameboardTest.getCoins());
        gameboardTest.removeCoin();
        assertEquals(19,gameboardTest.getCoins());
    }


}
