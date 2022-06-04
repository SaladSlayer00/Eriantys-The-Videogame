package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;

import java.util.List;

public class AskMoveMessage extends Message{
    private static final long serialVersionUID = -3704504226997118508L;
    private List<Student> students = null;
    private List<Island> islands;


    public AskMoveMessage(String nickname, List<Student> students, List<Island> islands) {
        super(nickname, MessageType.ASK_MOVE);
        this.students = students;
        this.islands = islands;
    }


    public String toString() {
        return "MoveMessage{" +
                "nickname=" + getNickname() +
                ", Islands=" + this.getIslands() + ", Students= "+ this.getIslands() +
                '}';
    }


    public List<Student> getStudents(){
        return this.students;
    }

    public List<Island> getIslands(){
        return this.islands;
    }
}
