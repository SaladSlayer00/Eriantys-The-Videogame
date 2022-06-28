package it.polimi.ingsw.message.observation;

import com.sun.scenario.effect.impl.prism.PrImage;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * Class used to send gameboard update messages
 */
public class BoardMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private final Gameboard board;
    private final List<Dashboard> dashboards;
    private final List<Player> players;

    public BoardMessage(Gameboard board, List<Dashboard> dashboards,List<Player> players) {
        super("server", MessageType.BOARD);
        this.board = board;
        this.dashboards = dashboards;
        this.players = players;

    }
    public String toString() {
        return "MoveMessage{" +
                "nickname=" + getNickname() +
                ", gameBoard=" + this.board +
                ", dashboards=" + this.dashboards +
                '}';
    }

    public Gameboard getBoard() {
        return board;
    }

    public List<Dashboard> getDashboards() {
        return dashboards;
    }

    public List<Player> getPlayers(){return players; }
}
