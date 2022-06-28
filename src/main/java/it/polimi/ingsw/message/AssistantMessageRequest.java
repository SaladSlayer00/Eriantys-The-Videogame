package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Assistant;

import java.util.List;

/**
 * Class used to ask the player for an assistant input
 */
public class AssistantMessageRequest extends Message{
    private List<Assistant> availableAssistants;

    public AssistantMessageRequest(String nickname, List<Assistant> availableAssistants){
        super(nickname, MessageType.ASSISTANT_REQUEST);
        this.availableAssistants = availableAssistants;
    }

    public List<Assistant> getAssistants(){
        return availableAssistants;
    }

    @Override
    public String toString(){
        return "AssistantMessage{" +
                "nickname = " + getNickname() +
                "availableAssistants = " + getAssistants() +
                "}";
    }
}
