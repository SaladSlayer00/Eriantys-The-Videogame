package it.polimi.ingsw.message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.enums.Color;
/*
public class MoveMotherMessage extends Message{

    private final int moves;

    public MoveMotherMessage(String nickname, int moves) {
        super(nickname, MessageType.MOVE_MOTHER);
        this.moves = moves;
    }

    public String toString() {
        return "MoveMotherMessage{" +
                "nickname=" + getNickname() +
                "moves=" + this.getMoves() +'}';
    }

    public int getMoves(){
        return this.moves;
    }

}
*/

//Da controllare. Ho pensato che all'input controller servissero informazioni relative al numero di passi
//che deve fare madre natura e all'assistente scelto. In questo modo possiamo controllare che il numero di passi sia
//compatibile con il numero sulla carta assistente
public class MoveMotherMessage extends Message{
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