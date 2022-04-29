package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.model.enums.modeEnum;

public class GameFactory {
    private modeEnum type;
    public GameFactory(){};

    public void setType(modeEnum type) {
        this.type = type;
    }

    public modeEnum getType() {
        return type;
    }

    //eccezione nel factory non necessaria, si fa nel controller al massimo
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
                    retval = new ExpertGame(players);
                    break;
            }
            return retval;
        }
    }
}

