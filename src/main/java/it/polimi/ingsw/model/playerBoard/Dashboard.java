package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Type;


import java.util.ArrayList;

//The following class represents the player's dashboard
public class Dashboard {
    //attributes of the class dashboard
    private int towers;
    private int numTowers;
    private final ArrayList<Row> rows = new ArrayList<>();
    private final ArrayList<Student> hall = {new Row(Color.BLUE), new Row(Color.GREEN), new Row(Color.RED), new Row(Color.PINK), new Row(Color.YELLOW)};
    private Type team;
    //private static final int NUM_ROWS = 5;
    private int hallDimension;

    public Dashboard(int numPlayers){
        if(numPlayers == 2){
            this.numTowers = 8;
            this.hallDimension = 7;
        }else if(numPlayers == 3){
            this.numTowers = 6;
            this.hallDimension = 9;
        }
        this.rows = {};
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
        if (towers < numTowers) {
            towers++;
        } else {
            throw new fullTowersException();
        }
    }

    public Type getTower() throws noTowersException {
        if (towers > 0) {
            towers--;
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
        return this.rows
    }



}
