package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.Observable;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * represents a single instance of a player's dashbboard, contains
 * all the needed parameters and some methods to act on the hall and rows,
 * to set and remove towers
 */
public class Dashboard extends Observable implements Serializable {
    //attributes of the class dashboard
    private static final long serialVersionUID = -3704504226997118508L;
    private int towers; //fiso
    private int numTowers; //variabile
    private final ArrayList<Row> rows = new ArrayList<>();
    private final ArrayList<Student> hall = new ArrayList<>();
    private Type team;
    private int hallDimension;
    private String owner;

    public Dashboard(int numPlayers, String playerName){
        if(numPlayers == 2){
            this.numTowers = 8;
            this.hallDimension = 7;
        }else if(numPlayers == 3){
            this.numTowers = 6;
            this.hallDimension = 9;
        }
        rows.add(new Row(Color.RED));
        rows.add(new Row(Color.GREEN));
        rows.add(new Row(Color.PINK));
        rows.add(new Row(Color.YELLOW));
        rows.add(new Row(Color.BLUE));
        this.towers = numTowers;
        this.owner = playerName;

    }

    public int getHallDimension() {
        return hallDimension;
    }

    //methods of the class dashboard
    public void addToHall(Student s) {
        hall.add(s);
    }

    public void addStudent(Student student) throws maxSizeException {

        for (Row r : this.rows) {
            if (r.getName().equals(student.getColor())) {
                r.addStudent(student);
            }
        }
    }

    public Student takeStudent(Color c) throws noStudentException {
        for (Student s : this.hall) {
            if (s.getColor().equals(c)) {
                hall.remove(s);
                return s;
            }
        }
        throw new noStudentException();
    }

    //puts a tower on the dashboard
    public void putTower() throws fullTowersException {
        if (towers > numTowers) {
            numTowers++;
        } else {
            throw new fullTowersException();
        }
    }

    public Type getTower() throws noTowersException {
        if (numTowers > 0) {
            numTowers--;
            return this.team;
        }
        throw new noTowersException();

    }


    public void setTeam(Type team) {
        this.team = team;
    }

    public Type getTeam() {
        return team;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public ArrayList<Student> getHall() {
        return hall;
    }

    public Row getRow(Color color){
        for (Row r : rows){
            if(r.getName()==color){
                return r;
            }
        }
        //errore
        return null;
    }

    public Row getRow(String s){
        for(Row r:rows){
            if(r.getName().getText().equals(s)){
                return r;
            }
        }
        return null;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public String getOwner(){return owner;}
}
