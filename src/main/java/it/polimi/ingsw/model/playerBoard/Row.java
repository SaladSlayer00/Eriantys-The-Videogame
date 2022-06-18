package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.exceptions.alreadyAProfessorException;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.noProfessorException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.ArrayList;

//the following class represents the dashboard rows
public class Row  implements Serializable {
    //attributes of the class Row
    private static final long serialVersionUID = -3704504226997118508L;
    private Color name;
    private ArrayList<Student> students = new ArrayList<>();
    private boolean hasProfessor = false;
    private Professor professor; //Can we eliminate it ?
    public static final int MAX_STUDENTS = 10;

    //constructor of the row
    //IS THIS METHOD ALREADY IMPLEMENTED???????
    public Row(Color c){
        name = c;
    }
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

    public boolean checkProfessor(){
        if(hasProfessor == true){
            return true;
        }
        return false;
    }

    public void addStudent(Student student) throws maxSizeException {
        if (students.size() - 1 < MAX_STUDENTS) {
            students.add(student);
        } else {
            throw new maxSizeException();
        }
    }

    public int getNumOfStudents(){
        return students.size();
    }

    public void removeProfessor() throws noProfessorException {
        if (hasProfessor == true) {
            hasProfessor = false;
        } else {
            //we can replace it with an exception
            throw new noProfessorException();
        }
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Student removeStudent(){
        return students.remove(0);
    }
}