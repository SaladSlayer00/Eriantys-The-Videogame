package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Color enum class for the students' paws' colors
 */
public enum Color{
    RED("red"),
    GREEN("green"),
    PINK("pink"),
    YELLOW("yellow"),
    BLUE("blue");
    //it's the string of the color of the paw
    private String color;
    private static final ArrayList<Color> available = new ArrayList<Color>(Arrays.asList(Color.GREEN,Color.RED,Color.YELLOW,Color.PINK,Color.BLUE));

    /**
     * class constructor
     * @param color is the string corresponding to one of the colors of the enum
     */
    Color(String color){
        this.color = color;
    }

    /**
     * getText method is a getter of the string indicating the color of a specific enum type
     * @return the color of the enum type as a string
     */
    public String getText(){
        return color;
    }

    /**
     * getAvailable method is a getter of the available colors type
     * @return an arraylist with the colors available in the specific moment
     */
    public static ArrayList getAvailable(){
        return available;
    }


}

