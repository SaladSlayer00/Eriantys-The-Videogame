package it.polimi.ingsw.message;

/**
 * Class used to send player number update messages
 */
public class PlayerNumberReply extends Message{

        private static final long serialVersionUID = -4419241297635925047L; //boh
        private final int playerNumber;

        public PlayerNumberReply(String nickname, int playerNumber) {
            super(nickname, MessageType.PLAYERNUMBER_REPLY);
            this.playerNumber = playerNumber;
        }

        public int getPlayerNumber() {
            return playerNumber;
        }

        @Override
        public String toString() {
            return "PlayerNumberReply{" +
                    "nickname=" + getNickname() +
                    ", playerNumber=" + playerNumber +
                    '}';
        }
}
