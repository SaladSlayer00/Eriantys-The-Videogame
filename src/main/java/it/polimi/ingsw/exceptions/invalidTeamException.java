package it.polimi.ingsw.exceptions;

public class invalidTeamException extends Exception {
   public invalidTeamException() {
        super("There's no such team");
    }
}
