package it.polimi.ingsw.exceptions;

//exception to handle the moment when no tower is found on an island
public class noTowerException extends Exception{
    public noTowerException(){
        super("There isn't any tower on this island");
    }
}
