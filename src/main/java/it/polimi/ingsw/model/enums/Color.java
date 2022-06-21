package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;

//enumeration for the paws' color
public enum Color{
    RED("red"),
    GREEN("green"),
    PINK("pink"),
    YELLOW("yellow"),
    BLUE("blue");
    //it's the string of the color of the paw
    private String color;
    private static final ArrayList<Color> available = new ArrayList<Color>(Arrays.asList(Color.GREEN,Color.RED,Color.YELLOW,Color.PINK,Color.BLUE));

    //constructor that initializes the enum of the wanted color
    Color(String color){
        this.color = color;
    }

    public String getText(){
        return color;
    }

    public static ArrayList getAvailable(){
        return available;
    }


}

