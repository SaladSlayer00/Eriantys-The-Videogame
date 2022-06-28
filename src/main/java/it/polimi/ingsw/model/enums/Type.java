package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Type class for the towers' colors players can choose from for the match
 */

public enum Type{
    BLACK("black"),
    WHITE("white"),
    GREY("grey");

    private static final ArrayList<Type> available = new ArrayList<>(Arrays.asList(Type.WHITE,Type.BLACK,Type.GREY));

    //it's the string of the color of the tower
    private String color;

    /**
     * class constructor
     * @param color is the color chosen for the towers
     */
    private Type(String color){
        this.color = color;
    }

    /**
     * reset method resets the list indicating the available towers
     */
    public static void reset() {
        available.clear();
        available.add(BLACK);
        available.add(WHITE);
        available.add(GREY);
    }

    /**
     * choose method allows the player to choose a set of towers
     * @param type is the towers' color chosen by the player which is going to be removed from the possible available choices
     */
    public static void choose(Type type) {
        available.remove(type);
    }

    /**
     * isChosen method tells whether a color has already been chosen
     * @param type is the type whom it is needed to check the status
     * @return a boolean variable indicating the status of the towers' color
     */
    public static boolean isChosen(Type type) {
        return !(available.contains(type));
    }

    /**
     * notChosen method tells which sets of towers are already available to choose
     * @return a list of the remaining sets of towers
     */
    public static List<Type> notChosen() {
        return available;
    }

    /**
     * parseInput method normalises the input to make it comprehensible for the game logic
     * @param input is the towers' color typed as input by a player
     * @return the enum of the color typed
     */
    public static Type parseInput(String input) {
        return Enum.valueOf(Type.class, input.toUpperCase());
    }

    /**
     * isEmpty method tells whether there are available sets of towers left
     * @return a boolean variable indicating the status of the list of possible choices
     */
    public static boolean isEmpty(){
        return(available.size()==0);
    }

    /**
     * getText method is a getter for the name of the towers' color
     * @return a string indicating the color's name of a set of towers
     */
    public String getText(){
        return this.color;
    }
}