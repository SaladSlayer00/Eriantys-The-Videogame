package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Map;


public class Gameboard{

    //attributes of the class Gameboard
    private ArrayList<Island> islands;
    private Sack sack;
    private Cloud[] clouds;
    private ArrayList<Professor> professors = new ArrayList<>();
    private int motherNature;
    private int numClouds;

    public Gameboard(int numClouds) {
        this.numClouds = numClouds;
        clouds = new Cloud[this.numClouds];
        for(Color c : sack.colors){
            Professor p = new Professor(c);
            professors.add(p);
        }
    }

    public Sack getSack() {
        return sack;
    }

    //methods of the Gameboard
    //it places Mother Nature on a random island
    public void placeMother() {
        int random = (int)(Math.random() * 11);
        islands.get(random).motherNature = true;
        motherNature = random;
    }

    //it initializes the gameboard TO CHECK
    public void initializeIslands() throws Sack.NoMoreStudentsException {
        islands = new ArrayList<Island>();
        for(int i = 0; i < 12; i++){
            islands.set(i, new Island(i));
          }
        placeMother();


        //should add the thing that sets the students
        if(this.motherNature < 6){
            for(Island i : islands){
                if(i.index != motherNature + 6){
                    Student s = sack.drawStudent();
                    i.addStudent(s);
                }
            }
        }
        else if(this.motherNature >= 6){
            for(Island i : islands){
                if(i.index != motherNature - 6){
                    Student s = sack.drawStudent();
                    i.addStudent(s);
                }
            }
        }
    }

    //it checks that there is JUST ONE Mother Nature on the Gameboard
    public void checkMother() throws TooManyMotherNatureException {
        int counter = 0;
        for (Island i : islands) {
            if (i.motherNature == true) {
                counter = +1;
            }
        }
        if (counter > 1) {
            throw new TooManyMotherNatureException();
        }
    }

    //exception that handle the case in which we got more than one boolean of mother nature true
    public class TooManyMotherNatureException extends Exception {
        TooManyMotherNatureException() {
            super("Something's wrong...There is more than one Mother Nature on the board!");
        }
    }

    //it merges two island together
    public void mergeIslands(int one, int two) {
        islands.get(one).changeDimension(islands.get(two).dimension);
        if (islands.get(one).motherNature == false) {
            islands.get(one).addMother();
        }
        //this should add the students that were on the island that we are deleting on the one we are keeping for the merge
        for (Color c : islands.get(one).students.keySet()) {
            //islands.get(one).students.put(c, islands.get(two).students.get(c));
            islands.get(one).students.get(c).addAll(islands.get(two).students.get(c));
        }
        //should be found a smart way to merge the two map together ???
        islands.remove(two);
    }


    //it calculates the influence of a player on an island
    public boolean calculateInfluence(Player player, int island) {
        int playerInfluence = islands.get(island).calculateInfluence(player);
        //for(Color c : islands.get(island).students.keySet()){
        //islands.get(one).students.put(c, islands.get(two).students.get(c));
        //islands.get(one).students.get(c).addAll(islands.get(two).students.get(c));
        //if(player.hasProfessor(c)){
        //    playerInfluence = playerInfluence + islands.get(island).students.get(c).size();
        //}

        //}

        if (playerInfluence > islands.get(island).getInfluence()) {
            islands.get(island).setInfluence(playerInfluence);
            return true;
        }

        return false;
    }
    public void createClouds() {
        for(int i=0;i<this.numClouds;i++){
            this.clouds[i]=new Cloud(this.numClouds);
    }
}


    //classes that are used by the Gameboard

    //class Island
    private class Island {
        //attributes of the class Island
        private Map<Color, ArrayList<Student>> students; // ??? is this really necessary
        private boolean motherNature;
        private boolean hasTower = false;
        private Type tower = null;  // ???
        private int dimension = 1;
        int index;
        int influence;

        //constructor
        private Island(int index){
            this.index = index;
        }

        //methods of the class

        //it returns the index of the island
        public int getIndex(){
            return index;
        }

        //it adds a student to the island
        public void addStudent(Student student) {
            students.get(student.color).add(student);
        }

        public int getInfluence() {
            return influence;
        }

        public void setInfluence(int influence) {
            this.influence = influence;
        }

        //it adds a tower of a specific team to an island
        public void addTower(Type team) throws AlreadyATowerException{
            if(hasTower == true){
                if(this.tower.equals(team))
                    throw new AlreadyATowerException();
                else
                    this.tower = team;
            }
            else{
                hasTower = true;
                this.tower = team;
            }
        }

        //exception to handle the moment when a tower is already on an island
        public class AlreadyATowerException extends Exception{
            AlreadyATowerException(){
                super("There is already a tower on this island. It's " + tower + "!");
            }
        }

        //it returns the color of the tower TO CHECK //
        public Type getColor() throws NoTowerException{
            if (hasTower == true) {
                return tower;
            } else {
                throw new NoTowerException();
            }
        }

        //exception to handle the moment when no tower is found on an island
        public class NoTowerException extends Exception{
            NoTowerException(){
                super("There isn't any tower on this island");
            }
        }

        //it returns the influence
        public int calculateInfluence(Player player){
            int playerInfluence = 0;
            for(Color c : this.students.keySet()){
                //islands.get(one).students.put(c, islands.get(two).students.get(c));
                //islands.get(one).students.get(c).addAll(islands.get(two).students.get(c));
                if(player.hasProfessor(c)){
                    playerInfluence = playerInfluence + this.students.get(c).size();
                }

            }
            return playerInfluence;

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
           this.dimension += newDim;
        }

    }


    //class Cloud
    private class Cloud{
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
    }


    //class Sack
    class Sack{

        //attributes of class Sack
        private ArrayList<Student> students = new ArrayList<>();
        private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW};

        public Sack(){
            for(Color c : this.colors){
                for(int i = 0; i<2;i++){
                    Student s = new Student(c);
                    this.students.add(s);
                }
            }
        }
        //methods of sack
        public void initializeSack(){
            for(Color c : this.colors){
                for(int i=0; i<24;i++){
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
        public Student drawStudent() throws NoMoreStudentsException{
            if(students.isEmpty()){
                throw new NoMoreStudentsException();
            }
            else {
                int random = (int)(Math.random() * students.size()); //it chooses randomly the type of student to draw
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


}
