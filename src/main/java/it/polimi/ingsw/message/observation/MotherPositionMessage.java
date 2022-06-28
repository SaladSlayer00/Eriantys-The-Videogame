package it.polimi.ingsw.message.observation;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Class used to send mother nature update messages
 */
public class MotherPositionMessage extends Message {
    private static final long serialVersionUID = -3704504226997118508L;
    private final int mother;

    public MotherPositionMessage(String nickname,int mother) {
        super(nickname, MessageType.MOTHER_POSITION);
        this.mother = mother;
    }

    @Override
    public String toString() {
        return "MotherPositionMessage{" +
                "nickname=" + getNickname() +
                ", Mother=" + getMother() +
                '}';
    }

    public int getMother() {
        return this.mother;
    }
}
