package it.polimi.ingsw.message;

public class PlayerNumberRequest extends Message{

    //ancora no giocatori inizializzati
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
