package it.polimi.ingsw.model;
import java.util.ArrayList;

public class Gameboard{

    //attributes of the class Gameboard
    private ArrayList<Island> islands;
    private boolean sack;
    private Cloud[] clouds;
    private ArrayList<Professor> professors;
    private int motherNature;

    //methods of the Gameboard
    //it places Mother Nature on a random island
    public void placeMother(){
        int random = (int)(Math.random() * 11);
        islands.get(random).motherNature = true;
        if(random < 6) {
            //no students should be on the islands opposite at mn at the start of the match
            islands.get(random + 6)...;
        }
        else{
            //ditto
            islands.get(random - 6)...;
        }
    }

    //it merges two island together
    public void mergeIslands(int one, int two) {
        islands.get(one).dimension =+ islands.get(two).dimension;
        if(islands.get(one).motherNature == false){
            islands.get(one).addMother;
        }
        for(Color c : islands.get(two).students.keySet()){
            for(Color k : islands.get(one).students.keySet()){
                if(k.equals(c)){
                    //how should this thing go on???
                }
            }
        }
        //should be found a smart way to merge the two map together ???
        islands.remove(two);
    }

    //it calculates the influence of a player on an island
    public int calculateInfluence(Player player, int island){

    }


    //classes that are used by the Gameboard

    //class Island
    private class Island {
        //attributes of the class Island
        private Map<Color, ArrayList<Student>> students; // ???
        private boolean motherNature;
        private boolean hasTower;
        private Team tower;  // ???
        private int dimension = 1;
        int index;

        //methods of the class
        //it adds a student to the island
        public void addStudent(Student student) {

        }

        //it adds a tower of a specific team to an island
        public void addTower(team Team) throws AlreadyATowerException{
            if(hasTower == true){
                thorw new AlreadyATowerException();
            }
            else{
                hasTower = true;
            }
        }

        //exception to handle the moment when a tower is already on an island
        //!!!
        //colorToString(tower) may be a method to get the color of the tower we want as a string
        //!!!
        public class AlreadyATowerException extends Exception{
            AlreadyATowerException(){
                super("There is already a tower on this island. It's " + colorToString(getColor()) + "!");
            }
        }

        //it returns the color of the tower TO CHECK!!!!!!
        public Team getColor() throws NoTowerException{
            if (hasTower == true) {
                return tower;
            } else {
                throw new NoTowerException();
                getInfluence(player);
            }
        }

        //exception to handle the moment when no tower is found on an island
        public class NoTowerException extends Exception{
            NoTowerException(){
                super("There isn't any tower on this island");
            }
        }

        //it returns the influence
        public int getInfluence(Player player){


        }

        //it adds Mother Nature on the island
        public void addMother(){
            motherNature = true;
        }

        //it removes Mother Nature
        public void removeMother(){
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
