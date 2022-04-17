package it.polimi.ingsw.exceptions;

public class emptyDecktException extends Exception{
    public emptyDecktException(){
        super("Your deck is empty. You cannot draw any more cards");
    }
}