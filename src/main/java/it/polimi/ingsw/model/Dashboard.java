package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;

//The following class represents the player's dashboard
public class Dashboard {
    //attributes of the class dashboard
    private int towers;
    private int numTowers;
    private final Row[] rows = new Row[NUM_ROWS];
    private final ArrayList<Student> hall = new ArrayList<>();
    private Type team;
    private static final int NUM_ROWS = 5;
    private int hallDimension;

    public void setHallDimension(int hallDimension) {
        this.hallDimension = hallDimension;
    }

    public int getHallDimension() {
        return hallDimension;
    }

    //methods of the class dashboard
    public void addToHall(Student s) {
        hall.add(s);
    }

    public void addStudent(Student student) throws maxSizeException {
        /*ALTERNATIVE VERSION
        for (int i = 0; i < NUM_ROWS; i++) {
            if (rows[i].getName().equals(student.color))
                rows[i].addStudent(student);
        }
        */
        for (Row r : this.rows) {
            if (r.getName().equals(student.color)) {
                r.addStudent(student);
            }
        }
    }

    public Student takeStudent(Color c) throws noStudentException {
        for (Student s : this.hall) {
            if (s.color.equals(c)) {
                hall.remove(s);
                return s;
            }
        }
        throw new noStudentException();
    }

    //puts a tower on the dashboard
    public void putTower() throws fullTowersException {
        if (towers < numTowers) {
            towers++;
        } else {
            throw new fullTowersException();
        }
    }

    public Type getTower() throws noTowersException {
        if (towers > 0) {
            towers--;
            return this.team;
        }
        throw new noTowersException();

    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public void setTeam(Type team) {
        this.team = team;
    }
}


    //the following class represents the dashboard rows
    private class Row {
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
}

