package it.polimi.ingsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.deckUnavailableException;
import it.polimi.ingsw.exceptions.invalidTeamException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.board.Gameboard;


public class EasyGame extends Oservable implements Serializable{
    private static final long serialVersioneUID =  4405183481677036856L; //da cambiare
    private static EasyGame instance;
    private static final String SERVER_NICKNAME = "server";
    private final Gameboard gameBoard;
    private List<Player> players;
    private List<Player> activeplayers;
    private int chosenPlayerNumber;



    public EasyGame(int numPlayers) {
        this.chosenPlayerNumber = numPlayers;
        this.gameBoard = new Gameboard(numPlayers);
        this.players = new ArrayList<>();
        this.activeplayers = new ArrayList<>();
    }
    public void initIslands() throws noMoreStudentsException {
        this.gameBoard.initializeIslands();
    }

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
    public List<Player> getActiveplayers() {
        return activeplayers;
    }

    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getNickname());
        }
        return nicknames;
    }

}

