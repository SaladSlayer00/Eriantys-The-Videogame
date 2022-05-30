package it.polimi.ingsw.model.test;

import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.exceptions.lowerLimitException;
import it.polimi.ingsw.model.GameFactory;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GameFactoryTest {
    private GameFactory gameFactoryTest;

    @BeforeEach
    void startingSetup(){
        this.gameFactoryTest = new GameFactory();
        this.gameFactoryTest.setType(modeEnum.EASY);
    }

    @Test
    void getTypeTest(){
        assertEquals(modeEnum.EASY,gameFactoryTest.getType());
    }
    @Test
    void getModeTest() throws invalidNumberException {
        assertNotNull(gameFactoryTest.getMode(gameFactoryTest.getType(),2));
        assertThrows(invalidNumberException.class,()->gameFactoryTest.getMode(gameFactoryTest.getType(),5));


    }

}
