package it.polimi.ingsw.model;

public class ExpertGame implements Mode{
    private final Gameboard gameBoard;
    private int playing;
    private  int playerNum;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playingList = new ArrayList<Player>();
    private Turn turn;
    private GameState gameState;

    @Override
    public void startGame() {

    }

    @Override
    public void inizializeGameboard() {

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

    @Override
    public void startTurn() {

    }
}
