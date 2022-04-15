package it.polimi.ingsw.model;
import java.util.ArrayList;
import it.polimi.ingsw.model.Gameboard;

public class EasyGame implements Mode {

    private Gameboard gameBoard;
    private int playing;
    private  int playerNum;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playingList = new ArrayList<Player>();
    private GameState gameState;

    public EasyGame(int players){
        this.playerNum = players;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void initializeGameboard() {
        this.gameBoard = new Gameboard(this.playerNum);
    }

    @Override
    public void getNumPlayers(int numPlayers) {
        this.playerNum = numPlayers;
    }

    @Override
    public void nextState() {

    }

    @Override
    public void createPlayers() {
        for(int i = 0; i< this.playerNum; i++){
            this.players.add(new Player());
        }
    }

    @Override
    public void initializePlayer(String nickname, int playerID) {


    }
}