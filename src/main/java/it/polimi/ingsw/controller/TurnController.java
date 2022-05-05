package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.maxSizeException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.noStudentException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.enums.*;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

//si occupa della fase iniziale per settare l'ordine e richiama le azioni per la
//fase azione

public class TurnController implements Serializable {
    private static final long serialVersionUID = -5987205913389392005L; //non so che cazzo è
    private final Mode game;
    private final List<String> nicknameQueue;
    private String activePlayer;
    transient Map<String, VirtualView> virtualViewMap;
    private MainPhase mainPhase;
    private PhaseType phaseType;
    private final GameController gameController;
    private ArrayList<Assistant> chosen;
    private int moved = 0;

    public TurnController(Map<String, VirtualView> virtualViewMap, GameController gameController, Mode game) {
        this.game = game;
        this.nicknameQueue = new ArrayList<>(game.getPlayersNicknames());

        this.activePlayer = nicknameQueue.get(0); // set first active player
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
        this.mainPhase = MainPhase.PLANNING;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Set the active player.
     *
     * @param activePlayer the active Player to be set.
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }


    public void resetChosen(){
        chosen = new ArrayList<Assistant>();
    }

    public ArrayList<Assistant> getChosen() {
        return chosen;
    }

    public int getMoved() {
        return moved;
    }

    public void setMoved(int moved) {
        this.moved = moved;
    }

    /**
     * Set next active player.
     */
    public void next() {

        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
        phaseType = PhaseType.YOUR_MOVE;
    }


    public void setMainPhase(MainPhase turnMainPhase) {
        this.mainPhase = turnMainPhase;
    }


    /**
     * @return the current Phase Type.
     */
    public MainPhase getMainPhase() {
        return mainPhase;
    }

    public void setPhaseType(PhaseType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }


    /**
     * @return the current Phase Type.
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void newTurn() {
        setActivePlayer(nicknameQueue.get(0));
        turnControllerNotify("Turn of " + activePlayer, activePlayer);
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("Initiate the game! Pick your clouds. . .");

        StorageData storageData = new StorageData();
        storageData.store(gameController);

        setMainPhase(MainPhase.PLANNING);
        setPhaseType(PhaseType.ADD_ON_CLOUD);
        pickCloud();
    }


    //il player SCEGLIE LE CAZZO DI NUVOLE era pickpositions mi pare
    //i controlli sul valore valido penso li farà inputController...???
    public void pickCloud(){
        ArrayList<Cloud> cloudList;
        Player player = game.getPlayerByNickname(getActivePlayer());
        //lista che si passava come parametro per fare scegliere il player
        cloudList = game.getEmptyClouds();
        VirtualView virtualView = virtualViewMap.get(player);
        virtualView.askCloud(cloudList); //da chiedere sugli indici spacchettando?? non so sto metodo che fa
        //manderà un messaggio al player con la lista di disponibili booh poi vedremo
    }

    public void cloudInitializer(int cloudIndex) throws noMoreStudentsException {
        Cloud cloud = game.getGameBoard().getClouds()[cloudIndex];
        Sack sack = game.getGameBoard().getSack();
        int var;
        if(game.getChosenPlayerNumber()==2 || game.getChosenPlayerNumber() == 4){
            var = 3;
        }
        else{
            var = 4;
        }
        for(int i = 0; i < var; var++){
            cloud.addStudent(sack.drawStudent());
        }
    }
    //passa quelle da non mettere
    public void drawAssistant(){
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("Please choose which card to draw!");
        Player player = game.getPlayerByNickname(getActivePlayer());
        //lista che si passava come parametro per fare scegliere il player
        vv.askAssistant(chosen);
    }

    public void determineOrder(){
        Vector<Integer> order = new Vector<Integer>();
        int i = 0;
        for(Assistant a : chosen){
            order.set(i, a.getNumOrder());
            i++;
        }
        Collections.sort(order);
        for(Player p : game.getActivePlayers()){
            for(i=0;i< game.getNumCurrentActivePlayers(); i++){
                if(p.getCardChosen().getNumOrder()==order.get(i)){
                    this.nicknameQueue.set(i, p.getName());
                }
            }
        }
        this.resetChosen();
        mainPhase = MainPhase.ACTION;

    }

    public void moveMaker(){
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("You have moved "+ moved + " students!");
        vv.showGenericMessage("Please choose a student and where do you want to move it!");
        //lista che si passava come parametro per fare scegliere il player
        vv.askMoves();
    }

    //in effetti non ha senso il colore è lo stesso
    public void moveOnBoard(Color color, Color row) throws noStudentException, maxSizeException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        VirtualView vv = virtualViewMap.get(player);
        player.getDashboard().getRow(row).addStudent(player.getDashboard().takeStudent(color));
        moved++;

    }

    public void moveOnIsland(Color color, int index) throws noStudentException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        VirtualView vv = virtualViewMap.get(player);
        game.getGameBoard().placeStudent(color, player.getDashboard().takeStudent(color), index);
        moved++;
    }

    public void moveMother(int moves) throws noTowerException {
        int actual = game.getGameBoard().getMotherNature();
        game.getGameBoard().getIslands().get(actual).removeMother();
        for(int i = 0; i < moves; i++){
            if(i+actual == game.getGameBoard().getIslands().size()-1){
                actual = 0;
            }
            else {
                actual=actual+i;
            }
        }

        game.getGameBoard().setMotherNature(actual);
        checkInfluence(actual);
    }

    private void checkInfluence(int actual) throws noTowerException {
        Player player = game.getPlayerByNickname(activePlayer);
        Type team = player.getDashboard().getTeam();
        Island active = game.getGameBoard().getIslands().get(actual);
        int influence = 0;
        for(Color c : player.getProfessors()){
            influence = influence + active.getStudents().get(c).size();
        }
        //non mi ricordo come funziona il numero di torri
        if(active.getTower()) {
            if (active.getTeam().equals(team)) {
                influence = influence + active.getDimension();
            }
        }
        if(influence > active.getInfluence()){
            active.setInfluence(influence);
            active.setTower(team);
            vv.showGenericMessage("The island is yours!");
            islandMerger(active);
        }

    }

    public void islandMerger(Island active) throws noTowerException {
        ArrayList<Island> islands = game.getGameBoard().getIslands();
        Island before;
        Island after;
        if(active.getIndex()==0){
            before=islands.get(islands.size()-1);
            after = islands.get(1);
        }else if(active.getIndex()==islands.size()-1){
            after=islands.get(0);
            before = islands.get(active.getIndex()-1);
        }
        else{
            before = islands.get(active.getIndex()-1);
            after= islands.get(active.getIndex()+1);
        }
        if(before.getTower()) {
            if (before.getTeam().equals(active.getTeam())) {
                active.changeDimension(before.getDimension());
                for(Color c : before.getStudents().keySet()){
                    active.getStudents().get(c).addAll(before.getStudents().get(c));
                }
                islands.remove(before);
            }
        }
        if(after.getTower()) {
            if (after.getTeam().equals(active.getTeam())) {
                active.changeDimension(after.getDimension());
                for(Color c : after.getStudents().keySet()){
                    active.getStudents().get(c).addAll(after.getStudents().get(c));
                }
                islands.remove(after);
            }
        }

    }



}
