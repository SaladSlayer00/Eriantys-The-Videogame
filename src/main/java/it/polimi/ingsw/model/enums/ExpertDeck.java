package it.polimi.ingsw.model.enums;

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

    //constructor that initializes the enum of the wanted color
    ExpertDeck(String name){
        this.name = name;
    }

    public String getText(){
        return name;
    }
}
