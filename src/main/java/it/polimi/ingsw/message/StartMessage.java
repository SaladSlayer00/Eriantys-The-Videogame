package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Mage;

public class StartMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final String answer;

    public StartMessage(String nickname, String answer) {
        super(nickname, MessageType.INIT_GAMEBOARD);
        this.answer = answer;
    }

    public String getAnswer(){
        return this.answer;
    }

}
