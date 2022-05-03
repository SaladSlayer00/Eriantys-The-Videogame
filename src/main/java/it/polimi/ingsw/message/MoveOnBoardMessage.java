package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;

public class MoveOnBoardMessage extends MoveMessage{

    private final Color row;

    public MoveOnBoardMessage(String nickname, Color color, Color row) {
        super(nickname, color).super(MessageType.MOVE_ON_BOARD);
        this.row = row;
    }

    public String toString() {
        return "MoveOnIslandMessage{" +
                "nickname=" + getNickname() +
                ", Color of Student=" + this.getColor() +
                "color of Row=" + this.getColor() +'}';
    }

    public Color getRow(){
        return this.row;
    }

}
