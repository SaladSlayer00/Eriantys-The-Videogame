package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

public class Professor{
    private Color color;

    public Professor(Color color){
        this.color = color;
    }

    //getter for the professor's color
    public Color getColor(){
        return color;
    }


}
