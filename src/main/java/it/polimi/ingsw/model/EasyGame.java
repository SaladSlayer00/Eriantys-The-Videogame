package it.polimi.ingsw.model;
import java.util.ArrayList;

public class EasyGame implements Mode {

    private Gameboard gameBoard;
    private int playing;
    private  int playerNum;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playingList = new ArrayList<Player>();
    //private GameState gameState; secondo me non necessario
    private int[][] teams = null;


    public EasyGame(int players){
        this.playerNum = players;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void initializeGameboard() throws Gameboard.Sack.NoMoreStudentsException {
        this.gameBoard = new Gameboard(this.playerNum);
        this.gameBoard.initializeIslands();
    }

    @Override
    //lasciato in caso di disconnessione, non necessario per inizializzazione visto che si usa il costruttore
    public void getNumPlayers(int numPlayers) {
        this.playerNum = numPlayers;
    }

//    @Override
//    public void nextState() {
//        this.gameState
//
//    }

    @Override
    public void createPlayers() {
        for(int i = 0; i< this.playerNum; i++){
            this.players.add(new Player());
        }
    }

    @Override
    //setter rimpiazzabili con un costruttore per dashboard
    public void initializePlayer(String nickname, int playerID) {
        this.players.get(playerID).setName(nickname);
        this.players.get(playerID).setPlayerID(playerID);
        if(this.playerNum == 2){
            this.players.get(playerID).getDashboard().setNumTowers(8);
            this.players.get(playerID).getDashboard().setHallDimension(7);
        }
        else if(this.playerNum == 3){
            this.players.get(playerID).getDashboard().setNumTowers(6);
            this.players.get(playerID).getDashboard().setHallDimension(9);
        }
    }

    public void setDeck(Mage m, int playerID) throws deckUnavailableException{
        for(Player p : this.players){
            if(p.getDeck().getMage().equals(m)){
                throw new deckUnavailableException();
            }
        }
        players.get(playerID).setDeck(m);
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
    public void initializeDashboards() throws Gameboard.Sack.NoMoreStudentsException, MaxSizeException {
        for(Player p : this.players){
            for(int i = 0;i<p.getDashboard().getHallDimension();i++){
                Student s = gameBoard.getSack().drawStudent();
                p.getDashboard().addStudent(s);
            }
        }
    }
}

