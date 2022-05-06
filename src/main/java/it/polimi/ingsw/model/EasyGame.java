package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.deckUnavailableException;
import it.polimi.ingsw.exceptions.invalidTeamException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.playerBoard.Dashboard;


public class EasyGame extends Oservable implements Serializable{
    private static final long serialVersioneUID =  4405183481677036856L; //da cambiare
    private static EasyGame instance;
    public static final String SERVER_NICKNAME = "server";
    private  Gameboard gameBoard;
    private List<Player> players;
    private List<Player> activeplayers;
    private int chosenPlayerNumber;



    public EasyGame(int numPlayers) {
        this.chosenPlayerNumber = numPlayers;
        this.players = new ArrayList<>();
        this.activeplayers = new ArrayList<>();
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

    public void initializeDashboards() throws noMoreStudentsException, maxSizeException {
        for(Player p : this.players){
            for(int i = 0; i < p.getDashboard().getHallDimension(); i++){
                Student s = gameBoard.getSack().drawStudent();
                p.getDashboard().addStudent(s);
            }
        }
    }
//    public void setDeck(Mage m , int playerID) throws deckUnavailableException{
//        for(Player p : this.players){
//            if(p.getDeck().getMage().equals(m));
//            throw new deckUnavailableException();
//        }
//        players.get(playerID).setDeck(m);
//    }

    public int getChosenPlayersNumber() {
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


}

