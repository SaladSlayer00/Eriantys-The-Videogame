package it.polimi.ingsw.message;

public class BoardMessage extends Message{
    private static final long serialVersionUID = -8014575220371739730L;

    public BoardMessage(ReducedSpace[][] board) {
        super(Game.SERVER_NICKNAME, MessageType.BOARD);
        this.board = board;

    }
}
