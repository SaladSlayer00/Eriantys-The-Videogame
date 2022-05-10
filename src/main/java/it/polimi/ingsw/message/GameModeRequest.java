package it.polimi.ingsw.message;

import it.polimi.ingsw.model.GameFactory;
import it.polimi.ingsw.model.enums.modeEnum;

public class GameModeRequest extends Message{
    //dunno if it needs the part of the ID??
    private modeEnum mode;

    public GameModeRequest(String nickname, modeEnum mode){
        super(nickname, MessageType.GAMEMODE_REQUEST);
        this.mode = mode;
    }

    public modeEnum getGameMode() {
        return mode;
    }

    @Override
    public String toString(){
        return "GameModeRequest{" +
                "nickname = " + getNickname() +
                ", Mode = " + getGameMode() +
                "}";
    }
}
