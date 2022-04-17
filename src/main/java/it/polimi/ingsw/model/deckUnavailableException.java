package it.polimi.ingsw.model;

public class deckUnavailableException extends Exception {
    deckUnavailableException() {
        super("Deck already assigned!");
    }

}
