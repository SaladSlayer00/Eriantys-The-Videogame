package it.polimi.ingsw.model;
import it.polimi.ingsw.model.modeEnum;

public class GameFactory {
    public GameFactory(){};

    public Mode getMode (modeEnum type, int players) throws invalidNumberException{
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
                    retval = new ExpertGame(players);
                    break;
            }
            return retval;
        }
    }
}

