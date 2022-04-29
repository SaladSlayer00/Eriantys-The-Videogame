package it.polimi.ingsw.exceptions;

public class impossibleBanException extends Exception{
    public impossibleBanException(){
        super("All the bans are in use right now! You can't cast one, sorry!");
    }
}
