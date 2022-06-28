package it.polimi.ingsw.message;

/**
 * Class used to ask the player for the player number input
 */
public class PlayerNumberRequest extends Message{

    private static final long serialVersionUID = -2155556142315548857L;
    public PlayerNumberRequest() {
        super("FirstPlayer", MessageType.PLAYERNUMBER_REQUEST);
    }
    @Override
    public String toString() {
        return "PlayerNumberRequest{" +
                "nickname=" + getNickname() +
                '}';
    }
}
