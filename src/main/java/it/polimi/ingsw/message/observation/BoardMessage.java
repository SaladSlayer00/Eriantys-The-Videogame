package it.polimi.ingsw.message.observation;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.playerBoard.Dashboard;

import java.util.List;

public class BoardMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private final Gameboard board;
    private final List<Dashboard> dashboards;

    public BoardMessage(Gameboard board, List<Dashboard> dashboards) {
        super("server", MessageType.BOARD);
        this.board = board;
        this.dashboards = dashboards;

    }
    public String toString() {
        return "MoveMessage{" +
                "nickname=" + getNickname() +
                ", gameBoard=" + this.board +
                ", dashboards=" + this.dashboards +
                '}';
    }
}
