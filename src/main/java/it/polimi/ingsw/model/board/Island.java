package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.alreadyATowerException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Type;

import java.util.ArrayList;
import java.util.Map;

//class Island
public class Island {
    //attributes of the class Island
    private Map<Color, ArrayList<Student>> students;
    private boolean motherNature;
    private boolean hasTower = false;
    private Type tower = null;
    private int dimension = 1;
    int index;
    int influence;

    //constructor
    public Island(int index){
        this.index = index;
    }

    //methods of the class

    //it returns the index of the island
    public int getIndex(){
        return index;
    }

    //it adds a student to the island
    public void addStudent(Student student) {
        students.get(student.getColor()).add(student);
    }

    public int getInfluence() {
        return influence;
    }

    public void setInfluence(int influence) {
        this.influence = influence;
    }

    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean isMotherNature() {
        return motherNature;
    }

    public Map<Color, ArrayList<Student>> getStudents() {
        return students;
    }

    //it adds a tower of a specific team to an island
    public void addTower(Type team) throws alreadyATowerException {
        if(hasTower == true){
            if(this.tower.equals(team))
                throw new alreadyATowerException(this.tower);
            else
                this.tower = team;
        }
        else{
            hasTower = true;
            this.tower = team;
        }
    }

    //it returns the color of the tower TO CHECK //
    public Type getColor() throws noTowerException {
        if (hasTower == true) {
            return tower;
        } else {
            throw new noTowerException();
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