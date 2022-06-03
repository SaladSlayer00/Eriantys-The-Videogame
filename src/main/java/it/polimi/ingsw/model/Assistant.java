package it.polimi.ingsw.model;
import java.io.Serializable;
//The following class represent the Assistant

public class Assistant implements Serializable{
    //attributes of the class Assistant
    private static final long serialVersionUID = -2089913761654565866L;
    private int numOrder;
    private int move;
    //methods of the class Assistant
    public Assistant(int numOrder , int move){
        this.numOrder = numOrder;
        this.move = move;
    }

    public int getMove() {
        return move;
    }

    public int getNumOrder() {
        return numOrder;
    }

}
