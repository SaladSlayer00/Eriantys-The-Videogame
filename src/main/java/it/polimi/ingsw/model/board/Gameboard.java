package it.polimi.ingsw.model.board;
import java.util.ArrayList;
import java.util.Map;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.tooManyMotherNatureException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;

public class Gameboard {

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
        for (Color c : sack.getColors()) {
            Professor p = new Professor(c);
            professors.add(p);
        }
        this.sack = new Sack();

    }

    public Sack getSack() {
        return sack;
    }

    //methods of the Gameboard
    //it places Mother Nature on a random island
    public void placeMother() {
        int random = (int) (Math.random() * 11);
        //islands.get(random).motherNature = true;
        islands.get(random).setMotherNature(true);
        motherNature = random;
    }

    //it initializes the gameboard TO CHECK
    public void initializeIslands() throws noMoreStudentsException {
        islands = new ArrayList<Island>();
        for (int i = 0; i < 12; i++) {
            islands.set(i, new Island(i));
        }
        placeMother();

        if (this.motherNature < 6) {
            for (Island i : islands) {
                if (i.index != motherNature + 6) {
                    Student s = sack.drawStudent();
                    i.addStudent(s);
                }
            }
        } else if (this.motherNature >= 6) {
            for (Island i : islands) {
                if (i.index != motherNature - 6) {
                    Student s = sack.drawStudent();
                    i.addStudent(s);
                }
            }
        }
        sack.initializeSack();
    }

    //it checks that there is JUST ONE Mother Nature on the Gameboard
    public void checkMother() throws tooManyMotherNatureException {
        int counter = 0;
        for (Island i : islands) {
            if (i.isMotherNature() == true) {
                counter = +1;
            }
        }
        if (counter > 1) {
            throw new tooManyMotherNatureException();
        }
    }

    //it merges two island together
    public void mergeIslands(int one, int two) {
        islands.get(one).changeDimension(islands.get(two).getDimension());
        if (islands.get(one).isMotherNature() == false) {
            islands.get(one).addMother();
        }
        //this should add the students that were on the island that we are deleting on the one we are keeping for the merge
        for (Color c : islands.get(one).getStudents().keySet()) {
            //islands.get(one).students.put(c, islands.get(two).students.get(c));
            islands.get(one).getStudents().get(c).addAll(islands.get(two).getStudents().get(c));
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
        for (int i = 0; i < this.numClouds; i++) {
            this.clouds[i] = new Cloud(this.numClouds);
        }
    }

    public Cloud[] getClouds() {
        return clouds;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

}
