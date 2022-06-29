package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.enums.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * game instance for the expert version of Eriantys
 */

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

    /*
    @Override
    public void startGame() {

    }
    */


    @Override
    public void initializeGameboard() {

    }

    /*
    @Override
    public void getNumPlayers(int numPlayers) {

    }
    */

    public void nextState() {

    }
/*
    @Override
    public void createPlayers() {

    }

 */

    @Override
    public void initializePlayer(Player p) {

    }

    @Override
    public void initializeDashboards() throws maxSizeException {

    }

    @Override
    public int getChosenPlayerNumber() {
        return 0;
    }

    @Override
    public Gameboard getGameBoard() {
        return null;
    }

    @Override
    public int getNumCurrentPlayers() {
        return 0;
    }

    @Override
    public int getNumCurrentActivePlayers() {
        return 0;
    }

    @Override
    public boolean isNicknameTaken(String nickname) {
        return false;
    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public Player getPlayerByID(int playerID) {
        return null;
    }

    @Override
    public Player getPlayerByNickname(String nickname) {
        return null;
    }

    @Override
    public ArrayList<Cloud> getEmptyClouds() {
        return null;
    }

    @Override
    public void resetInstance() {

    }

    @Override
    public boolean removePlayerByNickname(String nickname, boolean notifyEnabled) {

        return notifyEnabled;
    }

    @Override
    public void restoreGame(Gameboard board, List<Player> players, int chosenPlayerNumber) {

    }

    @Override
    public List<ExpertDeck> getExperts() {
        return null;
    }

    @Override
    public boolean getNoMoreStudents() {
        return false;
    }

    @Override
    public void setNoMoreStudents(boolean ans) {

    }

    @Override
    public void lobbyUpdate() {

    }

    @Override
    public void setActives(int number) {

    }

    @Override
    public int getActives() {
        return 0;
    }

    @Override
    public List<Player> getActivePlayers() {
        return null;
    }

    @Override
    public List<String> getPlayersNicknames() {
        return null;
    }

    @Override
    public void updateGameboard() {

    }


}
