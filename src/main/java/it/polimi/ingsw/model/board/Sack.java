package it.polimi.ingsw.model.board;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Student;
import java.util.Arrays;
import java.util.ArrayList;

//class Sack
public class Sack{

    //attributes of class Sack
    private ArrayList<Student> students = new ArrayList<>();
    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW};

    public Sack(){
        for(Color c : this.colors){
            for(int i = 0; i < 2; i++){
                Student s = new Student(c);
                this.students.add(s);
            }
        }
    }
    //methods of sack
    public void initializeSack(){
        for(Color c : this.colors){
            for(int i = 0; i < 24; i++){
                Student s = new Student(c);
                this.students.add(s);
            }
        }
    }
    //it puts a student in the sack TO CHECK
    public void putStudent(Student student){
        students.add(student);
    }

    //it draws a student from the sack TO CHECK
    public Student drawStudent() throws noMoreStudentsException {
        if(students.isEmpty()){
            throw new noMoreStudentsException();
        }
        else {
            int random = (int)(Math.random() * students.size()); //it chooses randomly the type of student to draw
            Student chosen = students.get(random);
            students.remove(random);
            return chosen;
        }
    }


    //it gives the number of students that are still in the sack
    public int getNum(){
        return students.size();
    }

    public Color[] getColors() {
        return Arrays.copyOf(colors,colors.length);
    }
}