package it.polimi.ingsw.message;

/**
 * Class used to send a ping to the client or server
 */
public class PingMessage extends Message{
    private static final long serialVersionUID = -7019523659587734169L;

    public PingMessage() {
        super(null, MessageType.PING);
    }
}
