package it.polimi.ingsw.model;

import java.util.ArrayList;


//class Sack
private class Sack{
    //attributes of class Sack
    private ArrayList<Student> students;

    //methods of sack
    //it puts a student in the sack TO CHECK
    public void putStudent(Student student){
        students.add(student);
    }

    //it draws a student from the sack TO CHECK
    public Student drawStudent() throws Gameboard.Sack.NoMoreStudentsException {
        if(students.isEmpty()){
            throw new Gameboard.Sack.NoMoreStudentsException();
        }
        else {
            int random = (int) (Math.random() * students.size()); //it chooses randomly the type of student to draw
            Student chosen = students.get(random);
            students.remove(random);
            return chosen;
        }
    }

    //exception that handle the emptiness of the sack. Should this method call for the end of the game???
    public class NoMoreStudentsException extends Exception{
        NoMoreStudentsException(){
            super("There are no more students in the sack!");
        }
    }

    //it gives the number of students that are still in the sack
    public int getNum(){
        return students.size();
    }


}
