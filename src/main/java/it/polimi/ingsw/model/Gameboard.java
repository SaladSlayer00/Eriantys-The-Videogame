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
        motherNature = random;
        if(random < 6) {
            //no students should be on the islands opposite at mn at the start of the match
            islands.get(random + 6)...;
        }
        else{
            //ditto
            islands.get(random - 6)...;
        }
    }

    //it checks that there is JUST ONE Mother Nature on the Gameboard
    public void checkMother() throws TooManyMotherNatureException {
          int counter = 0;
          for(Island i : islands){
              if(i.motherNature == true){
                  counter =+1;
              }
          }
          if(counter > 1) {
              throw new TooManyMotherNatureException();
          }
    }

    //exception that handle the case in which we got more than one boolean of mother nature true
    public class TooManyMotherNatureException extends Exception(){
        TooManyMotherNatureException(){
            super("Something's wrong...There is more than one Mother Nature on the board!");
        }
    }

    //it merges two island together
    public void mergeIslands(int one, int two) {
        islands.get(one).changeDimension(islands.get(two).dimension);
        if(islands.get(one).motherNature == false){
            islands.get(one).addMother;
        }
        //this should add the students that were on the island that we are deleting on the one we are keeping for the merge
        for(Color c : islands.get(one).students.keySet()){
            islands(one).students.put(c, islands.get(two).students.get(c));
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
        private Map<Color, ArrayList<Student>> students; // ??? is this really necessary
        private boolean motherNature;
        private boolean hasTower;
        private Team tower;  // ???
        private int dimension = 1;
        int index;

        //constructor
        Island(){

        }

        //methods of the class
        //it adds a student to the island
        public void addStudent(Student student) {
            students.put(student.color, student);
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
        public class AlreadyATowerException extends Exception{
            AlreadyATowerException(){
                super("There is already a tower on this island. It's " + tower + "!");
            }
        }

        //it returns the color of the tower TO CHECK
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
        public void changeDimension(int newDim){
           this.dimension =+newDim;
        }

    }


    //class Cloud
    private class Cloud{
        //attribute of class Cloud
        private int index;
        private ArrayList<Student> students;

        //methods of cloud
        //it adds a student on the cloud
        public void addStudent(Student student){

        }

        //it removes a student from the cloud
        public void removeStudents(){
          for(Student s : students){
              students.remove(s);
          }
        }
    }


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
        public Student drawStudent() throws NoMoreStudentsException{
            if(stdents.isEmpty()){
                trow new NoMoreStudentsException();
            }
            else {
                int random = (int) (Math.random() * students.size()); //it chooses randomly the type of student to draw
                Student choosen = students.get(random);
                students.remove(random);
                return choosen;
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


}
