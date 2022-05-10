package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Mage;

import java.util.List;


public class DeckMessageRequest extends Message{
    //ID thing??
    private List<Mage> availableDecks;

    public DeckMessageRequest(String nickname, List<Mage> gameDecks){
        super(nickname, MessageType.ASK_DECK);
        availableDecks = gameDecks;
    }

    @Override
    public String toString(){
        return "ColorsMessage{" +
                "nickname = " + getNickname() +
                "}";

    }
}
