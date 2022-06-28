package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.LobbyMessage;
import it.polimi.ingsw.message.observation.BoardMessage;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.Observable;

/** This class represents the instance of the game,
 * contains the list of players, the chosen mode,
 * mostly getters and setters that will be used by the controller
 */

public class EasyGame extends Observable implements Serializable, Mode{
    private static final long serialVersionUID =  4405183481677036856L; //da cambiare
    private static EasyGame instance;
    public static final String SERVER_NICKNAME = "server";
    private  Gameboard gameBoard;
    private List<Player> players;
    private List<Player> activeplayers;
    private int chosenPlayerNumber;
    private List<Dashboard> dashboards ;
    private boolean noMoreStudents;
    private int numCurrentActivePlayers = 0;

    /**Constructor of game, takes the number of players as parameter
     *
     * @param numPlayers the number of players chosen
     */

    public EasyGame(int numPlayers) {
        this.chosenPlayerNumber = numPlayers;
        this.players = new ArrayList<>();
        this.activeplayers = new ArrayList<>();
        this.dashboards=new ArrayList<>();
        this.noMoreStudents= false;
    }

    /**
     * method that initializes the parameters of the gameboard according
     * to the player's input and sets it up
     * @throws noMoreStudentsException
     */
    public void initializeGameboard() throws noMoreStudentsException {
        this.gameBoard = new Gameboard(this.chosenPlayerNumber);
        this.gameBoard.getSack().initializeSack();
        this.gameBoard.initializeIslands();
        this.gameBoard.getSack().initializeSack();
        this.gameBoard.createClouds();


    }

    /**
     * method that inserts the player in the player list and creates its
     * dashboard
     * @param p the player to add
     */

    public void initializePlayer(Player p) {
        this.players.add(p);
        this.activeplayers.add(p);
        numCurrentActivePlayers=numCurrentActivePlayers+1;
        p.setDashboard(new Dashboard(this.chosenPlayerNumber,p.getName()));
        lobbyUpdate();
    }

    /**
     * method that initializes the player's dashboard adding students on it
     * from the sack
     * @throws maxSizeException if there's too many students
     */


    public void initializeDashboards() throws maxSizeException {
        for(Player p : this.players){
            for(int i = 0; i < p.getDashboard().getHallDimension(); i++){
                Student s = gameBoard.getSack().drawStudent();
                p.getDashboard().addToHall(s);
            }
            dashboards.add(p.getDashboard());
        }

    }

    /**
     * method that notifies every model observer of the updates made
     * by the controller
     */

    public void updateGameboard(){
        notifyObserver(new BoardMessage(gameBoard,dashboards,players));
    }

    /**
     * method that notifies every observer of the updates
     * caused by the connection of a new player
     */

    @Override
    public void lobbyUpdate(){
        notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayerNumber));
    }

    /**
     * persistence method, sets up the current parameters
     * with the ones from the save file that are passed by the controller
     * @param board
     * @param players
     * @param chosenPlayersNumber
     */

    @Override
    public void restoreGame(Gameboard board, List<Player> players, int chosenPlayersNumber) {
        this.gameBoard = board;
        this.players = players;
        this.chosenPlayerNumber = chosenPlayersNumber;
        dashboards = new ArrayList<>();
        for(Player p: players){
            dashboards.add(p.getDashboard());
        }
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

    public int getActives(){
        return this.numCurrentActivePlayers;
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
        instance=null;
    }

    @Override
    public boolean removePlayerByNickname(String nickname, boolean notifyEnabled) {
        boolean result = players.remove(getPlayerByNickname(nickname));
        if(notifyEnabled){
            notifyObserver(new LobbyMessage(getPlayersNicknames(),this.chosenPlayerNumber));
        }
        return result;
    }


    @Override
    public List<ExpertDeck> getExperts() {
        return gameBoard.getExperts();
    }


    @Override
    public void setActives(int number) {
        this.numCurrentActivePlayers=this.numCurrentActivePlayers+number;
    }



    public boolean getNoMoreStudents(){
        return this.noMoreStudents;
    }

    public void setNoMoreStudents(boolean ans){
        this.noMoreStudents=ans;
    }


    public static EasyGame getInstance() {
        if (instance == null)
            instance = new EasyGame(0);
        return instance;
    }


}

