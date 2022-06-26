package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.alreadyATowerException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Island class
 * @author group 68 (???)
 */
public class Island implements Serializable {
    //attributes of the class Island
    private static final long serialVersionUID = 1L;
    private Map<Color, ArrayList<Student>> students = new HashMap<Color,ArrayList<Student>>();
    private boolean motherNature;
    private boolean hasTower = false;
    private Type tower = null;
    private int dimension = 1;
    int index;
    int influence=0;
    private boolean blocked=false;

    /**
     * Class constructor
     * @param index which is the index of the island on the gameboard
     */
    public Island(int index){
        this.index = index;
        students.put(Color.RED, new ArrayList<Student>());
        students.put(Color.GREEN, new ArrayList<Student>());
        students.put(Color.YELLOW, new ArrayList<Student>());
        students.put(Color.BLUE, new ArrayList<Student>());
        students.put(Color.PINK, new ArrayList<Student>());
    }


    /**
     * getIndex method getter for the index of the island on the gameboard
     * @return the index of the island
     */
    public int getIndex(){
        return index;
    }

    /**
     * addStudent method that adds a student on the island
     * @param student which is the paw that it has to be moved on the island, it is added to the students arraylist
     */
    public void addStudent(Student student) {
        students.get(student.getColor()).add(student);
    }

    /**
     * getInfluence method getter for the value of the influence
     * @return the influence actually calculated
     */
    public int getInfluence() {
        return influence;
    }

    /**
     * setInfluenceMethod method setter of the value of the influence
     * @param influence it is set as the value of the influence variable
     */
    public void setInfluence(int influence) {
        this.influence = influence;
    }

    /**
     * setMotherNature method setter for the boolean which indicates if Mother Nature's paw is on the island
     * @param motherNature indicates whether Mother Nature's paw has to be set on the island
     */
    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * getDimension method getter of the dimension of the island
     * @return the dimension of the island
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * isMOtherNature method returns whether Mother Nature's method is on the island
     * @return a boolean that indicates whether Mother Nature's paw is on the island
     */
    public boolean isMotherNature() {
        return motherNature;
    }

    /**
     * isBlocked method returns whether the island is blocked
     * @return a boolean that indicates whether the island has a block on it (for the expert mode)
     */
    public boolean isBlocked(){
        return blocked;
    }

    /**
     * setBlocked method setter for a block
     * @param blocked is a boolean that indicated whether the island must be blocked (for the expert mode)
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Map<Color, ArrayList<Student>> getStudents() {
        return students;
    }

    /**
     * addTower method adds a tower on the island
     * @param team indicates the color of the tower that has to be put on the island
     * @throws alreadyATowerException if there's a tower yet
     */
    public void addTower(Type team) throws alreadyATowerException {
        if(hasTower == true){
            if(this.tower.equals(team))
                throw new alreadyATowerException(this.tower);
            else
                this.tower = team;
        }
        else{
            hasTower = true;
            this.tower =  team;
        }
    }

    /**
     * getTeam method getter of the color of the tower on the island
     * @return the color of the tower on the island
     * @throws noTowerException if there is no tower on the island
     */
    public Type getTeam() throws noTowerException {
        if (getTower()) {
            return tower;
        } else {
            throw new noTowerException();
        }
    }

    /**
     * getTower method getter whether there is a tower on the island
     * @return a boolean which indicates whether there is a tower on the island
     */
    public boolean getTower(){
        if(hasTower)
            return true;
        else
            return false;
    }


    /**
     * calculateInfluence method that calculate the influence of a player on the island
     * @param player is the player who has requested the operation
     * @return the influence of that player on the island
     */
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

    /**
     * addMother method which sets Mother Nature's paw on the island
     */
    public void addMother(){
        motherNature = true;
    }

    /**
     * removeMother method which removes Mother Nature's paw from the island
     */
    public void removeMother(){
        motherNature = false;
    }


    /**
     * changeDimension method that change the dimension of the island
     * @param newDim is the new dimension of the island
     */
    public void changeDimension(int newDim){
        this.dimension += newDim;
    }

    /**
     * addStudentOnIsland method that add a student of a certain color on the island
     * @param c is the color of the student
     * @param s is the student that has to be added
     */
    public void addStudentOnIsland(Color c, Student s){
        for(Color color : students.keySet()){
            if(color.equals(c)){
                students.get(c).add(s);
            }
        }
    }

    /**
     * setTower method that sets a tower of a certain color on the island
     * @param tower is the tower that has to be added
     */
    public void setTower(Type tower) {
        this.tower = tower;
        hasTower=true;
    }

    /**
     * setIndex method sets the index of the island
     * @param index is the index that has to be set
     */
    public void setIndex(int index) {
        this.index = index;
    }


}