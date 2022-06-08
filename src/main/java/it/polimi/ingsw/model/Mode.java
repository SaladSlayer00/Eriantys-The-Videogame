package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.Mage;

import java.util.ArrayList;
import java.util.List;


public interface Mode {

     public void initializeGameboard() throws noMoreStudentsException;
     public void initializePlayer(Player p);
     public void initializeDashboards() throws maxSizeException;
     public int getChosenPlayerNumber();
     public Gameboard getGameBoard();
     public int getNumCurrentPlayers();
     public int getNumCurrentActivePlayers();
     public boolean isNicknameTaken(String nickname);
     public List<Player> getPlayers();
     public List<Player> getActivePlayers();
     public List<String> getPlayersNicknames();
     public void updateGameboard();
     public Player getPlayerByID(int playerID);
     public Player getPlayerByNickname(String name);
     public ArrayList<Cloud> getEmptyClouds();
     public void resetInstance();
     public void removePlayerByNickname(String nickname, boolean notifyEnabled);
     public void restoreGame(Gameboard board, List<Player> players, List<Character> carteEsperto, int chosenPlayerNumber);
     public List<Character> getExperts();
     public boolean getNoMoreStudents();
     public void setNoMoreStudents(boolean ans);
}
