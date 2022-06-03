package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    Color color;

    public Student(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
