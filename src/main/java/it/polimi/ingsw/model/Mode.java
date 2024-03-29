package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.ExpertDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * enumm for the different methods callable from the game classes
 * that implement this interface
 */

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
     public boolean removePlayerByNickname(String nickname, boolean notifyEnabled);
     public void restoreGame(Gameboard board, List<Player> players, int chosenPlayerNumber);
     public List<ExpertDeck> getExperts();
     public boolean getNoMoreStudents();
     public void setNoMoreStudents(boolean ans);
     void lobbyUpdate();
     void setActives(int number);
     int getActives();
}
