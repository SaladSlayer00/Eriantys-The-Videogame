package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.exceptions.notEnoughMoneyException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.playerBoard.Dashboard;

/* This card allows the player who summons it to control the professor even if they have the same number
* students of the player who has it in that very moment
* This is quite ambiguous BUT probably the player needs to have THE SAME number of students, not less!!!
 */
public class ProfessorControllerCard extends Character{

    //constructor of the card
    public ProfessorControllerCard(){
        super(2);
    }

    /* auxiliary method used to check if the summoner has the actual same number of students of the
     player who has the professor
     */
    public boolean sameNumberAs(Player summoner, Player professorOwner, Color professorC){
        Dashboard summonerD = summoner.getDashboard();
        Dashboard profOwnD = professorOwner.getDashboard();
        //TODO
        if(){
            return true;
        }
        else
            return false;
    }

    //effect of the card
    public void useEffect(Player summoner, ExpertGame g, Gameboard gb, int index) throws notEnoughMoneyException {
        if(checkMoney(summoner) == false){
            throw new notEnoughMoneyException();
        }
        else{
            //TODO
        }
    }
}
