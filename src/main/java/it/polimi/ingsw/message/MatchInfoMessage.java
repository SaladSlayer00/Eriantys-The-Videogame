package it.polimi.ingsw.message;



    public class MatchInfoMessage extends Message{
        //ID?
        int chosen;
        int actual;

        public MatchInfoMessage(int chosen, int actual){
            super("all",MessageType.MATCH_INFO);
            this.chosen = chosen;
            this.actual = actual;
        }


        @Override
        public String toString(){
            return "MatchInfoMessage{" +
                    "chosen = " + this.chosen +
                    "actual =" + this.actual +
                    "}";
        }
    }

