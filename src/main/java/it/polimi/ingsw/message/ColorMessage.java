package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;

/**
 * Class used to send color update messages
 */
public class ColorMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private Color c;

    public ColorMessage(String nickname, Color c) {
        super(nickname,
                MessageType.COLOR_MESSAGE);
        this.c = c;
    }



    public String toString() {
        return "PickCloudMessage{" +
                "nickname=" + getNickname() +
                ", Color=" + this.getColor() +
                '}';
    }

    public Color getColor() {
        return c;
    }
}
