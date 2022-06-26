package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.io.Serializable;

/**
 * represents a single instance of a professors, its color is specified as a
 * construction parameter
 */

public class Professor implements Serializable {
    private static final long serialVersionUID = -3704504226997118508L;
    private Color color;

    public Professor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

}
