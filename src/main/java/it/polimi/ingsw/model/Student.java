package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

public class Student{
    Color color;

    public Student(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
