package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Type;


import java.util.ArrayList;

//The following class represents the player's dashboard
public class Dashboard {
    //attributes of the class dashboard
    private int towers;
    private int numTowers;
    private final Row[] rows = new Row[NUM_ROWS];
    private final ArrayList<Student> hall = new ArrayList<>();
    private Type team;
    private static final int NUM_ROWS = 5;
    private int hallDimension;

    public void setHallDimension(int hallDimension) {
        this.hallDimension = hallDimension;
    }

    public int getHallDimension() {
        return hallDimension;
    }

    //methods of the class dashboard
    public void addToHall(Student s) {
        hall.add(s);
    }

    public void addStudent(Student student) throws maxSizeException {
        /*ALTERNATIVE VERSION
        for (int i = 0; i < NUM_ROWS; i++) {
            if (rows[i].getName().equals(student.color))
                rows[i].addStudent(student);
        }
        */
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

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public void setTeam(Type team) {
        this.team = team;
    }

    public int getNumTowers() {
        return numTowers;
    }
}
