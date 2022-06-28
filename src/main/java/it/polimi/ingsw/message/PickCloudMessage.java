package it.polimi.ingsw.message;

/**
 * Class used to send chosen cloud update messages
 */
public class PickCloudMessage extends Message{

    private static final long serialVersionUID = -3704504226997118508L;
    private final int index;

    public PickCloudMessage(String nickname, int index) {
        super(nickname,
                MessageType.PICK_CLOUD);
        this.index = index;
    }

    public String toString() {
        return "PickCloudMessage{" +
                "nickname=" + getNickname() +
                ", Cloud=" + this.getCloudIndex() +
                '}';
    }

    public int getCloudIndex(){
        return this.index;
    }
}
