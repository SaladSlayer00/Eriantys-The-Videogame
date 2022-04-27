package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Gameboard;

import java.nio.ByteBuffer;
import java.util.List;


public interface Mode {

     public void intializeGameboard() throws noMoreStudentsException;
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

     }
