package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.exceptions.alreadyAProfessorException;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.noProfessorException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;
import java.util.ArrayList;

//the following class represents the dashboard rows
public class Row {
    //attributes of the class Row
    private Color name;
    private ArrayList<Student> students;
    private boolean hasProfessor;
    private Professor professor; //Can we eliminate it ?
    public static final int MAX_STUDENTS = 10;

    //methods of the class Row

    public Color getName() {
        return name;
    }

    public void addProfessor() throws alreadyAProfessorException {
        if (hasProfessor == true) {
            throw new alreadyAProfessorException();
        } else {
            hasProfessor = true;
        }
    }

    public void addStudent(Student student) throws maxSizeException {
        if (students.size() - 1 < MAX_STUDENTS) {
            students.add(student);
        } else {
            throw new maxSizeException();
        }
    }

    public void removeProfessor() throws noProfessorException {
        if (hasProfessor == true) {
            hasProfessor = false;
        } else {
            //we can replace it with an exception
            throw new noProfessorException();
        }
    }
}