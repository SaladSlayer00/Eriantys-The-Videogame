package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.expertDeck.*;
import it.polimi.ingsw.controller.expertDeck.Character;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.EffectMessage;
import it.polimi.ingsw.message.GenericMessage;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.controller.expertDeck.*;
import it.polimi.ingsw.utils.StorageData;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.*;

/**
 * class that handles every mechanic of the game's turn, including calls to the views
 * it returns and takes values from the gameController
 */

public class TurnController implements Serializable {
    private static final long serialVersionUID = -5987205913389392005L;
    private  Mode game;
    private List<String> nicknameQueue;
    private String activePlayer;
    transient Map<String, VirtualView> virtualViewMap;
    private MainPhase mainPhase;
    private PhaseType phaseType;
    private GameController gameController;
    private ArrayList<Assistant> chosen = new ArrayList<>();
    private int moved = 0;
    private List<Character> toReset = new ArrayList<>();
    private List<Color> banned = new ArrayList<>();
    private Map<ExpertDeck, Integer> price = new HashMap<>();
    private int restored = 0;


    /**
     * Class constructor
     *
     * @param virtualViewMap map of virtual views for the players
     * @param gameController the gameController
     * @param game the game instance
     */
    public TurnController(Map<String, VirtualView> virtualViewMap, GameController gameController, Mode game) {
        this.game = game;
        this.nicknameQueue = new ArrayList<>(game.getPlayersNicknames());
        this.activePlayer = nicknameQueue.get(0);
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
        this.mainPhase = MainPhase.PLANNING;
    }

    /**
     * Getter for activePlayer
     *
     * @return nickname of the playing person
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Setter for activePlayer
     *
     * @param activePlayer nickname of next active
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Resets the chosen assistants for a new turn
     */
    public void resetChosen(){
        chosen = new ArrayList<Assistant>();
    }

    /**
     * Gets the list of chosen assistants
     *
     * @return assistants chosen in this turn
     */
    public ArrayList<Assistant> getChosen() {
        return chosen;
    }

    /**
     * Getter for the number of moves made by the player
     *
     * @return the number of moves
     */
    public int getMoved() {
        return moved;
    }

    /**
     * Setter for moves made by the player
     */
    public void setMoved(int moved) {
        this.moved = moved;
    }

