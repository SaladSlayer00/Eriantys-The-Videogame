package it.polimi.ingsw.model.board;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Sack class for the game sack which contains the students' paws
 */
public class Sack implements Serializable {

    //attributes of class Sack
    private ArrayList<Student> students = new ArrayList<>();
    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW};

    /**
     * class constructor
     */
    public Sack(){
        for(Color c : this.colors){
            for(int i = 0; i < 2; i++){
                Student s = new Student(c);
                this.students.add(s);
            }
        }
    }
    //methods of sack

    /**
     * initializeSack method puts the students' paw in the sack at the beginning of the match
     */
    public void initializeSack(){
        for(Color c : this.colors){
            for(int i = 0; i < 24; i++){
                Student s = new Student(c);
                this.students.add(s);
            }
        }
    }

    /**
     * putStudent method puts a student of a certain color in the sack
     * @param student is the very student that it has to be added to the sack
     */
    public void putStudent(Student student){
        students.add(student);
    }

    /**
     * drawStudent method is called to draw a student randomly out of the sack
     * @return a random student between the ones in the sack
     */
    public Student drawStudent() {

        if (!(students.isEmpty())) {
            int random = (int) (Math.random() * students.size()); //it chooses randomly the type of student to draw
            Student chosen = students.get(random);
            students.remove(random);
            return chosen;
        } else {
            return null;
        }



    }

    /**
     * getNum method getter for the number of students' paw in the sack
     * @return
     */
    public int getNum(){
        return students.size();
    }

    /**
     * getStudents method getter for the students within the sack
     * @return an arraylist with the students in the sack
     */
    public ArrayList<Student> getStudents(){
        return students;
    }

    /**
     * isEmpty method indicates whether the sack has students in it
     * @return a boolean value telling the status of the sack (empty or not)
     */
    public boolean isEmptySack(){
       return students.isEmpty();

    }
}