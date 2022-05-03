package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;

public class MoveOnIslandMessage extends MoveMessage{

    private final int index;

    public MoveOnIslandMessage(String nickname, Color color, int index) {
        super(nickname, color);
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
