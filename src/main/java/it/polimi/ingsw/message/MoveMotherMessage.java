package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;

public class MoveMotherMessage extends Message{

    private final int moves;

    public MoveMotherMessage(String nickname, int moves) {
        super(nickname, MessageType.MOVE_MOTHER);
        this.moves = moves;
    }

    public String toString() {
        return "MoveMotherMessage{" +
                "nickname=" + getNickname() +
                "moves=" + this.getMoves() +'}';
    }

    public int getMoves(){
        return this.moves;
    }

}
