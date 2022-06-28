package it.polimi.ingsw.message.observation;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.playerBoard.Dashboard;

import java.util.List;
/**
 * Class used to send assistant update messages
 */
public class showAssistantMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private int index;
    public showAssistantMessage(int index) {
        super("server", MessageType.SHOW_ASSISTANT);
        this.index = index;

    }
    public String toString() {
        return "MoveMessage{" +
                "nickname=" + getNickname() +
                ", gameBoard=" + this.index +
                '}';
    }
}
