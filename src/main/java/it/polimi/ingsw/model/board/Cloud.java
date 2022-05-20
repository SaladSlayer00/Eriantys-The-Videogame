package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.Student;
import java.util.ArrayList;

//class Cloud
public class Cloud{
    //attribute of class Cloud
    private int index;
    private ArrayList<Student> students;
    private int dimension;

    public Cloud(int dimension){
        if(dimension == 2 || dimension == 4){
            this.dimension = 3;
        }
        else{
            this.dimension = 4;
        }
    }
    //methods of cloud
    //it adds a student on the cloud
    public void addStudent(Student student){
        this.students.add(student);
    }

    //it removes a student from the cloud
    public ArrayList<Student> removeStudents(){
        ArrayList<Student> temp = new ArrayList<>(this.students);
        for(Student s : students){
            students.remove(s);
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
}