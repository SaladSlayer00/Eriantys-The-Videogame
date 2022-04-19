package it.polimi.ingsw.model.test;

import it.polimi.ingsw.model.Mage;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//this should be the class that allows to test the class Player
public class PlayerTest{

    Player playert;
    private static final int playerID = 0;
    private static final String name = "ptest";
    String chosenMage = "fairy";

    //initial set up
    @BeforeEach
    void startingsetup(){
        playert = new Player();
        playert.setPlayerID(playerID);
        playert.setName(name);

    }




}
