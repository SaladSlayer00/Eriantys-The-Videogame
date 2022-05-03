package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Assistant;

public class AssistantMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final Assistant assistant;

    public AssistantMessage(String nickname, Assistant assistant) {
        super(nickname, MessageType.INIT_GAMEBOARD);
        this.assistant = assistant;
    }

    public int getIndex(){
        return this.assistant.getNumOrder();
    }
}
