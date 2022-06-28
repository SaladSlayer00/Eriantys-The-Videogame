package it.polimi.ingsw.message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.enums.Color;

/**
 * Class used to send mother moves update messages
 */
public class MoveMotherMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private final int moves;
    private final Assistant chosenAssistant;

    public MoveMotherMessage(String nickname , int moves, Assistant chosenAssistant){
        super(nickname,MessageType.MOVE_MOTHER);
        this.moves = moves;
        this.chosenAssistant = chosenAssistant;
    }

    public String toString() {
        return "MoveMotherMessage{" +
                "nickname=" + getNickname() +
                "moves=" + this.getMoves() +'}';
    }

    public int getMoves(){
        return this.moves;
    }

    public Assistant getChosenAssistant(){
        return this.chosenAssistant;
    }

}