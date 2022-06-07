package it.polimi.ingsw.model.expertDeck;

import it.polimi.ingsw.model.enums.ExpertDeck;

/* this card gives two more influence points to the summoner
* many ways to implement this and maybe it would be better to discuss it before just writing
* stuffs and then delete everything because it doesn't work
 */
public class TwoMorePointsCard extends Character{

    private ExpertDeck name = ExpertDeck.KNIGHT;

    //constructor
    public TwoMorePointsCard(){
        super(2);
    }

    public ExpertDeck getName() {
        return name;
    }
}
