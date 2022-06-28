package it.polimi.ingsw.message;

import it.polimi.ingsw.model.enums.Type;

import java.util.List;

/**
 * Class used to ask the player for tower input
 */
public class TowerMessageRequest extends Message{
    List<Type> availableTypes;

    public TowerMessageRequest(String nickname, List<Type> availableTypes){
        super(nickname, MessageType.ASK_TOWER);
        this.availableTypes = availableTypes;
    }

    public List<Type> getTypes(){
        return availableTypes;
    }

    @Override
    public String toString(){
        return "TowerMessage{" +
                "nickname = " + getNickname() +
                "availableTypes = " + getTypes() +
                "}";
    }
}
