package it.polimi.ingsw.message;

import java.io.Serializable;

/**
 * Abstract class that represents the information exchanged between the client and the server
 * contains the sender's nickname and the message type that's determined to call methods and
 * initiate actions in both the client and server side
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 6589184250663958343L;

    private final String nickname;
    private final MessageType messageType;

    public Message(String nickname, MessageType messageType) {
        this.nickname = nickname;
        this.messageType = messageType;
    }

    public String getNickname() {
        return nickname;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nickname=" + nickname +
                ", messageType=" + messageType +
                '}';
    }
}
