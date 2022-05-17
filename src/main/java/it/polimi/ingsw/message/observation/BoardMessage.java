package it.polimi.ingsw.message.observation;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.board.Gameboard;

public class BoardMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private final Gameboard board;

    public BoardMessage(Gameboard board) {
        super("server", MessageType.BOARD);
        this.board = board;

    }
    public String toString() {
        return "MoveMessage{" +
                "nickname=" + getNickname() +
                ", gameBoard=" + this.board +
                '}';
    }
}
