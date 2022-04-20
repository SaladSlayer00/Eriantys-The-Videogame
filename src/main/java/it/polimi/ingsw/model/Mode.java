package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.noMoreStudentsException;


public interface Mode {
     public void startGame();
     public void initializeGameboard() throws noMoreStudentsException;
     public void getNumPlayers(int numPlayers);
     //public void createPlayers();
     void initializePlayer(Player p);

     Gameboard getGameBoard();
}
