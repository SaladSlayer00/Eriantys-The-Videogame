package it.polimi.ingsw.message;

public class DrawMessage extends Message{
    private static final long serialVersionUID = 4516402749630283459L;


    public DrawMessage() {
        super("server", MessageType.WIN_FX);
    }

    @Override
    public String toString() {
        return "DrawMessage{" +
                '}';
    }
}
