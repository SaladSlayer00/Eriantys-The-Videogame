package it.polimi.ingsw.message;
import it.polimi.ingsw.model.enums.Mage;

/**
 * Class used to send deck update messages
 */
public class DeckMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final Mage deck;

    public DeckMessage(String nickname,Mage mage) {
        super(nickname, MessageType.INIT_DECK);
        this.deck = mage;
    }

    @Override
    public String toString() {
        return "ColorsMessage{" +
                "nickname=" + getNickname() +
                ", Deck=" + getMage() +
                '}';
    }

    public Mage getMage() {
        return this.deck;
    }
}
