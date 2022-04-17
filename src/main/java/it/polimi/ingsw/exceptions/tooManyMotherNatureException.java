package it.polimi.ingsw.exceptions;
//exception that handle the case in which we got more than one boolean of mother nature true
public class tooManyMotherNatureException extends Exception {
    public tooManyMotherNatureException() {
        super("Something's wrong...There is more than one Mother Nature on the board!");
    }
}


