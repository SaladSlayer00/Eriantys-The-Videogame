package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.enums.Mage;

/**
 * Class used to send expert card update messages
 */
public class ExpertMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final ExpertDeck card;

    public ExpertMessage(String nickname,ExpertDeck card) {
        super(nickname, MessageType.USE_EXPERT);
        this.card = card;
    }

    @Override
    public String toString() {
        return "ExpertMessage{" +
                "nickname=" + getNickname() +
                ", Card=" + getCard() +
                '}';
    }

    public ExpertDeck getCard() {
        return this.card;
    }
}
