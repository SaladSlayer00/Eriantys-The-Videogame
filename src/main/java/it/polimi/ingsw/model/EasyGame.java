package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Vector;
import it.polimi.ingsw.model.Gameboard;

public class EasyGame implements Mode {

    private Gameboard gameBoard;
    private int playing;
    private  int playerNum;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playingList = new ArrayList<Player>();
    private GameState gameState;
    private int[][] teams = null;


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
        this.players.get(playerID).setName(nickname);
        this.players.get(playerID).setPlayerID(playerID);
        if(this.playerNum == 2){
            this.players.get(playerID).getDashboard().setNumTowers(8);
        }
        else if(this.playerNum == 3){
            this.players.get(playerID).getDashboard().setNumTowers(6);
        }
    }

    public void setTeams(int group, int playerID, int playerID2) throws invalidTeamException{
        if(group>2||group<1){
            throw new invalidTeamException();
        }
        this.teams[group][0] = playerID;
        this.teams[group][1] = playerID2;
        this.players.get(playerID).getDashboard().setNumTowers(8);
        this.players.get(playerID2).getDashboard().setNumTowers(0);
    }

}

