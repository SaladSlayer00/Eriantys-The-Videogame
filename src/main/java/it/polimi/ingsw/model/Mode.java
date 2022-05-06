package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.Mage;

import java.util.ArrayList;
import java.util.List;


public interface Mode {

     public void initializeGameboard() throws noMoreStudentsException;
     public void initializePlayer(Player p);
     public void initializeDashboards();
     public void setDeck(Mage m , int playerID);
     public int getChosenPlayerNumber();
     public Gameboard getGameBoard();
     public int getNumCurrentPlayers();
     public int getNumCurrentActivePlayers();
     public boolean isNicknameTaken(String nickname);
     public List<Player> getPlayers();
     public List<Player> getActivePlayers();
     public List<String> getPlayersNicknames();
     public Player getPlayerByID(int playerID);
     public Player getPlayerByNickname(String name);
     public ArrayList<Cloud> getEmptyClouds();
     public void resetInstance();


     }
