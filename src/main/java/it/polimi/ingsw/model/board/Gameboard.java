package it.polimi.ingsw.model.board;
import java.io.Serializable;
import java.util.ArrayList;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.exceptions.tooManyMotherNatureException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.observer.Observable;

public class Gameboard extends Observable implements Serializable {

    //attributes of the class Gameboard
    private ArrayList<Island> islands;
    private Sack sack;
    private ArrayList<Cloud> clouds;
    private ArrayList<Professor> professors = new ArrayList<>();
    private int motherNature;
    private int numClouds;

    public Gameboard(int numClouds) {
        this.numClouds = numClouds;
        clouds = new ArrayList<>();
        for (Color c : sack.getColors()) {
            Professor p = new Professor(c);
            professors.add(p);
        }
        this.sack = new Sack();
        this.sack.initializeSack();

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
    public void mergeIslands(Island active) throws noTowerException {
        Island before;
        Island after;
        if(active.getIndex()==0){
            before=islands.get(islands.size()-1);
            after = islands.get(1);
        }else if(active.getIndex()==islands.size()-1){
            after=islands.get(0);
            before = islands.get(active.getIndex()-1);
        }
        else{
            before = islands.get(active.getIndex()-1);
            after= islands.get(active.getIndex()+1);
        }
        if(before.getTower()) {
            if (before.getTeam().equals(active.getTeam())) {
                active.changeDimension(before.getDimension());
                for(Color c : before.getStudents().keySet()){
                    active.getStudents().get(c).addAll(before.getStudents().get(c));
                }
                islands.remove(before);
                if(active.getIndex()!=0) {
                    for (int i = active.getIndex(); i < islands.size(); i++) {
                        islands.get(i).setIndex(i - 1);
                    }
                }
            }
        }
        if(after.getTower()) {
            if (after.getTeam().equals(active.getTeam())) {
                active.changeDimension(after.getDimension());
                for(Color c : after.getStudents().keySet()){
                    active.getStudents().get(c).addAll(after.getStudents().get(c));
                }
                islands.remove(after);
                if(active.getIndex()==islands.size()-1){
                    for(int i = 1;i<islands.size();i++){
                        //secondo me gli indici non servono
                    }
                }
                for(int i = active.getIndex()+1;i < islands.size()-1;i++){
                    if(i==0){
                        active.setIndex(islands.size()-1);
                    }
                    else {
                        islands.get(i).setIndex(i - 1);
                    }
                }
            }
        }

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
           // this.clouds[i] = new Cloud(this.numClouds);
            clouds.add(new Cloud(numClouds));
        }
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public void placeStudent(Color c, Student s, int island){
        islands.get(island).addStudentOnIsland(c, s);
    }

    public int getMotherNature() {
        return motherNature;
    }

    public void setMotherNature(int motherNature) {
        this.motherNature = motherNature;
    }

    public Cloud chooseCloud(int index){
        return clouds.remove(index);
    }

    public Cloud getCloud(int index) {return clouds.get(index);}


}
