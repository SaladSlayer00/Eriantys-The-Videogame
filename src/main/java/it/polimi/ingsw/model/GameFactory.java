package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.model.enums.modeEnum;

import java.io.Serializable;

/**
 * class that allows for the creation of the correct game instance
 * according to the mode chosen by the player
 */


public class GameFactory implements Serializable {
    private static final long serialVersionUID = -3704504226997118508L;
    private modeEnum type;
    public GameFactory(){};

    public void setType(modeEnum type) {
        this.type = type;
    }

    public modeEnum getType() {
        return type;
    }

    /**
     * actual implementation of factory method
     * @param type mode chosen by player
     * @param players number of players
     * @return correct value according to the argumets
     * @throws invalidNumberException if the number is not allowed
     */
    public Mode getMode (modeEnum type, int players) throws invalidNumberException {
        Mode retval = null;
        if(players > 4 || players < 2){
            throw new invalidNumberException();
        }
        else {
            switch (type) {
                case EASY:
                    retval = new EasyGame(players);
                    break;
                case EXPERT:
                    retval = new EasyGame(players);
                    break;
            }
            return retval;
        }
    }
}

