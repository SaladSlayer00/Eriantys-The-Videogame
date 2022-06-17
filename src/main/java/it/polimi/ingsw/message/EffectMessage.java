package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.ExpertDeck;

public class EffectMessage extends Message{

    private static final long serialVersionUID = -3704504226997118508L;
    private final ExpertDeck name;
    int index;

    public EffectMessage(String nickname, int island) {
        super(nickname,
                MessageType.ENABLE_EFFECT);
        this.name = ExpertDeck.HERALD;
        index = island;
    }


    public String toString() {
        return "PickCloudMessage{" +
                "nickname=" + getNickname() +
                ", Island=" + this.getIndex() +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public ExpertDeck getName(){
        return this.name;
    }

}
