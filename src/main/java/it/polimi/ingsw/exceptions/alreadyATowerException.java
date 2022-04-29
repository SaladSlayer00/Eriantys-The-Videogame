package it.polimi.ingsw.exceptions;
import it.polimi.ingsw.model.enums.Type;

//exception to handle the moment when a tower is already on an island
public class alreadyATowerException extends Exception{
    public alreadyATowerException(Type tower){
        super("There is already a tower on this island. It's " + tower + "!");
    }
}