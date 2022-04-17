package it.polimi.ingsw.model;

import java.util.ArrayList;

//The following class represents the player's dashboard
public class Dashboard {
    //attributes of the class dashboard
    private int towers;
    private int numTowers;
    private Row[] rows;
    private ArrayList<Student> hall;
    private Type team;
    private static final int NUM_ROWS = 5;


    //methods of the class dashboard
    public void addStudent(Student student) throws MaxSizeException {
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

        public void addProfessor() throws AlreadyAProfessorException {
            if (hasProfessor == true) {
                throw new AlreadyAProfessorException();
            } else {
                hasProfessor = true;
            }
        }

        public void addStudent(Student student) throws MaxSizeException {
            if (students.size() - 1 < MAX_STUDENTS) {
                students.add(student);
            } else {
                throw new MaxSizeException();
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
    //exception to manage the existence of the professor
     class  AlreadyAProfessorException extends Exception {
        AlreadyAProfessorException(){super("This row already has the professor");}

    }

    class  noProfessorException extends Exception {
        noProfessorException(){super("There's no professor to be taken");}

    }

    class  MaxSizeException extends Exception {
        MaxSizeException(){super("You have reached the maximum number of students that can be placed");}

    }

    class  noStudentException extends Exception {
        noStudentException(){super("No student matches the selected color");}

    }

    class  fullTowersException extends Exception {
        fullTowersException(){super("You have reached the maximum number of towers that can be placed");}

    }
    class  noTowersException extends Exception {
        noTowersException(){super("There are no towers left!");}

    }
