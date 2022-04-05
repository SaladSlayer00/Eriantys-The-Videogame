package it.polimi.ingsw.model;


public interface Mode {
     public void startGame();
     public void inizializeGameboard();
     public void getNumPlayers(int numPlayers);
     public void nextState();
     public void createPlayers();
     public void startTurn();

}
