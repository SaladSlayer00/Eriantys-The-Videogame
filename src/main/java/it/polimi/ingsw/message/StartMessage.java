package it.polimi.ingsw.message;

/**
 * Class used to send the player's answer when asked to start the game
 */
public class StartMessage extends Message{
    private static final long serialVersionUID = -3704504226997118509L;
    private final String answer;

    public StartMessage(String nickname, String answer) {
        super(nickname, MessageType.INIT_GAMEBOARD);
        this.answer = answer;
    }

    public String toString() {
        return "StartMessage{" +
                "nickname=" + getNickname() +
                ", Answer=" + this.answer +
                '}';
    }

    public String getAnswer(){
        return this.answer;
    }

}
