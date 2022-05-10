package it.polimi.ingsw.message;

import it.polimi.ingsw.model.GameFactory;
import it.polimi.ingsw.model.enums.modeEnum;

import java.util.List;

public class GameModeRequest extends Message{
    //dunno if it needs the part of the ID??
    private List<modeEnum> modes;

    public GameModeRequest(String nickname, List<modeEnum> modes){
        super(nickname, MessageType.GAMEMODE_REQUEST);
        this.modes = modes;
    }

    public List<modeEnum> getModes(){
        return modes;
    }

    @Override
    public String toString(){
        return "GameModeRequest{" +
                "nickname = " + getNickname() +
                "modes = " + getModes() +
                "}";
    }
}
