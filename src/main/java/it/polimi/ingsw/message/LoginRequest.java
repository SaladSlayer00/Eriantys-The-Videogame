package it.polimi.ingsw.message;

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
