package it.polimi.ingsw.message;

/**
 * Class used to send generic messages of various nature to the client,
 * showing the message in text form
 */
public class GenericMessage extends Message{
    private static final long serialVersionUID = 934399396584368694L;

    private final String message;


    public GenericMessage(String message) {
        super("server", MessageType.GENERIC_MESSAGE);
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "nickname=" + getNickname() +
                ", message=" + message +
                '}';
    }
}
