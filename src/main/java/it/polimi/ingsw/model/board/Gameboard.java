package it.polimi.ingsw.model.board;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.exceptions.tooManyMotherNatureException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.observer.Observable;

public class Gameboard extends Observable implements Serializable {

    //attributes of the class Gameboard
    private static final long serialVersionUID = -3704504226997118508L;
    private ArrayList<Island> islands;
    private Sack sack;
    private ArrayList<Cloud> clouds = new ArrayList<>();
    private ArrayList<Professor> professors = new ArrayList<>();
    private int motherNature;
    private int numClouds;
    private int coins = 20;
    private List<ExpertDeck> experts = new ArrayList<>();
    private modeEnum mode;
    private List<ExpertDeck> toReset= new ArrayList<>();
    private List<Character> activeCards = new ArrayList<>();

    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW};

    public Gameboard(int numClouds) {
        this.numClouds = numClouds;
        for (Color c : colors) {
            Professor p = new Professor(c);
            professors.add(p);
        }
        this.sack = new Sack();
    }

    public void setMode(modeEnum mode) {
        this.mode = mode;
    }

    public modeEnum getMode() {
        return mode;
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
            //islands.set(i, new Island(i));
            islands.add(new Island(i));
        }
        placeMother();

        if (this.motherNature < 6) {
            for (Island i : islands) {
                if (i.index != motherNature + 6 && i.index !=motherNature) {
                    Student s = sack.drawStudent();
                    i.addStudent(s);
                }
            }
        } else if (this.motherNature >= 6) {
            for (Island i : islands) {
                if (i.index != motherNature - 6 && i.index != motherNature) {
                    Student s = sack.drawStudent();
                    i.addStudent(s);
                }
            }
        }
        //sack.initializeSack();
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
        if(islands.indexOf(active)==0){
            before=islands.get(islands.size()-1);
            after = islands.get(1);
        }else if(islands.indexOf(active)==islands.size()-1){
            after=islands.get(0);
            before = islands.get(islands.indexOf(active)-1);
        }
        else{
            before = islands.get(islands.indexOf(active)-1);
            after= islands.get(islands.indexOf(active)+1);
        }
        Island motherIsland = islands.get(getMotherNature());
        if(before.getTower()) {
            if (before.getTeam().equals(active.getTeam())) {
                active.changeDimension(before.getDimension());
                for(Color c : before.getStudents().keySet()){
                    active.getStudents().get(c).addAll(before.getStudents().get(c));
                }

                if(!toReset.contains(ExpertDeck.HERALD) ||(toReset.contains(ExpertDeck.HERALD) &&motherIsland.equals(before))) {
                    islands.remove(before);
                    setMotherNature(islands.indexOf(active));
                }
                else{
                    islands.remove(before);
                    setMotherNature(islands.indexOf(motherIsland));
                }
                //setMotherNature(islands.indexOf(active));
            }
        }
        if(after.getTower()) {
            if (after.getTeam().equals(active.getTeam())) {
                active.changeDimension(after.getDimension());
                for(Color c : after.getStudents().keySet()){
                    active.getStudents().get(c).addAll(after.getStudents().get(c));
                }
                if(!toReset.contains(ExpertDeck.HERALD) || toReset.contains(ExpertDeck.HERALD) && after.equals(motherIsland)) {
                    islands.remove(after);
                    setMotherNature(islands.indexOf(active));
                }
                else{
                    islands.remove(after);
                    setMotherNature(islands.indexOf(motherIsland));
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

    public void createClouds(){
        for (int i = 0; i < this.numClouds; i++) {
            clouds.add(new Cloud(numClouds , i));
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

    public void removeCoin(){
        coins=coins-1;
    }

    public void removeProfessor(Color color) {
        Professor chosenProfessor = null;
        for (Professor professor : professors) {
            if (professor.getColor().equals(color)) {
               chosenProfessor = professor;
            }
        }
        if(chosenProfessor!=null){
            professors.remove(chosenProfessor);
        }


    }
    public void addProfessor(Color color){
        professors.add(new Professor(color));
    }

    //testing methods
    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public int getCoins(){
        return coins;
    }

    public List<ExpertDeck> getExperts() {
        return experts;
    }


    //passo solo quelli da input
    public void setToReset(List<ExpertDeck> toReset) {
        this.toReset = toReset;
    }

    public List<ExpertDeck> getToReset() {
        return toReset;
    }

    public void setActiveCards(List<Character> activeCards) {
        this.activeCards = activeCards;
    }

    public List<Character> getActiveCards() {
        return activeCards;
    }
}
