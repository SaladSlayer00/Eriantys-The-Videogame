package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    //constructor that initializes the enum of the wanted color
    ExpertDeck(String name){
        this.name = name;
    }

    public String getText(){
        return name;
    }

    public static void choose(ExpertDeck card) {
        available.remove(card);
    }

    public static boolean isChosen(ExpertDeck card) {
        return !(available.contains(card));
    }

    public static List<ExpertDeck> notChosen() {
        return available;
    }
}
