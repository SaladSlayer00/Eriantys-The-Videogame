package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;

public class MoveMotherMessage extends Message{

    private final int index;

    public MoveMotherMessage(String nickname, Color color, int index) {
        super(nickname, MessageType.MOVE_MOTHER);
        this.index = index;
    }

    public String toString() {
        return "MoveOnIslandMessage{" +
                "nickname=" + getNickname() +
                ", Color of Student=" + this.getColor() +
                "index of Island=" + this.getIndex() +'}';
    }

    public int getIndex(){
        return this.index;
    }

}
