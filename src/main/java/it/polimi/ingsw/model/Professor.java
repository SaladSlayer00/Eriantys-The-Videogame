package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.io.Serializable;

public class Professor implements Serializable {
    private static final long serialVersionUID = -3704504226997118508L;
    private Color color;

    public Professor(Color color){
        this.color = color;
    }

    //getter for the professor's color
    public Color getColor(){
        return color;
    }


}
