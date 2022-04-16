package it.polimi.ingsw.model;

public class invalidTeamException extends Exception {
    invalidTeamException() {
        super("There's no such team");
    }
}
