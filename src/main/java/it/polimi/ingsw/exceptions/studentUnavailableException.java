package it.polimi.ingsw.exceptions;

public class studentUnavailableException extends Exception{
    public studentUnavailableException(){
        super("This student is not on the card!");
    }
}
