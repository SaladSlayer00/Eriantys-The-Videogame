package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Cloud class for the clouds on the gameboard
 * @author Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */
public class Cloud implements Serializable {
    //attribute of class Cloud
    private static final long serialVersionUID = 1L;
    private int index;
    private ArrayList<Student> students = new ArrayList<Student>();
    private int dimension;

    /**
     * class constructor
     * @param players indicates the number of player for the current match
     * @param index indicates the index of the very cloud
     */
    public Cloud(int players , int index){
        if(players == 2 || players == 4){
            dimension = 3;

        }
        else{
            dimension = 4;
        }
        this.index = index;
    }
    //methods of cloud

    /**
     * addStudent method adds a student paw to the cloud
     * @param student indicates the student that it has to be added
     */
    public void addStudent(Student student){
        this.students.add(student);
    }

    /**
     * removeStudents method removes the students from the cloud (because the cloud it has been chosen by a player)
     * @return an arraylist that contains the students that where on the cloud before
     */
    public ArrayList<Student> removeStudents(){
        ArrayList<Student> temp = new ArrayList<>();
        for(int i=0 ; i<dimension;i++){
            temp.add(students.remove(0));
        }
        return temp;
    }

    /**
     * getStudents method is a getter for the students on the cloud
     * @return an arraylist of the students on the cloud
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * emptyCloud method indicates whether a cloud has students on itself
     * @return a boolean value which indicates if the cloud has paws on itself
     */
    public boolean emptyCloud() {
       return students.isEmpty();
    }

    /**
     * getIndex method is a getter for the index of the very cloud
     * @return the index of the cloud
     */
    public int getIndex() {
        return index;
   }

    /**
     * getDimension method is a getter for the number of students that have to be on the cloud
     * @return the dimension of the very cloud (given by the number of players)
     */
    public int getDimension() {
        return dimension;
    }
}