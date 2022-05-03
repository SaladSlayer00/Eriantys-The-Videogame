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

/* we have two choice here:
* - ONE -> we create an arraylist of proper islands
* - TWO -> we create an arraylist that contains the index of the islands. maybe it's easier in this way... (?)
*          THIS METHOD SEEMS MORE LOGICAL TO ME TBH
 */
public class InfluenceBansCard extends Character{

    private int banPaws = 4;
    private ArrayList<Integer> bannedIslands;
    private Gameboard g;

    //constructor
    public InfluenceBansCard(){
        super(2);
    }

    /* is this useful??
    * getter for the gameboard
    * public void getGameboard(Gameboard g){
    *    this.g = g;
    * }
    */

    /* this is the method that put the ban on the island.
    * the island chosen by the player is added to an arraylist that contains all the banned islands
    * (of course the number of banned islands must go from 0 to 4!)
    * the real talk here is: how do we handle this thing during the game???
    * the controller may check if an island is banned before calculate the influence???
     */
    public void banIsland(Player p, int index) throws impossibleBanException {
        if(banPaws == 0)
            throw new impossibleBanException();
        else{
            bannedIslands.add(index);
            banPaws--;
        }
    }

    //method that check if an island is banned
    public boolean checkBan(Island i){
        if(bannedIslands.contains(i.getIndex()))
            return true;
        else return false;
    }
}
