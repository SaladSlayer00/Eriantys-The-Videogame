package it.polimi.ingsw.model;


public interface Mode {
     public void startGame();
     public void initializeGameboard() throws Gameboard.Sack.NoMoreStudentsException;
     public void getNumPlayers(int numPlayers);
     public void createPlayers();
     void initializePlayer(String nickname, int playerID);
}
