package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ExpertDeck class for the various type of cards of the expert deck
 * @autohors Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */

public enum ExpertDeck {
    TAVERNER("taverner"),
    COOK("cook"),
    HERALD("herald"),
    GAMBLER("gambler"),
    HERBALIST("herbalist"),
    CUSTOMER("customer"),
    JOKER("joker"),
    KNIGHT("knight"),
    SELLER("seller"),
    MUSICIAN("musician"),
    BARBARIAN("barbarian"),
    BANKER("banker"),
    NULL("null");

    //it's the string of the color of the paw
    private String name;
    private static final ArrayList<ExpertDeck> available = new ArrayList<>(Arrays.asList(ExpertDeck.CUSTOMER, ExpertDeck.HERALD, ExpertDeck.BANKER, ExpertDeck.BARBARIAN, ExpertDeck.COOK, ExpertDeck.GAMBLER, ExpertDeck.HERBALIST, ExpertDeck.JOKER, ExpertDeck.KNIGHT, ExpertDeck.MUSICIAN, ExpertDeck.SELLER, ExpertDeck.TAVERNER));

    /**
     * class constructor
     * @param name is a string which indicates the name of a specific card
     */
    ExpertDeck(String name){
        this.name = name;
    }

    /**
     * getText method getter of the name of the card
     * @return a string which indicates the name of the type of card specified by the enum type
     */
    public String getText(){
        return name;
    }

    /**
     * choose method allows the player to choose a card between the available ones
     * @param card is the card chosen by the player and that is going to be removed from the deck
     */
    public static void choose(ExpertDeck card) {
        available.remove(card);
    }

    /**
     * isChosen method tells whether the card selected has already been chosen
     * @param card is the card whom it is wanted to know the status (available or not)
     * @return a boolean variable telling if the card is still in the deck
     */
    public static boolean isChosen(ExpertDeck card) {
        return !(available.contains(card));
    }

    /**
     * notChosen method tells which are the cards still in the deck
     * @return a list containing all the remaining  cards in the deck
     */
    public static List<ExpertDeck> notChosen() {
        return available;
    }
}