    //TODO
    public void broadcastMatchInfo() {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showMatchInfo(game.getChosenPlayerNumber(), game.getNumCurrentActivePlayers());
        }
    }

    /**
     * Sets next active player.
     */
    public void next() {
        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getActives()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
        phaseType = PhaseType.YOUR_MOVE;
    }


    /**
     * Setter for main phase
     *
     * @param turnMainPhase the phase of the turn
     */
    public void setMainPhase(MainPhase turnMainPhase) {
        this.mainPhase = turnMainPhase;
    }

    /**
     * Getter for main phase
     *
     * @return the phase of the turn
     */
    public MainPhase getMainPhase() {
        return mainPhase;
    }

    /**
     * Setter for phaseType
     *
     * @param turnPhaseType secondary turn phase
     */
    public void setPhaseType(PhaseType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }

    /**
     * Initializes a new turn for the game, removing the expert cards effects and saving the game on
     * storage files
     *
     * @throws noMoreStudentsException if there's no more students left in sack
     */

    public void newTurn() throws noMoreStudentsException {
        setActivePlayer(nicknameQueue.get(0));
        turnControllerNotify("Turn of " + activePlayer, activePlayer);
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("Initiate the game! Pick your clouds. . .");
        if(restored==0) {
            if (toReset.size() > 0) {
                for (Character c : toReset) {
                    c.removeEffect();
                }
            }
        }
        else if(restored==1){
            restored = 0;
            toReset=new ArrayList<>();
            game.getGameBoard().setActiveCards(new ArrayList<>());
        }

        setMainPhase(MainPhase.PLANNING);
        setPhaseType(PhaseType.ADD_ON_CLOUD);

        StorageData storageData = new StorageData();
        storageData.store(gameController);

        pickCloud();
    }

    /**
     * Method to show message to the virtualViews
     *
     * @param messageToNotify the message to send
     * @param excludeNickname the nickname of the player to exclude
     */

    public void turnControllerNotify(String messageToNotify, String excludeNickname) {
        virtualViewMap.values().forEach(vv -> vv.showMatchInfo(game.getChosenPlayerNumber(), game.getNumCurrentPlayers()));
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    /**
     * Method that asks the views for cloud input
     */

    public void pickCloud(){
        ArrayList<Cloud> cloudList = game.getEmptyClouds();
        VirtualView virtualView = virtualViewMap.get(activePlayer);

        virtualView.askCloud(activePlayer,cloudList);
    }


    /**
     * Method that puts students on a cloud
     *
     * @param cloudIndex the selected cloud's index
     * @throws noMoreStudentsException if there's no more students in the sack
     */
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
            if(s == null){
                throw new noMoreStudentsException();
            }
            cloud.addStudent(s);
        }

    }

    /**
     * Method that asks the virtualView for an assistant to draw
     */
    public void drawAssistant(){
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.askAssistant(activePlayer,chosen);
    }

    /**
     * Method that determines the order for the game's turn, switches phase to action
     */

    public void determineOrder(){
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

        setActivePlayer(nicknameQueue.get(0));
        this.resetChosen();
        mainPhase = MainPhase.ACTION;

    }

    /**
     * Method that asks the virtualView for moves
     */

    public void moveMaker(){
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("You have moved "+ moved + " students!");
        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
    }

    /**
     * Method that handles the choice to move student on row
     *
     * @param color the color to move
     * @param row the row to move it on
     * @throws noStudentException if there's no matching student
     * @throws maxSizeException if the row's full
     * @throws emptyDecktException if player's deck is empty
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if the towers are full
     * @throws noTowerException if there's no tower on the island
     * @throws invalidNumberException if the number of moves's invalid
     * @throws noTowersException if there's no towers on the dashboard
     */

    public void moveOnBoard(Color color, Color row) throws noStudentException,maxSizeException,  emptyDecktException, noMoreStudentsException, fullTowersException, noTowerException, invalidNumberException, noTowersException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        try {
            player.getDashboard().addStudent(player.getDashboard().takeStudent(color));
        } catch (maxSizeException e) {
            moveMaker();
            return;
        }
        try {
            checkProfessors(color);
        } catch (maxSizeException e) {
            moveMaker();
            return;
        }
        if(gameController.getGameMode().equals(modeEnum.EXPERT)){
            if(player.getDashboard().getRow(row).getStudents().size()%3==0){
                player.addCoin(1);
                game.getGameBoard().removeCoin();
            }
        }
        moved++;
    }


    /**
     * Method that handles the choice to move the student on island
     *
     * @param color the student to move
     * @param index the index of the island
     * @throws noStudentException if there's no matching student
     */
    public void moveOnIsland(Color color, int index) throws noStudentException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        game.getGameBoard().placeStudent(color, player.getDashboard().takeStudent(color), index);
        moved++;
    }

    /**
     * Method that handles the moves of motherNature on the gameboard
     *
     * @param moves the number of moves selected by the player
     * @return integer value to signal the different cases for the action's outcomes
     * @throws noTowerException if there's no tower on the island
     * @throws noTowersException if there's no towers on the dashboard
     */

    public int moveMother(int moves) throws noTowerException, noTowersException {
        int extra = 0;
        Character c=null;
        for(Character car : getToReset()){
            if(car.getName().equals(ExpertDeck.GAMBLER)){
                extra = 2;
                c = car;
            }
        }
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
        game.getGameBoard().getIslands().get(actual).addMother();
        virtualViewMap.get(activePlayer).showGenericMessage("Mother nature on: "+actual);
        if(extra>0){
            c.removeEffect();
        }
        return checkInfluence(actual);
    }

    /**
     * Method to check the influence a player has on the island, includes various effects for the
     * expert cards
     *
     * @param actual index of active island
     * @return integer value to symbolize the result of the operation
     * @throws noTowerException if there's no tower on the island
     * @throws noTowersException if there's no towers on the player's dashboard
     */

    public int checkInfluence(int actual) throws noTowerException, noTowersException {
        Player player = game.getPlayerByNickname(activePlayer);
        Type team = player.getDashboard().getTeam();
        Island active = game.getGameBoard().getIslands().get(actual);
        if(active.isBlocked()){
            VirtualView vv = virtualViewMap.get(activePlayer);
            vv.showGenericMessage("The island is blocked!\n");
            for(Character c : toReset){
                if(c.getName().equals(ExpertDeck.KNIGHT)){
                    c.removeEffect();
                }
                if(c.getName().equals(ExpertDeck.HERBALIST)){
                    InfluenceBansCard ci = (InfluenceBansCard) c;
                    ci.removeEffect(actual);
                    return 0;
                }
            }
            return 0;
        }
        Character character=null;
        int set = 0;
        int influence = 0;
        Player owner=player;


        for(Color c : player.getProfessors()){
            if(!banned.contains(c))
                influence = influence + active.getStudents().get(c).size();
            else {
                VirtualView vv = virtualViewMap.get(activePlayer);
                vv.showGenericMessage("The color "+c.getText()+" is banned!\n");
            }
        }


        if(active.getTower()) {
            for(Character car : getToReset()){
                if(car.getName().equals(ExpertDeck.CUSTOMER)){
                    character = car;
                }
            }
            if (active.getTeam().equals(team) && character==null) {
                influence = influence + active.getDimension();
            }
            else if(character!=null){
                character.removeEffect();
            }
        }
        for(Character car : getToReset()){
            if(car.getName().equals(ExpertDeck.KNIGHT)){
                character = car;
            }
        }
        if(character!=null){
            influence = influence +2;
            character.removeEffect();
        }


        if(influence > active.getInfluence()){
            set = 1;
            active.setInfluence(influence);
        }

        for(Player p : game.getPlayers()){
            int influenceOther = 0;
            for(Color c : p.getProfessors()){
                if(!banned.contains(c))
                    influenceOther = influenceOther + active.getStudents().get(c).size();
            }

            if(active.getTower()) {
                if (active.getTeam().equals(p.getDashboard().getTeam())) {
                    influenceOther = influenceOther + active.getDimension();
                }
            }
            if(influenceOther> influence){
                set = 1;
                owner = p;
                influence = influenceOther;
            }
        }

        banned = new ArrayList<>();

        if(set==1){
            active.setInfluence(influence);
            if(active.getTower() && !(active.getTeam().equals(owner.getDashboard().getTeam()))){
                for(Player p : game.getPlayers()){
                    if(p.getDashboard().getTeam().equals(active.getTeam())){
                        try {
                            p.putTower();
                        } catch (fullTowersException e) {
                            break;
                        }
                    }
                }
            }
            if(!active.getTower() || (active.getTower() && !(active.getTeam().equals(owner.getDashboard().getTeam())))) {
                active.setTower(owner.getDashboard().getTower());
            }
            VirtualView vv = virtualViewMap.get(owner.getName());
            vv.showGenericMessage("The island is yours!");
            return towerChecker();
        }
        else{
            return 0;
        }

    }

    /**
     * Method to check the position of the game professors and give them to the players
     *
     * @param color the color the influence is calculated for
     * @throws emptyDecktException if there's no cards in the deck
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if there's too many towers
     * @throws noStudentException if there's no student of the selected color
     * @throws noTowerException if there's notower on island
     * @throws invalidNumberException if thenumber is invalid
     * @throws maxSizeException if the size of the row is too big
     * @throws noTowersException if there's no towers on dashboard
     */

    public void checkProfessors(Color color) throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, invalidNumberException, maxSizeException, noTowersException {
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


    /**
     * Method to check if the player's positioned their last tower
     *
     * @return integer value to notify the result of the operation
     */
    public int towerChecker(){
        for(Player p : game.getPlayers()) {
            if (p.getDashboard().getNumTowers() == 0) {
                activePlayer = p.getName();
                return 2;
            }
        }
        return 1;
    }


    /**
     * Method that calls the islandMerger of the gameboard
     *
     * @param active the index of the active island
     * @throws noTowerException if there's no tower on the island
     */
    public void islandMerger(Island active) throws noTowerException {
        game.getGameBoard().mergeIslands(active);

    }

    /**
     * method to get students from a cloud
     * @param index the cloud selected by the player
     */

    public void getFromCloud(int index){
        Player p = game.getPlayerByNickname(activePlayer);
        ArrayList<Student> students = game.getGameBoard().getClouds().get(index).removeStudents();
        for(Student s : students){
            p.getDashboard().addToHall(s);
        }
    }

    /**
     * method to activate the expert card selected by the player, it inserts the expert
     * in two lists, one for control and the other one for view purposes
     * @param card the selected expert card
     */

    public void useExpertEffect(ExpertDeck card){
        int cost = price.get(card);
        VirtualView vv = virtualViewMap.get(activePlayer);
        //vv.showGenericMessage("Your money: "+game.getPlayerByNickname(activePlayer).getCoins()+"\n");
            switch(card) {
                case COOK:
                    ProfessorControllerCard active = new ProfessorControllerCard(this.gameController, this);
                    vv.showGenericMessage("cost: " + active.getCost()+"+"+cost + "\n");
                    boolean result = active.checkMoney(game.getPlayerByNickname(activePlayer));
                    if (!result) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        game.getPlayerByNickname(activePlayer).removeCoin(active.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        active.useEffect();
                        toReset.add(active);
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;

                case GAMBLER:
                    TwoMoreMovesCard activeTM = new TwoMoreMovesCard(gameController, this);
                    vv.showGenericMessage("cost: " + activeTM.getCost()+"+"+cost + "\n");
                    if (!activeTM.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        activeTM.useEffect();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTM.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        //activeTM.addCoin();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case CUSTOMER:
                    NoTowerCard activeTC = new NoTowerCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeTC.getCost()+"+"+cost + "\n");
                    if (!activeTC.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTC.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        activeTC.useEffect();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case KNIGHT:
                    TwoMorePointsCard activeTP = new TwoMorePointsCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeTP.getCost()+"+"+cost + "\n");
                    if (!activeTP.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTP.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        activeTP.useEffect();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                        break;

                    case HERALD:
                        ImproperInfluenceCard activeII = new ImproperInfluenceCard(gameController, this);
                        vv.showGenericMessage("Cost: " + activeII.getCost()+"+"+cost + "\n");
                        if (!activeII.checkMoney(game.getPlayerByNickname(activePlayer))) {
                            vv.showGenericMessage("You haven't enough money for this!");
                            vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                            vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                        } else {
                            game.getPlayerByNickname(activePlayer).removeCoin(activeII.getCost()+cost);
                            price.put(card, price.get(card)+1);
                            toReset.add(activeII);
                            game.getGameBoard().getToReset().add(ExpertDeck.HERALD);
                            game.updateGameboard();
                            vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                        }
                        break;
                case HERBALIST:
                    InfluenceBansCard activeIB = new InfluenceBansCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeIB.getCost()+"+"+cost + "\n");
                    if (!activeIB.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        game.getPlayerByNickname(activePlayer).removeCoin(activeIB.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeIB);
                        game.getGameBoard().getToReset().add(ExpertDeck.HERBALIST);
                        game.updateGameboard();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;

                case SELLER:
                    NullColorCard activeNC = new NullColorCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeNC.getCost()+"+"+cost + "\n");
                    if (!activeNC.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        game.getPlayerByNickname(activePlayer).removeCoin(activeNC.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeNC);
                        game.getGameBoard().getToReset().add(ExpertDeck.SELLER);
                        game.updateGameboard();
                        activeNC.useEffect();
                    }
                    break;
                case BANKER:
                    RemoveAColorCard activeRC = new RemoveAColorCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeRC.getCost()+"+"+cost + "\n");
                    if (!activeRC.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        game.getPlayerByNickname(activePlayer).removeCoin(activeRC.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeRC);
                        game.getGameBoard().getToReset().add(ExpertDeck.BANKER);
                        game.updateGameboard();
                        activeRC.useEffect();
                    }
                    break;
                case BARBARIAN:
                    OneMoreStudentCard activeOM = null;
                        for (Character c : toReset) {
                            if (c.getName().equals(ExpertDeck.BARBARIAN)) {
                                activeOM = (OneMoreStudentCard) c;
                                if (!activeOM.checkMoney(game.getPlayerByNickname(activePlayer))) {
                                    vv.showGenericMessage("You haven't enough money for this!");
                                    vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                                    vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                                    return;
                                }
                                else {
                                    game.getPlayerByNickname(activePlayer).removeCoin(activeOM.getCost() + cost);
                                    price.put(card, price.get(card) + 1);
                                    game.getGameBoard().getToReset().add(ExpertDeck.BARBARIAN);
                                    game.updateGameboard();
                                    activeOM.useEffect();
                                    return;
                                }
                            }
                        }
                    try {
                        activeOM = new OneMoreStudentCard(gameController, this);
                    } catch (noMoreStudentsException e) {
                        e.printStackTrace();
                    }
                    vv.showGenericMessage("Cost: " + activeOM.getCost()+"+"+cost + "\n");
                    if (!activeOM.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{

                        game.getPlayerByNickname(activePlayer).removeCoin(activeOM.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeOM);
                        game.getGameBoard().getActiveCards().add(activeOM);
                        game.getGameBoard().getToReset().add(ExpertDeck.BARBARIAN);
                        game.updateGameboard();
                        activeOM.useEffect();
                    }
                    break;

                case MUSICIAN:
                    SwapTwoStudentsCard activeSS = new SwapTwoStudentsCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeSS.getCost()+"+"+cost + "\n");
                    if (!activeSS.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{
                        game.getPlayerByNickname(activePlayer).removeCoin(activeSS.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeSS);
                        game.getGameBoard().getToReset().add(ExpertDeck.MUSICIAN);
                        game.updateGameboard();
                        activeSS.useEffect();
                    }
                    break;
                case JOKER:
                    ExchangeStudentsCard activeES = null;
                    for (Character c : toReset) {
                        if (c.getName().equals(ExpertDeck.JOKER)) {
                            activeES = (ExchangeStudentsCard) c;
                            if (!activeES.checkMoney(game.getPlayerByNickname(activePlayer))) {
                                vv.showGenericMessage("You haven't enough money for this!");
                                vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                                vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                                return;
                            }
                            else {
                                game.getPlayerByNickname(activePlayer).removeCoin(activeES.getCost() + cost);
                                price.put(card, price.get(card) + 1);
                                game.getGameBoard().getToReset().add(ExpertDeck.JOKER);
                                game.updateGameboard();
                                activeES.useEffect();
                                return;
                            }
                        }
                    }
                    try {
                        activeES = new ExchangeStudentsCard(gameController, this);
                    } catch (noMoreStudentsException e) {
                        e.printStackTrace();
                    }
                    vv.showGenericMessage("Cost: " + activeES.getCost()+"+"+cost + "\n");
                    if (!activeES.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{
                        game.getPlayerByNickname(activePlayer).removeCoin(activeES.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeES);
                        game.getGameBoard().getActiveCards().add(activeES);
                        game.getGameBoard().getToReset().add(ExpertDeck.JOKER);
                        game.updateGameboard();
                        activeES.useEffect();
                    }
                    break;

                case TAVERNER:
                    ToIslandCard activeTI=null;
                    for (Character c : toReset) {
                        if (c.getName().equals(ExpertDeck.TAVERNER)) {
                            activeTI = (ToIslandCard) c;
                            if (!activeTI.checkMoney(game.getPlayerByNickname(activePlayer))) {
                                vv.showGenericMessage("You haven't enough money for this!");
                                vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                                vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                                return;
                            }
                            else {
                                game.getGameBoard().getToReset().add(ExpertDeck.TAVERNER);
                                game.updateGameboard();
                                game.getPlayerByNickname(activePlayer).removeCoin(activeTI.getCost() + cost);
                                price.put(card, price.get(card) + 1);
                                activeTI.useEffect();
                                return;
                            }
                        }
                    }
                    try {
                        activeTI = new ToIslandCard(gameController, this);
                    } catch (noMoreStudentsException e) {
                        e.printStackTrace();
                    }
                    vv.showGenericMessage("Cost: " + activeTI.getCost()+"+"+cost + "\n");
                    if (!activeTI.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTI.getCost()+cost);
                        price.put(card, price.get(card)+1);
                        toReset.add(activeTI);
                        game.getGameBoard().getActiveCards().add(activeTI);
                        game.getGameBoard().getToReset().add(ExpertDeck.TAVERNER);
                        game.updateGameboard();
                        activeTI.useEffect();
                    }
                    break;

                    }
    }

    /**
     * method that handles the effects of an expert card that requires an input from the player
     * @param message the message sent from the client
     * @throws emptyDecktException if there's no more cards in the deck
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if the number of tower's full
     * @throws noStudentException if there's no matching student
     * @throws noTowerException if there's no tower
     * @throws invalidNumberException if the selected number is invalid
     * @throws maxSizeException if the max size's reached
     * @throws noTowersException if there's no tower on dashboard
     */

    public void effectHandler(EffectMessage message) throws emptyDecktException, noMoreStudentsException, fullTowersException, noStudentException, invalidNumberException, maxSizeException, noTowersException, noTowerException {
        VirtualView vv = virtualViewMap.get(activePlayer);
        Character chosen = null;
        for(Character c : toReset){
            if(message.getName().equals(c.getName())){
                chosen = c;
            }
        }
        switch(message.getName()){
            case HERALD:
                ImproperInfluenceCard card = (ImproperInfluenceCard) chosen;
                ((ImproperInfluenceCard) chosen).setIndex(message.getIndex());
                card.useEffect();
                card.removeEffect();
                game.updateGameboard();
                break;

            case HERBALIST:
                InfluenceBansCard c = (InfluenceBansCard) chosen;
                c.setIndex(message.getIndex());
                c.useEffect();
                gameController.getGame().getGameBoard().getToReset().remove(ExpertDeck.HERBALIST);
                game.updateGameboard();
                break;
            case SELLER:
                NullColorCard colorCard = (NullColorCard) chosen;
                colorCard.setColor(message.getColor());
                game.updateGameboard();
                break;
            case BANKER:
                RemoveAColorCard colorCard1 = (RemoveAColorCard) chosen;
                colorCard1.setColor(message.getColor());
                game.updateGameboard();
                break;
            case BARBARIAN:
                OneMoreStudentCard oneMoreStudentCard = (OneMoreStudentCard) chosen;
                oneMoreStudentCard.addStudent(message.getColor());
                game.updateGameboard();
                break;
            case MUSICIAN:
                SwapTwoStudentsCard swapTwoStudentsCard = (SwapTwoStudentsCard) chosen;
                if(swapTwoStudentsCard.getHall()!=null){
                    try {
                        swapTwoStudentsCard.getColorRow(message.getColor());
                    } catch (noStudentException e) {
                        gameController.draw();
                    } catch (maxSizeException e) {
                        return;
                    }
                }
                else{
                    swapTwoStudentsCard.getColorHall(message.getColor());
                }
                game.updateGameboard();
                return;
            case JOKER:
                ExchangeStudentsCard exchangeStudentCard = (ExchangeStudentsCard) chosen;
                if(exchangeStudentCard.getHall()!=null){
                    exchangeStudentCard.swapStudent(message.getColor());
                }
                else
                    exchangeStudentCard.getColorHall(message.getColor());
                game.updateGameboard();
                return;

            case TAVERNER:
                ToIslandCard toIslandCard = (ToIslandCard) chosen;
                if(toIslandCard.getChosen()==null){
                    toIslandCard.getStudent(message.getColor());
                }
                else{
                    toIslandCard.getIsland(message.getIndex());
                }
                game.updateGameboard();
                return;

        }
        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
    }

    public List<Character> getToReset() {
        return toReset;
    }

    public List<Color> getBanned() {
        return banned;
    }

    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setToReset(List<Character> toReset) {
        this.toReset = toReset;
    }

    public void setGame(Mode game) {
        this.game = game;
    }

    public Map<ExpertDeck, Integer> getPrice() {
        return price;
    }

    public void setVirtualViewMap(Map<String, VirtualView> virtualViewMap) {
        this.virtualViewMap = virtualViewMap;
    }

    public void setRestored(int set){
        restored = set;
    }
}
