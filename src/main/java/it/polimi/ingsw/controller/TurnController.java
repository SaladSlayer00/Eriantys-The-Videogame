package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.EffectMessage;
import it.polimi.ingsw.message.GenericMessage;
import it.polimi.ingsw.message.PlayerNumberReply;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.expertDeck.*;
import it.polimi.ingsw.model.expertDeck.Character;
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
    private List<String> nicknameQueue;
    private String activePlayer;
    transient Map<String, VirtualView> virtualViewMap;
    private MainPhase mainPhase;
    private PhaseType phaseType;
    private final GameController gameController;
    private ArrayList<Assistant> chosen = new ArrayList<>();
    private int moved = 0;
    private List<Character> toReset = new ArrayList<>();
    private List<Color> banned = new ArrayList<>();

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
    //TODO rimuovere solo il nickname dalla lista game
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

        StorageData storageData = new StorageData();
        storageData.store(gameController);

        setMainPhase(MainPhase.PLANNING);
        setPhaseType(PhaseType.ADD_ON_CLOUD);
        if(toReset.size()>0){
            for(Character c : toReset){
                c.removeEffect();
            }
            toReset=new ArrayList<>();
        }
        pickCloud();
        //drawAssistant();
    }

    //quelle di turno le rimuove qua, quelle di metodo le devo rimuovere io
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

    public void moveOnBoard(Color color, Color row) throws noStudentException, maxSizeException,  emptyDecktException, noMoreStudentsException, fullTowersException, noTowerException, invalidNumberException, noTowersException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        VirtualView vv = virtualViewMap.get(player);
        player.getDashboard().addStudent(player.getDashboard().takeStudent(color));
        checkProfessors(color);
        if(gameController.getGameMode().equals(modeEnum.EXPERT)){
            if(player.getDashboard().getRow(row).getStudents().size()%3==0){
                player.addCoin(1);
                game.getGameBoard().removeCoin();
            }
        }
        moved++;
    }

    public void moveOnIsland(Color color, int index) throws noStudentException, noTowerException {
        Player player = game.getPlayerByNickname(getActivePlayer());
        game.getGameBoard().placeStudent(color, player.getDashboard().takeStudent(color), index);
        moved++;
    }

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

    public int checkInfluence(int actual) throws noTowerException, noTowersException {
        Player player = game.getPlayerByNickname(activePlayer);
        Type team = player.getDashboard().getTeam();
        Island active = game.getGameBoard().getIslands().get(actual);
        if(active.isBlocked()){
            VirtualView vv = virtualViewMap.get(activePlayer);
            vv.showGenericMessage("The island is blocked!\n");
            for(Character c : toReset){
                if(c.getName().equals(ExpertDeck.HERBALIST)){
                    c.removeEffect();
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
            active.setTower(owner.getDashboard().getTower());
            VirtualView vv = virtualViewMap.get(owner.getName());
            vv.showGenericMessage("The island is yours!");
            return towerChecker();
            //islandMerger(active);
        }
        else{
            return 0;
        }

    }

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




    public int towerChecker(){
        Player p = game.getPlayerByNickname(activePlayer);
        if(p.getDashboard().getNumTowers()==0){
            return 2;
        }
        return 1;
    }

    public void islandMerger(Island active) throws noTowerException {
        game.getGameBoard().mergeIslands(active);

    }



    public void getFromCloud(int index){
        Player p = game.getPlayerByNickname(activePlayer);
        ArrayList<Student> students = game.getGameBoard().getClouds().get(index).removeStudents();
        for(Student s : students){
            p.getDashboard().addToHall(s);
        }
    }

    public void useExpertEffect(ExpertDeck card){
        VirtualView vv = virtualViewMap.get(activePlayer);
            switch(card) {
                case COOK:
                    ProfessorControllerCard active = new ProfessorControllerCard(this.gameController, this);
                    vv.showGenericMessage("cost: " + active.getCost() + "\n");
                    boolean result = active.checkMoney(game.getPlayerByNickname(activePlayer));
                    if (!result) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        active.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(active.getCost());
                        active.useEffect();
                        toReset.add(active);
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;

                case GAMBLER:
                    TwoMoreMovesCard activeTM = new TwoMoreMovesCard(gameController, this);
                    vv.showGenericMessage("cost: " + activeTM.getCost() + "\n");
                    if (!activeTM.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        //TODO toReset.add(activeTM); vedere se funziona ancora
                        activeTM.addCoin();
                        activeTM.useEffect();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTM.getCost());
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case CUSTOMER:
                    NoTowerCard activeTC = new NoTowerCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeTC.getCost() + "\n");
                    if (!activeTC.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        activeTC.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTC.getCost());
                        activeTC.useEffect();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case KNIGHT:
                    TwoMorePointsCard activeTP = new TwoMorePointsCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeTP.getCost() + "\n");
                    if (!activeTP.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        activeTP.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeTP.getCost());
                        activeTP.useEffect();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                        break;

                    case HERALD:
                        ImproperInfluenceCard activeII = new ImproperInfluenceCard(gameController, this);
                        vv.showGenericMessage("Cost: " + activeII.getCost() + "\n");
                        if (!activeII.checkMoney(game.getPlayerByNickname(activePlayer))) {
                            vv.showGenericMessage("You haven't enough money for this!");
                            vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                            vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                        } else {
                            activeII.addCoin();
                            game.getPlayerByNickname(activePlayer).removeCoin(activeII.getCost());
                            //per chiamare effetto
                            toReset.add(activeII);
                            //per vedere da vv
                            game.getGameBoard().getToReset().add(ExpertDeck.HERALD);
                            game.updateGameboard();
                            //activeII.useEffect();
                            vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                        }
                        break;
                case HERBALIST:
                    InfluenceBansCard activeIB = new InfluenceBansCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeIB.getCost() + "\n");
                    if (!activeIB.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        activeIB.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeIB.getCost());
                        //per chiamare effetto
                        toReset.add(activeIB);
                        //per vedere da vv
                        game.getGameBoard().getToReset().add(ExpertDeck.HERBALIST);
                        game.updateGameboard();
                        //activeII.useEffect();
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;

                case SELLER:
                    NullColorCard activeNC = new NullColorCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeNC.getCost() + "\n");
                    if (!activeNC.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        activeNC.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeNC.getCost());
                        //per chiamare effetto
                        toReset.add(activeNC);
                        //per vedere da vv
                        game.getGameBoard().getToReset().add(ExpertDeck.SELLER);
                        game.updateGameboard();
                        activeNC.useEffect();
                        //vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case BANKER:
                    RemoveAColorCard activeRC = new RemoveAColorCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeRC.getCost() + "\n");
                    if (!activeRC.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    } else {
                        activeRC.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeRC.getCost());
                        //per chiamare effetto
                        toReset.add(activeRC);
                        //per vedere da vv
                        game.getGameBoard().getToReset().add(ExpertDeck.BANKER);
                        game.updateGameboard();
                        activeRC.useEffect();
                        //vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case BARBARIAN:
                    OneMoreStudentCard activeOM = null;
                    if(game.getGameBoard().getToReset().contains(ExpertDeck.BARBARIAN)) {
                        for (Character c : toReset) {
                            if (c.getName().equals(ExpertDeck.BARBARIAN)) {
                                activeOM = (OneMoreStudentCard) c;
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
                    vv.showGenericMessage("Cost: " + activeOM.getCost() + "\n");
                    if (!activeOM.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{
                            activeOM.addCoin();
                            game.getPlayerByNickname(activePlayer).removeCoin(activeOM.getCost());
                            //per chiamare effetto
                            toReset.add(activeOM);
                            //per vedere da vv
                            game.getGameBoard().getToReset().add(ExpertDeck.BARBARIAN);
                            game.updateGameboard();
                            activeOM.useEffect();
                            //vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                        }
                    break;

                case MUSICIAN:
                    SwapTwoStudentsCard activeSS = new SwapTwoStudentsCard(gameController, this);
                    vv.showGenericMessage("Cost: " + activeSS.getCost() + "\n");
                    if (!activeSS.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{
                        activeSS.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeSS.getCost());
                        //per chiamare effetto
                        toReset.add(activeSS);
                        //per vedere da vv
                        game.getGameBoard().getToReset().add(ExpertDeck.MUSICIAN);
                        game.updateGameboard();
                        activeSS.useEffect();
                        //vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                case JOKER:
                    ExchangeStudentsCard activeES = null;
                    try {
                        activeES = new ExchangeStudentsCard(gameController, this);
                    } catch (noMoreStudentsException e) {
                        e.printStackTrace();
                    }
                    vv.showGenericMessage("Cost: " + activeES.getCost() + "\n");
                    if (!activeES.checkMoney(game.getPlayerByNickname(activePlayer))) {
                        vv.showGenericMessage("You haven't enough money for this!");
                        vv.showGenericMessage("You have " + game.getPlayerByNickname(activePlayer).getCoins() + "\n");
                        vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    else{
                        activeES.addCoin();
                        game.getPlayerByNickname(activePlayer).removeCoin(activeES.getCost());
                        //per chiamare effetto
                        toReset.add(activeES);
                        //per vedere da vv
                        game.getGameBoard().getToReset().add(ExpertDeck.JOKER);
                        game.updateGameboard();
                        activeES.useEffect();
                        //vv.askMoves(game.getPlayerByNickname(activePlayer).getDashboard().getHall(), game.getGameBoard().getIslands());
                    }
                    break;
                    }
    }


    public void effectHandler(EffectMessage message){
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
                //non ha più herbalist questa volta non chiede input
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
                        e.printStackTrace();
                    } catch (maxSizeException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    swapTwoStudentsCard.getColorHall(message.getColor());
                }
                game.updateGameboard();
                return;
            case JOKER:
                ExchangeStudentsCard exchangeStudentCard = (ExchangeStudentsCard) chosen;
                exchangeStudentCard.swapStudent(message.getColor());
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

    public void setVirtualViewMap(Map<String, VirtualView> virtualViewMap) {
        this.virtualViewMap = virtualViewMap;
    }
}
