package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Gameboard;

import java.nio.ByteBuffer;
import java.util.List;


public interface Mode {
     public void initializeGameboard() throws noMoreStudentsException;
     public void getNumCurrentPlayers();
     public void initializePlayer(Player p);
     public Gameboard getGameBoard();
     public Player getPlayerByID(int playerID);
     public Player getPlayerByNickname(String nickname);
     public List<Player> getActivePlayers();
     public List<Player> getPlayers();
     public int getChosenPlayersNumber();
     public List<String> getPlayersNicknames();
     public int getNumCurrentActivePlayers();

     }
