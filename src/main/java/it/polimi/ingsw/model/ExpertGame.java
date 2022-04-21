package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;


public class ExpertGame implements Mode{
    private Gameboard gameBoard;
    private int playing;
    private  int playerNum;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playingList = new ArrayList<Player>();
    private GameState gameState;

    public ExpertGame(int players){
        this.playerNum = players;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void initializeGameboard() {

    }

    @Override
    public void getNumPlayers(int numPlayers) {

    }


    public void nextState() {

    }
/*
    @Override
    public void createPlayers() {

    }

 */

    @Override
    public void initializePlayer(Player p) {

    }

    @Override
    public Gameboard getGameBoard() {
        return null;
    }

    @Override
    public Player getPlayerByID(int playerID) {
        return null;
    }

    @Override
    public Player getPlayerByNickname(String nickname) {
        return null;
    }

    @Override
    public List<Player> getActivePlayers() {
        return null;
    }


}
