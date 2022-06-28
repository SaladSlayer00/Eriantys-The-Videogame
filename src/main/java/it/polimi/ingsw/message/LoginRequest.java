package it.polimi.ingsw.message;

/**
 * Class used to ask the client for the player's nickname
 */
public class LoginRequest extends  Message {
    private static final long serialVersionUID = 1L;

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }
    @Override
    public String toString(){
        return "LoginRequest{" +
                "nickname = " + getNickname() +
                '}';
    }
}
