package it.polimi.ingsw.message;

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
