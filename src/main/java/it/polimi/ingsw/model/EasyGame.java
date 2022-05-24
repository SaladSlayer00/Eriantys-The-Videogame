package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.observation.BoardMessage;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.Observable;


public class EasyGame extends Observable implements Serializable, Mode{
    private static final long serialVersionUID =  4405183481677036856L; //da cambiare
    private static EasyGame instance;
    public static final String SERVER_NICKNAME = "server";
    private  Gameboard gameBoard;
    private List<Player> players;
    private List<Player> activeplayers;
    private int chosenPlayerNumber;
    private List<Dashboard> dashboards;


    public EasyGame(int numPlayers) {
        this.chosenPlayerNumber = numPlayers;
        this.players = new ArrayList<>();
        this.activeplayers = new ArrayList<>();
        this.dashboards=new ArrayList<>();
    }

    public void initializeGameboard() throws noMoreStudentsException {
        this.gameBoard = new Gameboard(this.chosenPlayerNumber);
        this.gameBoard.initializeIslands();

    }

    public void initializePlayer(Player p) {
        this.players.add(p);
        this.activeplayers.add(p);
        p.setDashboard(new Dashboard(this.chosenPlayerNumber));
    }

    public void initializeDashboards() throws maxSizeException {
        for(Player p : this.players){
            for(int i = 0; i < p.getDashboard().getHallDimension(); i++){
                Student s = gameBoard.getSack().drawStudent();
                p.getDashboard().addStudent(s);
            }
            dashboards.add(p.getDashboard());
        }

        updateGameboard();
    }

    public void updateGameboard(){
        notifyObserver(new BoardMessage(gameBoard,dashboards));
    }

    public int getChosenPlayerNumber() {
        return this.chosenPlayerNumber;
    }

    public Gameboard getGameBoard(){
        return this.gameBoard;
    }

    public Player getPlayerByNickname(String name){
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public int getNumCurrentActivePlayers() {
        return activeplayers.size();

    }
    public int getNumCurrentPlayers() {
        return players.size();
    }

    public boolean isNicknameTaken(String nickname) {
        return players.stream().anyMatch(p->nickname.equals((p.getName())));
    }

    public List<Player> getPlayers() {
        return players;
    }
    public List<Player> getActivePlayers() {
        return activeplayers;
    }

    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getName());
        }
        return nicknames;
    }
    public Player getPlayerByID(int playerID){
        for(Player player : activeplayers) {
            if(player.getPlayerID() == playerID){
                return player;
            }
        }
        return null;
    }

    public ArrayList<Cloud> getEmptyClouds(){
        ArrayList<Cloud> result = new ArrayList<Cloud>();
        for(Cloud c : this.getGameBoard().getClouds()){
            if(c.emptyCloud()){
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public void resetInstance() {

    }

    @Override
    public void removePlayerByNickname(String nickname, boolean notifyEnabled) {

    }

    @Override
    public void restoreGame(Gameboard board, List<Player> players, List<Character> carteEsperto, int chosenPlayerNumber) {

    }

    @Override
    public List<Character> getExperts() {
        return null;
    }

//    public void restoreGame(Gameboard board, List<Player> players, List<God> gods, int chosenPlayersNumber) {
//        this.getGameBoard().restoreBoard(board.getSpaces());
//        this.players = players;
//        this.gods = gods;
//        this.chosenPlayerNumber = chosenPlayersNumber;
//    }

//    public void initializeDashboardsTower() throws fullTowersException {
//        for(Player p : players){
//            for(int i = 0 ; i < p.getDashboard().getNumTowers(); i++){
//                p.getDashboard().putTower();
//            }
//        }
//    }
}

