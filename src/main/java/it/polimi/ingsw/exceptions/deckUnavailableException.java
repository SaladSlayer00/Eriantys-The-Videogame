package it.polimi.ingsw.exceptions;

public class deckUnavailableException extends Exception {
   public deckUnavailableException() {
        super("Deck already assigned!");
    }

}
