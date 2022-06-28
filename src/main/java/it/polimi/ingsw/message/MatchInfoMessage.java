package it.polimi.ingsw.message;

import java.util.List;

/**
 * Class used to send match info update messages with the number of active and chosen players
 */
public class MatchInfoMessage extends Message{
        private static final long serialVersionUID = 1L;
        private  List<String> activePlayers;
        private  String activePlayerNickname;
        int chosen;
        int actual;

        public MatchInfoMessage(String senderNickname , MessageType messageType , List<String> activePlayers , String activePlayerNickname) {
            super(senderNickname ,messageType);
            this.activePlayerNickname = activePlayerNickname;
            this.activePlayers = activePlayers;
        }

        public MatchInfoMessage(int chosen, int actual){
            super("all",MessageType.MATCH_INFO);
            this.chosen = chosen;
            this.actual = actual;
        }

        public String getActivePlayerNickname(){
            return activePlayerNickname;
        }

        public List<String> getActivePlayers(){
            return activePlayers;
        }


        @Override
        public String toString(){
            return "MatchInfoMessage{" +
                    "chosen = " + this.chosen +
                    "actual =" + this.actual +
                    "}";
        }
    }

