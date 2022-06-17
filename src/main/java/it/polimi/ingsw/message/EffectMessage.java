package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;

public class EffectMessage extends Message{

    private static final long serialVersionUID = -3704504226997118508L;
    private final ExpertDeck name;
    private int index;
    private Color c;

    public EffectMessage(String nickname, ExpertDeck name ,int island) {
        super(nickname,
                MessageType.ENABLE_EFFECT);
        this.name = name;
        index = island;
    }

    public EffectMessage(String nickname, ExpertDeck name ,Color c) {
        super(nickname,
                MessageType.ENABLE_EFFECT);
        this.name = name;
        this.c = c;
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

    public Color getColor() {
        return c;
    }
}
