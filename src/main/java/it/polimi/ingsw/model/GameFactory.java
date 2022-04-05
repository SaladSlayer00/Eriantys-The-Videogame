package it.polimi.ingsw.model;
import it.polimi.ingsw.model.modeEnum;

public class GameFactory {
    public GameFactory(){};

    public Mode getMode (modeEnum type) {
        Mode retval = null;
        switch (type) {
            case EASY:
                retval = new EasyGame();
                break;
            case EXPERT:
                retval = new ExpertGame();
                break;
        }
        return retval;
    }
}

