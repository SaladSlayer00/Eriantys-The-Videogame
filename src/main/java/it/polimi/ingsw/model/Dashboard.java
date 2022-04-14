package it.polimi.ingsw.model;

import java.util.ArrayList;

//The following class represents the player's dashboard
public class Dashboard {
    //attributes of the class dashboard
    private int towers;
    private int numTowers;
    private Row[] rows;
    private Student[] hall;
    private Type team;

    //methods of the class dashboard
    public void addStudent(Student student){

    }
    public Student takeStudent() {

    }


    //the following class represents the dashboard rows
    private class Row{
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
            if (hasProfessor == true)
            {
                throw new AlreadyAProfessorException();
            }else{
                hasProfessor = true;
            }
        }

        public void addStudent(Student student) throws MaxSizeException{
            if(students.size()-1 < MAX_STUDENTS)
            {
                students.add(student);
            }else{
                throw new MaxSizeException();
            }
        }

        public void removeProfessor() {
            if (hasProfessor == true)
            {
                hasProfessor = false;
            }else{
                //we can replace it with an exception
                System.out.println("This row does not have a professor");
            }
        }


    }

    //exception to manage the existence of the professor
    public class  AlreadyAProfessorException extends Exception {
        AlreadyAProfessorException(){super("This row already has the professor");}

    }

    public class  MaxSizeException extends Exception {
        MaxSizeException(){super("You have reached the maximum number of students that can be placed");}

    }



}

