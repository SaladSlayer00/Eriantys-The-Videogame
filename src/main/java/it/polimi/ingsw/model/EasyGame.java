package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.deckUnavailableException;
import it.polimi.ingsw.exceptions.invalidTeamException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Gameboard;


public class EasyGame implements Mode {

    private Gameboard gameBoard;
    private int playing;
    private  int playerNum;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playingList = new ArrayList<Player>();
    private Player currentPLayer;

    private int[][] teams = null;


    public EasyGame(int players){
        this.playerNum = players;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void initializeGameboard() throws noMoreStudentsException {
        this.gameBoard = new Gameboard(this.playerNum);
        this.gameBoard.initializeIslands();
    }

    @Override
    //lasciato in caso di disconnessione, non necessario per inizializzazione visto che si usa il costruttore
    public void getNumPlayers(int numPlayers) {
        this.playerNum = numPlayers;
    }

    public Gameboard getGameBoard() {
        return gameBoard;
    }

    public List<Player> getActivePlayers() {
        return playingList;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public Player getPlayerByID(int playerID){
        for (Player player : playingList) {
            if (player.getPlayerID() == playerID) {
                return player;
            }
        }
        return null;
    }

    public Player getPlayerByNickname(String name){
        for (Player player : playingList) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    //    @Override
//    public void nextState() {
//        this.gameState
//
//    }

//    @Override
//    public void createPlayers() {
//        for(int i = 0; i < this.playerNum; i++){
//            this.players.add(new Player());
//        }
//    }

    @Override
    //setter rimpiazzabili con un costruttore per dashboard
    public void initializePlayer(Player p) {
        this.players.add(p);
        this.playingList.add(p);
//        if(this.playerNum == 2){
//            p.getDashboard().setNumTowers(8);
//            p.getDashboard().setHallDimension(7);
//        }
//        else if(this.playerNum == 3){
//            p.getDashboard().setNumTowers(6);
//            p.getDashboard().setHallDimension(9);
//        }
    }

    public void setDeck(Mage m, int playerID) throws deckUnavailableException {
        for(Player p : this.players){
            if(p.getDeck().getMage().equals(m)){
                throw new deckUnavailableException();
            }
        }
        players.get(playerID).setDeck(m);
    }

    public void setTeams(int group, int playerID, int playerID2) throws invalidTeamException {
        if(group>2 || group<1){
            throw new invalidTeamException();
        }
        this.teams[group][0] = playerID;
        this.teams[group][1] = playerID2;
        this.players.get(playerID).getDashboard().setNumTowers(8);
        this.players.get(playerID2).getDashboard().setNumTowers(0);
    }
    public void initializeDashboards() throws noMoreStudentsException, maxSizeException {
        for(Player p : this.players){
            for(int i = 0; i < p.getDashboard().getHallDimension(); i++){
                Student s = gameBoard.getSack().drawStudent();
                p.getDashboard().addStudent(s);
            }
        }
    }

    public Player getCurrentPlayer() {
        return currentPLayer;
    }
}

