package it.polimi.ingsw.model;
import java.util.ArrayList;


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

    @Override
    public void nextState() {

    }

    @Override
    public void createPlayers() {

    }


}
