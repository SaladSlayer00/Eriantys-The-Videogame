package it.polimi.ingsw.message;

/**
 * Class used to send win update messages
 */
public class WinMessage extends Message{
    private static final long serialVersionUID = 4516402749630283459L;

    public String getWinnerNickname() {
        return winnerNickname;
    }

    private final String winnerNickname;

    public WinMessage(String winnerNickname) {
        super("server", MessageType.WIN_FX);
        this.winnerNickname = winnerNickname;
    }

    @Override
    public String toString() {
        return "WinMessage{" +
                "nickname=" + getNickname() +
                ", winnerNickname=" + winnerNickname +
                '}';
    }
}
