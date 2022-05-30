package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.emptyDecktException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.tooManyMotherNatureException;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
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
    void InitializeIslandTest() throws noMoreStudentsException {
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
    }


}
