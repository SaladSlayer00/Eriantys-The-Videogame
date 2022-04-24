package it.polimi.ingsw.exceptions;

public class notPresentStudentException extends Exception{
    public notPresentStudentException(){
        super("It doesn't exist any student of this type on the card! You can't do the swap, sorry!");
    }
}
