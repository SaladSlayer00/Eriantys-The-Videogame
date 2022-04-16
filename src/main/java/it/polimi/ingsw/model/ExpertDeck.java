package it.polimi.ingsw.model;

public class ExpertDeck{
/*
    //this card needs four student that have to be drawn and put here at the start of the match
    public class DrawStudentCard extends Character{
        private Student[] students;

        //constructor of the card
        private DrawStudentCard(){
            super(1);
            for(int i = 0; i < 4; i++){
                students[i] = Sack.drawStudent(); //should solve this thing of the static method!!!
            }
        }

        //this is the card's effect: the player can choose one of the four students on this card and place it
        //on the island they want. Then another student must be drawn from the sack and put on the card again
        public void useEffect(){}
    }
*/
    //this card ables the player to control the professor of a color even if they have the same number of
    //students of another player
    public class ProfessorPriorityCard extends Character{

        //constructor of the card
        private ProfessorPriorityCard(){
            super(2);
        }

        //maybe a method that controls if the player's actually got the same number of students of another
        //player may be a good idea?
        private boolean checkStudents(Dashboard player1, Dashboard player2){

        }


       //this is the card's effect
       public void useEffect(){
            //there may be a sort of cycle where the Dashboard of the player who wants to use the card
            //if compared with all the other players' Dashboards so that it is 100% sure that the card can be used
            //wanted to throw an exception but apparently it's not possible???
       }
    }

    //this card calculates the influence on the island chosen from the player who summoned it.
    //The rest of the turn keeps going normally, so it has to be calculated ALSO the influence on the island
    //where Mother Nature ends at the end of the player's round
    public class ImproperInfluenceCard extends Character{

        //constructor of the card
        private ImproperInfluenceCard(){
            super(3);
        }
    }

}
