package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.modeEnum;

public class GameModeReply extends Message{
    private static final long serialVersionUID = -4419241297635925047L; //boh
    private final modeEnum mode;

    public GameModeReply(String nickname, modeEnum mode) {
        super(nickname, MessageType.GAMEMODE_REPLY);
        this.mode = mode;
    }

    public modeEnum getGameMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "GameModeReply{" +
                "nickname=" + getNickname() +
                ", Mode=" + getGameMode() +
                '}';
    }
}
