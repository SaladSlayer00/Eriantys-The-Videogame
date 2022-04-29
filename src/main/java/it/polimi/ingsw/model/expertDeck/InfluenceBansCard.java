package it.polimi.ingsw.model.expertDeck;


import it.polimi.ingsw.exceptions.impossibleBanException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;

import java.util.ArrayList;

/* this card has four ban paws on it
* when a player summon the card they can put one of these paw on an island of their choice
* when Mother Nature ends her journey on that island, the paw is put again on the card and
* the influence IT IS NOT CALCULATED
 */
public class InfluenceBansCard extends Character{

    private int banPaws = 4;
    private ArrayList<Island> bannedIslands;
    private Gameboard g;

    //constructor
    public InfluenceBansCard(){
        super(2);
    }

    //getter for the gameboard
    public void getGameboard(Gameboard g){
        this.g = g;
    }

    public void effect(Player p, int index) throws impossibleBanException {
        if(banPaws == 0)
            throw new impossibleBanException();
        else{
            bannedIslands.add(g.getIslands().get(index));
            banPaws--;
            //TODO
            //how the heck should be brought on from this point???
        }
    }
}
