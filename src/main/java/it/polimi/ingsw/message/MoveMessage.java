package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;

public class MoveMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final Color color;
    private int index = -1;
    private Color row = null;

    public MoveMessage(String nickname, Color color, int index) {
        super(nickname, MessageType.MOVE_ON_ISLAND);
        this.index = index;
        this.color = color;

    }

    public MoveMessage(String nickname, Color color, Color row) {
        super(nickname, MessageType.MOVE_ON_BOARD);
        this.row = row;
        this.color = color;

    }

    public String toString() {
        return "MoveMessage{" +
                "nickname=" + getNickname() +
                ", Color of Student=" + this.getColor() +
                '}';
    }

    public Color getColor(){
        return this.color;
    }

    public int getIndex(){
        return this.index;
    }

    public Color getRow(){
        return this.row;
    }
}
