package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Assistant;

/**
 * Class used to send assistant update messages
 */
public class AssistantMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final Assistant assistant;

    public AssistantMessage(String nickname, Assistant assistant) {
        super(nickname, MessageType.DRAW_ASSISTANT);
        this.assistant = assistant;
    }

    public String toString() {
        return "AssistantMessage{" +
                "nickname=" + getNickname() +
                ", Assistant Index=" + this.getIndex() +
                '}';
    }

    public int getIndex(){
        return this.assistant.getNumOrder();
    }

    public Assistant getAssistant(){
        return this.assistant;
    }
}
