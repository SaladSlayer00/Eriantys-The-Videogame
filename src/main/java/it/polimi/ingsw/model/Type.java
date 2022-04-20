package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

//enumeration for the towers' color
public enum Type{
    BLACK("black"),
    WHITE("white"),
    GREY("grey");

    private static final ArrayList<Type> available = new ArrayList<>();

    //it's the string of the color of the tower
    private String color;

    //constructor that initializes the enum of the wanted color
    private Type(String color){
        this.color = color;
    }

    public static void reset() {
        available.clear();
        available.add(BLACK);
        available.add(WHITE);
        available.add(GREY);
    }

    public static void choose(Type type) {
        available.remove(type);
    }

    public static boolean isChosen(Type type) {
        return !(available.contains(type));
    }

    public static List<Type> notChosen() {
        return available;
    }

    public static Type parseInput(String input) {
        return Enum.valueOf(Type.class, input.toUpperCase());
    }

}