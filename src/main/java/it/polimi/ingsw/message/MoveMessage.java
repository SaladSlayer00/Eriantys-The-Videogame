package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;

public class MoveMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final Color color;

    public MoveMessage(String nickname, Color color, MessageType messageType) {
        super(nickname, MessageType.MOVE);
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
}
