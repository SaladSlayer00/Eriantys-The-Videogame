package it.polimi.ingsw.model.expertDeck;

/* This card allows the player who summons it to control the professor even if they have the same number
* students of the player who has it in that very moment
* This is quite ambiguous BUT probably the player needs to have THE SAME number of students, not less!!!
 */
public class ProfessorControllerCard extends Character{

    //constructor of the card
    public ProfessorControllerCard(){
        super(2);
    }
}
