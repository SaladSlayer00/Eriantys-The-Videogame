package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.GenericMessage;
import it.polimi.ingsw.message.PlayerNumberReply;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.utils.StorageData;
import it.polimi.ingsw.view.VirtualView;

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
    private ArrayList<Assistant> chosen = new ArrayList<>();
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


    //TODO implementare correttamente lo show match info
    public void broadcastMatchInfo() {

        for (VirtualView vv : virtualViewMap.values()) {
            vv.showMatchInfo(game.getChosenPlayerNumber(), game.getNumCurrentActivePlayers());
        }
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

    public void newTurn() throws noMoreStudentsException {
        setActivePlayer(nicknameQueue.get(0));
        turnControllerNotify("Turn of " + activePlayer, activePlayer);
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("Initiate the game! Pick your clouds. . .");

        //StorageData storageData = new StorageData();
        //storageData.store(gameController);

        setMainPhase(MainPhase.PLANNING);
        setPhaseType(PhaseType.ADD_ON_CLOUD);
        //cloudInitializer(0);
        //cloudInitializer(1);
        //vv.showGenericMessage("Cloud 1 "+ game.getGameBoard().getCloud(0).getStudents().size());
        //vv.showGenericMessage("Cloud 1 "+ game.getGameBoard().getCloud(1).getStudents().size());
        pickCloud();
        //drawAssistant();
    }


    public void turnControllerNotify(String messageToNotify, String excludeNickname) {
        virtualViewMap.values().forEach(vv -> vv.showMatchInfo(game.getChosenPlayerNumber(), game.getNumCurrentPlayers()));
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }



    //il player SCEGLIE LE CAZZO DI NUVOLE era pickpositions mi pare
    //i controlli sul valore valido penso li farà inputController...???
    public void pickCloud(){
        //lista che si passava come parametro per fare scegliere il player
        ArrayList<Cloud> cloudList = game.getEmptyClouds();
        VirtualView virtualView = virtualViewMap.get(activePlayer);
        virtualView.showGenericMessage("Prova a chiedere la nuvola");
        virtualView.askCloud(activePlayer,cloudList); //da chiedere sugli indici spacchettando?? non so sto metodo che fa
        //manderà un messaggio al player con la lista di disponibili booh poi vedremo
    }

    public void cloudInitializer(int cloudIndex) throws noMoreStudentsException {
        Cloud cloud = game.getGameBoard().getClouds().get(cloudIndex);
        Sack sack = game.getGameBoard().getSack();
        int var;
        if(game.getChosenPlayerNumber()==2 || game.getChosenPlayerNumber() == 4){
            var = 3;
        }
        else{
            var = 4;
        }

        for(int i = 0; i < var; i++){
           Student s = sack.drawStudent();
           for(VirtualView virtualView: virtualViewMap.values())
               virtualView.showGenericMessage(s.getColor() +" Student on cloud n° "+ cloudIndex);
            if(s == null){
                game.setNoMoreStudents(true);
                break;
            }
            cloud.addStudent(s);
        }

    }
    //passa quelle da non mettere
    public void drawAssistant(){
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("Please choose which card to draw!");
        //lista che si passava come parametro per fare scegliere il player
        vv.askAssistant(activePlayer,chosen);
    }

    public void determineOrder(){
//        Vector<Integer> order = new Vector<Integer>();
//        int i = 0;
//        for(Assistant a : chosen){
//            order.set(i, a.getNumOrder());
//            i++;
//        }
//        Collections.sort(order);
//        for(Player p : game.getActivePlayers()){
//            for(i=0;i< game.getNumCurrentActivePlayers(); i++){
//                if(p.getCardChosen().getNumOrder()==order.get(i)){
//                    this.nicknameQueue.set(i, p.getName());
//                }
//            }
//        }

        /*
        for(Player p : game.getPlayers()) {
            if (chosen.isEmpty()) {
                nicknameQueue.add(p.getName());
            } else {

                for (int i = 0; i < chosen.size(); i++) {
                    if (p.getCardChosen().getNumOrder() > game.getPlayerByNickname(nicknameQueue.get(i)).getCardChosen().getNumOrder()) {
                        nicknameQueue.add(i, p.getName());
                    }
                }
            }
        }
         */
        if(chosen.isEmpty()){
            for(Player p : game.getPlayers()) {
                nicknameQueue.add((p.getName()));}
            }else{
            Collections.sort(chosen, (o1, o2) -> Integer.valueOf(o1.getNumOrder()).compareTo(o2.getNumOrder()));
            for(Player player: game.getPlayers()){
                for(Assistant a : chosen){
                    if(player.getCardChosen().getNumOrder()==a.getNumOrder()){
                        nicknameQueue.set(chosen.indexOf(a),player.getName());
                    }
                }
            }
        }

        for(VirtualView virtualView : virtualViewMap.values()){
            virtualView.showGenericMessage("Order : ");
            for(String name : nicknameQueue){
                virtualView.showGenericMessage("-> " + nicknameQueue.size());
                virtualView.showGenericMessage("-> " + name);
            }
        }
        setActivePlayer(nicknameQueue.get(0));
        this.resetChosen();
        mainPhase = MainPhase.ACTION;

    }

    public void moveMaker(){
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("You have moved "+ moved + " students!");
        vv.showGenericMessage("Please choose a student and where do you want to move it!");
        //lista che si passava come parametro per fare scegliere il player
        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
    }

    //in effetti non ha senso il colore è lo stesso
    public void moveOnBoard(Color color, Color row) throws noStudentException, maxSizeException,  emptyDecktException, noMoreStudentsException, fullTowersException, noTowerException, invalidNumberException, noTowersException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        VirtualView vv = virtualViewMap.get(player);
        player.getDashboard().addStudent(player.getDashboard().takeStudent(color));
        checkProfessors(color);
        moved++;
    }

    public void moveOnIsland(Color color, int index) throws noStudentException, noTowerException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        game.getGameBoard().placeStudent(color, player.getDashboard().takeStudent(color), index);
        moved++;
    }

    public int moveMother(int moves) throws noTowerException, noTowersException {

        int actual = game.getGameBoard().getMotherNature();
        virtualViewMap.get(activePlayer).showGenericMessage("Mother nature on: "+actual);
        game.getGameBoard().getIslands().get(actual).removeMother();
        for(int i = 0; i < moves; i++){
            if(actual >= game.getGameBoard().getIslands().size()-1){
                actual = 0;
            }
            else {
                actual=actual+1;
            }
        }
        game.getGameBoard().setMotherNature(actual);
        virtualViewMap.get(activePlayer).showGenericMessage("Mother nature on: "+actual);
        return checkInfluence(actual);
    }

    private int checkInfluence(int actual) throws noTowerException, noTowersException {
        Player player = game.getPlayerByNickname(activePlayer);
        Type team = player.getDashboard().getTeam();
        VirtualView vv = virtualViewMap.get(activePlayer);
        Island active = game.getGameBoard().getIslands().get(actual);
        int set = 0;
        int influence = 0;
        for(Color c : player.getProfessors()){
            influence = influence + active.getStudents().get(c).size();
        }

        if(active.getTower()) {
            if (active.getTeam().equals(team)) {
                influence = influence + active.getDimension();
            }
        }

        for(Player p : game.getPlayers()){
            int influenceOther = 0;
            for(Color c : p.getProfessors()){
                influenceOther = influenceOther + active.getStudents().get(c).size();
            }

            if(active.getTower()) {
                if (active.getTeam().equals(p.getDashboard().getTeam())) {
                    influenceOther = influenceOther + active.getDimension();
                }
            }
            if(influence > influenceOther){
                set = 1;
            }
        }
        if(set==1){
            active.setInfluence(influence);
            active.setTower(player.getDashboard().getTower());
            vv.showGenericMessage("The island is yours!");
            return towerChecker();
            //islandMerger(active);
        }
        else{
            return 0;
        }

    }

    private void checkProfessors(Color color) throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
        Player chosenPlayer = gameController.getGame().getPlayerByNickname(activePlayer);
        boolean draw = false;
        for (Player p : game.getPlayers()) {
            if (chosenPlayer.getDashboard().getRow(color).getNumOfStudents() < p.getDashboard().getRow(color).getNumOfStudents()) {
                chosenPlayer = p;
            }
        }
        try {
            chosenPlayer.getDashboard().getRow(color).addProfessor();
            chosenPlayer.getProfessors().add(color);
        } catch (alreadyAProfessorException e) {
            gameController.onMessageReceived(new GenericMessage("Already a professor"));
        } finally {
            game.getGameBoard().removeProfessor(color);
        }

        for (Player p : game.getPlayers()) {
            if ((!chosenPlayer.equals(p)) && chosenPlayer.getDashboard().getRow(color).getNumOfStudents() == p.getDashboard().getRow(color).getNumOfStudents()) {
                try {
                    chosenPlayer.getDashboard().getRow(color).removeProfessor();
                    chosenPlayer.getProfessors().remove(color);

                } catch (noProfessorException e) {
                }
                try {
                    p.getDashboard().getRow(color).removeProfessor();
                    p.getProfessors().remove(color);
                } catch (noProfessorException exp) {
                }

                if (!draw) {
                    game.getGameBoard().addProfessor(color);
                    draw = true;

                }

            }
        }
    }




    public int towerChecker(){
        Player p = game.getPlayerByNickname(activePlayer);
        if(p.getDashboard().getNumTowers()==0){
            return 2;
        }
        return 1;
    }

    public void islandMerger(Island active) throws noTowerException {
        game.getGameBoard().mergeIslands(active);
//        Island before;
//        Island after;
//        if(active.getIndex()==0){
//            before=islands.get(islands.size()-1);
//            after = islands.get(1);
//        }else if(active.getIndex()==islands.size()-1){
//            after=islands.get(0);
//            before = islands.get(active.getIndex()-1);
//        }
//        else{
//            before = islands.get(active.getIndex()-1);
//            after= islands.get(active.getIndex()+1);
//        }
//        if(before.getTower()) {
//            if (before.getTeam().equals(active.getTeam())) {
//                active.changeDimension(before.getDimension());
//                for(Color c : before.getStudents().keySet()){
//                    active.getStudents().get(c).addAll(before.getStudents().get(c));
//                }
//                islands.remove(before);
//                if(active.getIndex()!=0) {
//                    for (int i = active.getIndex(); i < islands.size(); i++) {
//                        islands.get(i).setIndex(i - 1);
//                    }
//                }
//            }
//        }
//        if(after.getTower()) {
//            if (after.getTeam().equals(active.getTeam())) {
//                active.changeDimension(after.getDimension());
//                for(Color c : after.getStudents().keySet()){
//                    active.getStudents().get(c).addAll(after.getStudents().get(c));
//                }
//                islands.remove(after);
//                if(active.getIndex()==islands.size()-1){
//                    for(int i = 1;i<islands.size();i++){
//                        //secondo me gli indici non servono
//                    }
//                }
//                for(int i = active.getIndex()+1;i < islands.size()-1;i++){
//                    if(i==0){
//                        active.setIndex(islands.size()-1);
//                    }
//                    else {
//                        islands.get(i).setIndex(i - 1);
//                    }
//                }
//            }
//        }

    }



    public void getFromCloud(int index){
        Player p = game.getPlayerByNickname(activePlayer);
        ArrayList<Student> students = game.getGameBoard().getClouds().get(index).removeStudents();
        for(Student s : students){
            p.getDashboard().addToHall(s);
        }
    }


    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }

    public void setVirtualViewMap(Map<String, VirtualView> virtualViewMap) {
        this.virtualViewMap = virtualViewMap;
    }
}
