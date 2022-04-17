package it.polimi.ingsw.exceptions;
//exception that handle the emptiness of the sack. Should this method call for the end of the game???
public class noMoreStudentsException extends Exception{
    public noMoreStudentsException(){super("There are no more students in the sack!");}
}
