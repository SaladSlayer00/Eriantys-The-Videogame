package it.polimi.ingsw.exceptions;

public class InvalidPlayersException extends Exception{
    public InvalidPlayersException() {
        super("Too many players!");
    }

}
