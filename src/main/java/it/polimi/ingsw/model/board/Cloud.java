package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.ArrayList;

//class Cloud
public class Cloud implements Serializable {
    //attribute of class Cloud
    private static final long serialVersionUID = 1L;
    private int index;
    private ArrayList<Student> students = new ArrayList<Student>();
    private int dimension;

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
    //it adds a student on the cloud
    public void addStudent(Student student){
        this.students.add(student);
    }

    //it removes a student from the cloud
    public ArrayList<Student> removeStudents(){
        ArrayList<Student> temp = new ArrayList<>();
        for(int i=0 ; i<dimension;i++){
            temp.add(students.remove(0));
        }
        return temp;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
    public boolean emptyCloud() {
       return students.isEmpty();
    }

    public int getIndex() {
        return index;
   }

    public int getDimension() {
        return dimension;
    }
}