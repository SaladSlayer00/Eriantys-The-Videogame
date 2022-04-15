package it.polimi.ingsw.model;


public interface Mode {
     public void startGame();
     public void initializeGameboard();
     public void getNumPlayers(int numPlayers);
     public void createPlayers();

}
