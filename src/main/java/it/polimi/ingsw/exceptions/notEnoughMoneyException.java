package it.polimi.ingsw.exceptions;

public class notEnoughMoneyException extends  Exception{
    public notEnoughMoneyException(){
        super("You haven't got enough money for this method");
    }
}
