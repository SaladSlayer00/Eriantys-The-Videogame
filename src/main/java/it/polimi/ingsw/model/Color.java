package it.polimi.ingsw.model;

//enumeration for the paws' color
public enum Color{
    RED("red"),
    GREEN("green"),
    PINK("pink"),
    YELLOW("yellow"),
    BLUE("blue");

    //it's the string of the color of the paw
    private String color;

    //constructor that initializes the enum of the wanted color
    Color(String color){
        this.color = color;
    }
}

