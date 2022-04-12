package it.polimi.ingsw.model;
import java.util.ArrayList;

public class Gameboard{

    //attributes of the class Gameboard
    private ArrayList<Island> islands;
    private boolean sack;
    private Cloud[] clouds;
    private ArrayList<Professor> professors;
    private int motherNature;

    //methods of the Gameboards
    //it places Mother Nature on a random island
    public void placeMother(){

    }

    //it merges two island together
    public void mergeIslands(int one, int two){

    }

    //it calculates the influence of a player on an island
    public int calculateInfluence(Player player, int island){

    }


    //classes that are used by the Gameboard

    //class Island
    private class Island {
        //attributes of the class Island
        private Map<Color, ArrayList<Student>> students;  ???
        private boolean motherNature;
        private boolean hasTower;
        private Team tower;  ???
        private int dimension = 1;
        int index;

        //methods of the class
        //it adds a student to the island
        public void addStudent(student Student) {

        }

        //it adds a tower of a specific team to an island
        public void addTower(team Team) {
            //idea: if hasTower == true then we can throw an exception
            //OR MAYBE we can just see if the person that asked for the tower
            //has more influence than the other player has on the island
            //else we add the tower and then hasTower = true
            //and tower = team;
        }

        //it returns the color of the tower
        public Team getColor() {
            if (hasTower == true) {
                return tower;
            } else {
                //exception???
            }
        }

        //it returns the influence
        public int getInfluence(player Player){


        }

        //it adds Mother Nature on the island
        public void addMother(){
            //maybe we should add a way to check if this action is legit
            motherNature = true;
        }

        //it removes Mother Nature
        public void removeMother(){
            //ditto for this method
            motherNature = false;
        }

        //it changes the dimension of the island
        public void changeDimension(){

        }

    }


    //class Cloud
    private class Cloud{
        //attribute of class Cloud
        private ArrayList<Student> students;

        //methods of cloud
        //it adds a student on the cloud
        public void addStudent(Student student){

        }

        //it removes a student from the cloud
        public void removeStduent{

        }
    }


    //class Sack
    private class Sack{
        //atributes of class Sack
        private ArrayList<Student> students;

        //methods of sack
        //it puts a student in the sack
        public void putStudent(Student student){
            add.students(student);
        }

        //it draws a student from the sack
        public Student drawStudent(){

        }

        //
        public int getNum{
            return students.length;
        }


    }


}
