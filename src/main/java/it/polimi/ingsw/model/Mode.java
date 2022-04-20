package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.noMoreStudentsException;


public interface Mode {
     public void startGame();
     public void initializeGameboard() throws noMoreStudentsException;
     public void getNumPlayers(int numPlayers);
     //public void createPlayers();
     public void initializePlayer(Player p);
     public Gameboard getGameBoard();
     public Player getPlayerByID(int playerID);
}
