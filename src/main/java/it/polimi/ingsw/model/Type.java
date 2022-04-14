package it.polimi.ingsw.model;

//enumeration for the towers' color
public enum Type{
    BLACK("black"),
    WHITE("white"),
    GREY("grey");

    //it's the string of the color of the tower
    private String color;

    //constructor that initializes the enum of the wanted color
    private Type(String color){
        this.color = color;
    }
}