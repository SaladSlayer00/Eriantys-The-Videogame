package it.polimi.ingsw.message;
import it.polimi.ingsw.model.EasyGame;
import java.util.List;

/**
 * Class used to send lobby update messages, shows the players the ones connected at the moment
 */
public class LobbyMessage extends Message {
    private static final long serialVersionUID = -6886305903361404798L;
    private final List<String> nicknameList;
    private final int maxPlayers;

    public LobbyMessage(List<String> nicknameList , int maxPlayers){
        super(EasyGame.SERVER_NICKNAME,MessageType.LOBBY);
        this.nicknameList = nicknameList;
        this.maxPlayers = maxPlayers;
    }

    public List<String> getNicknameList(){
        return nicknameList;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    @Override
    public String toString(){
        return "LobbyMessage{" +
                "nickname=" + getNickname() +
                ", nicknameList=" + nicknameList +
                ", numPlayers=" + maxPlayers +
                '}';
    }
}
