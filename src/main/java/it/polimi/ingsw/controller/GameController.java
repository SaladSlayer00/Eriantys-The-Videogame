//serve n try catch dove c'è la no more students exception


package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.StorageData;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.message.MessageType.PLAYERNUMBER_REPLY;
import static it.polimi.ingsw.message.MessageType.GAMEMODE_REPLY;

//TODO gestire la logica di fare agire solamente il player attivo
//TODO observer update o si leva o si ricicla x esperti

//il game controller può occuparsi delle azioni che riguardano l'azione sul gioco complessivo
public class GameController implements Serializable {
    private InputController inputController;
    private transient Map<String, VirtualView> virtualViewMap;
    private Mode game;
    private GameState gameState;
    private TurnController turnController;
    private modeEnum gameMode;
    private GameFactory gameFactory;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    public static final String SAVED_GAME_FILE = "match.bless";
    public static final int MAX_PLAYERS = 4;

    public GameController(){
        initGameController();
    }


    public void initGameController() {
        this.gameFactory = new GameFactory();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this, null);
        setGameState(GameState.SET_MODE);

    }

    public boolean isGameStarted() {
        return gameState.equals(GameState.IN_GAME);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void onMessageReceived(Message receivedMessage) throws invalidNumberException, noMoreStudentsException, fullTowersException, noStudentException, noTowerException, maxSizeException, noTowersException, emptyDecktException {

        VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
        switch (gameState) {
            case SET_MODE:
                setModeState(receivedMessage);
                break;
            case LOGIN:
                loginState(receivedMessage);
                break;
            case INIT:
                if (inputController.checkUser(receivedMessage)) {
                    initState(receivedMessage, virtualView);
                }
                break;
            case IN_GAME:
                if (inputController.checkUser(receivedMessage)) {
                    inGameState(receivedMessage);
                }
                break;
            default: // Should never reach this condition
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    private void setModeState(Message receivedMessage){
         if (receivedMessage.getMessageType() == GAMEMODE_REPLY) {
            if(inputController.verifyReceivedData(receivedMessage)){
                gameFactory.setType(((GameModeReply) receivedMessage).getGameMode());
                this.setGameMode(((GameModeReply) receivedMessage).getGameMode());
                virtualViewMap.get(receivedMessage.getNickname()).askPlayersNumber();
                setGameState(GameState.LOGIN);
            }
        }
         else{
             Server.LOGGER.warning("Wrong message received from client");
         }

    }

    private void loginState(Message receivedMessage) throws invalidNumberException {
        if (receivedMessage.getMessageType() == PLAYERNUMBER_REPLY) {
            if (inputController.verifyReceivedData(receivedMessage)) {
                this.game = gameFactory.getMode(gameFactory.getType(), ((PlayerNumberReply) receivedMessage).getPlayerNumber());
                this.inputController.setGame(game);
                game.initializePlayer(new Player(receivedMessage.getNickname(), 1));
                EasyGame easyGame = (EasyGame) game;
                easyGame.addObserver(virtualViewMap.get(receivedMessage.getNickname()));
                broadcastGenericMessage("Waiting for other Players . . .");
            }
        }
        else {
            Server.LOGGER.warning("Wrong message received from client.");
        }
    }


    //login handler sets the state to INIT
    public void loginHandler(String nickname, int ID, VirtualView virtualView) {

        if (virtualViewMap.isEmpty()) { // First player logged. Ask number of players.
            addVirtualView(nickname, virtualView);
            //game.getPlayers().add(new Player(nickname, ID));
            //game.initializePlayer(new Player(nickname,ID));

            virtualView.showLoginResult(true, true, "server");
            //virtualView.askPlayersNumber();
            virtualView.askGameMode(nickname, modeEnum.availableGameModes());

        } else if (virtualViewMap.size() < game.getChosenPlayerNumber()) {
            addVirtualView(nickname, virtualView);
            //game.getPlayers().add(new Player(nickname, ID));
            game.initializePlayer(new Player(nickname,ID));
            virtualView.showLoginResult(true, true, "server");

            if (game.getNumCurrentActivePlayers() == game.getChosenPlayerNumber()) { // If all players logged

                // check saved matches.
                StorageData storageData = new StorageData();
                GameController savedGameController = storageData.restore();
                if (savedGameController != null &&
                        game.getPlayersNicknames().containsAll(savedGameController.getTurnController().getNicknameQueue())) {
                    restoreControllers(savedGameController);
                    broadcastRestoreMessages();
                    Server.LOGGER.info("Saved Match restored.");
                    turnController.newTurn();
                } else {
                    initGame();
                }
            }
        } else {
            virtualView.showLoginResult(true, false, "server");
        }
    }

    private void broadcastRestoreMessages() {
        for (VirtualView vv : virtualViewMap.values()) {
            ArrayList<Dashboard> dashboards = new ArrayList<Dashboard>();
            for(Player p: game.getPlayers()){
                dashboards.add(p.getDashboard());
            }
            vv.updateTable(game.getGameBoard(), dashboards);
        }

        for (VirtualView vv : virtualViewMap.values()) {

            vv.showMatchInfo(game.getChosenPlayerNumber(), game.getNumCurrentActivePlayers());
        }
    }

    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this, this.game);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing their deck. . .");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askInitDeck(turnController.getActivePlayer(),Mage.notChosen());
    }


    private void initState(Message receivedMessage, VirtualView virtualView) throws noMoreStudentsException, fullTowersException, maxSizeException {
        switch (receivedMessage.getMessageType()) {
            case INIT_DECK:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    deckHandler((DeckMessage) receivedMessage);
                }
                break;

            case INIT_TOWERS:
                if(inputController.verifyReceivedData(receivedMessage)){
                    towerHandler((TowerMessage) receivedMessage);
                }
                break;
            case INIT_GAMEBOARD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    System.out.println("ok");
                    startHandler((StartMessage) receivedMessage);
                }
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }


    private void deckHandler(DeckMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        if (Mage.notChosen().size()==4){
            //4-game.getChosenPlayerNumber()
            player.setDeck(receivedMessage.getMage());
            Mage.choose(receivedMessage.getMage());
            virtualView.showGenericMessage("You chose your deck. Please wait for the other players to pick!");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked their deck.", turnController.getActivePlayer());
            askDeckToNextPlayer();
        }
        else if(Mage.notChosen().size()==1){
            virtualView.showGenericMessage("Your mage calls you! You have the " + Mage.notChosen().get(0) + " deck!");
            player.setDeck(Mage.notChosen().get(0));
            virtualView.showGenericMessage("It's your turn now. Please pick your team.");
            virtualView.askInitType(turnController.getActivePlayer(),Type.notChosen());
        }
        else if(Mage.notChosen().size()<4){
            player.setDeck(receivedMessage.getMage());
            Mage.choose(receivedMessage.getMage());
            virtualView.showGenericMessage("You chose your deck");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked their deck.", turnController.getActivePlayer());
            virtualView.showGenericMessage("It's your turn now. Please pick your team.");
            virtualView.askInitType(turnController.getActivePlayer(),Type.notChosen());
        }

    }

    private void askDeckToNextPlayer() {
        turnController.next();
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his deck...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your deck.");
        virtualView.askInitDeck(turnController.getActivePlayer(),Mage.notChosen());
    }

    private void askTowerToNextPlayer(){

        turnController.next();
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his team...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your team.");
        virtualView.askInitType(turnController.getActivePlayer(),Type.notChosen());

    }

    private void towerHandler(TowerMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());

        if(Type.notChosen().size() ==3){
            player.getDashboard().setTeam(receivedMessage.getType());
            Type.choose(receivedMessage.getType());
            virtualView.showGenericMessage("You chose your team. Please wait for the other players to pick!");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked their team.", turnController.getActivePlayer());
            askTowerToNextPlayer();

        }
        //supporta solo per 2-3
        else if(Type.notChosen().size()<3){
            virtualView.showGenericMessage("Your towers call you! You're in the " + Type.notChosen().get(0) + " team!");
            player.getDashboard().setTeam(Type.notChosen().get(0));
            broadcastGenericMessage("All decks and teams are set! The mode of the game is " + gameMode +
                    " and the number of players is "+ this.game.getChosenPlayerNumber() + ".");
            virtualView.showGenericMessage("Are you sure you want to start the game with these settings?");
            //yes or no
            virtualView.askStart(turnController.getActivePlayer(), "START");
        }

    }

    private void startHandler(StartMessage receivedMessage) throws noMoreStudentsException, fullTowersException, maxSizeException {
            turnController.next();
            game.initializeGameboard();
            game.initializeDashboards();
            startGame();
//        else {
//            VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
//            vv.askStart(turnController.getActivePlayer(), "START");
//
//        }

    }

    public void setGameMode(modeEnum gameMode) {
        this.gameMode = gameMode;
    }

    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");
        turnController.broadcastMatchInfo();
        turnController.newTurn();
        game.updateGameboard();
    }

    private void inGameState(Message receivedMessage) throws noMoreStudentsException, noStudentException, noTowerException, maxSizeException, noTowersException, emptyDecktException {
        switch (turnController.getMainPhase()){
            case PLANNING:
                planningState(receivedMessage);
                break;
            case ACTION:
                actionState(receivedMessage);
                break;

        }
    }

    private void planningState(Message receivedMessage) throws noMoreStudentsException, emptyDecktException {
        switch(receivedMessage.getMessageType()){
            case PICK_CLOUD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    pickCloudHandler(((PickCloudMessage)receivedMessage));
                }
                break;
            case DRAW_ASSISTANT:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    drawAssistantHandler((AssistantMessage)receivedMessage);
                }
                break;
        }
    }

//gli handler della azione richiamano il turnController
    private void actionState(Message receivedMessage) throws noTowerException, noStudentException, maxSizeException, noTowersException {
        switch (receivedMessage.getMessageType()) {
            case MOVE_ON_ISLAND:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    moveHandler((MoveMessage) receivedMessage);
                }
                break;
            case MOVE_ON_BOARD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    moveHandler((MoveMessage) receivedMessage);
                }
                break;
            case MOVE_MOTHER:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    motherHandler((MoveMotherMessage) receivedMessage);
                }
                break;

            case GET_FROM_CLOUD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    getFromCloudHandler((PickCloudMessage) receivedMessage);
                }
                break;

//            case USE_EXPERT:
//                if (inputController.verifyReceivedData(receivedMessage)) {
//                    expertHandler((UseExpertMessage) receivedMessage);
//                }
//                break;

            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    //la prima volta è richiamato nello startTurn, questo è quando arriva l'altro messaggio
    private void pickCloudHandler(PickCloudMessage receivedMessage) throws noMoreStudentsException {
        //Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        //quello che manda deve essere activeplayer dove lo controlla??
        game.updateGameboard();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        turnController.cloudInitializer(receivedMessage.getCloudIndex());//metodo per prendere l'indice cloud nel messaggio
        if(game.getNoMoreStudents()){
            broadcastGenericMessage("There are no more students in the sack! The game's over.");
            draw();
        }
        //sarà da scrivere il messaggio col giusto formato
        if(game.getEmptyClouds().size() > 1){
            virtualView.showGenericMessage("Please pick the cloud you want to setup. ");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is picking the clouds.", turnController.getActivePlayer());
            turnController.next();
            turnController.pickCloud();
        }

        else if (game.getEmptyClouds().size() == 0) {
            virtualView.showGenericMessage("You chose every cloud! Get ready to choose your assistant");
            broadcastGenericMessage("All clouds are set! Ready to initiate drawing phase!");
            turnController.resetChosen();
            turnController.drawAssistant();
        }


    }
    private void drawAssistantHandler(AssistantMessage receivedMessage) throws emptyDecktException {
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing their assistant", turnController.getActivePlayer());
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        Assistant card = player.getDeck().draw(receivedMessage.getIndex());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        player.setCard(card);
        turnController.getChosen().add(card);
        if(player.getDeck().getNumCards()==0){
            //broadcastDrawMessage();
            broadcastGenericMessage("Game finished! It's a draw!");
            draw();
        }

        //check sulla carta uguale la facciamo nell'input controller
        if(turnController.getChosen().size() < game.getNumCurrentActivePlayers()){
            virtualView.showGenericMessage("You chose your assistant. Please wait for the other players to pick!");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked their deck.", turnController.getActivePlayer());
            turnController.next();
            turnController.drawAssistant();
        }
        else{
            broadcastGenericMessage("All assistants are set! Please wait for the game to decide the turn order!");
            //yes or no
            turnController.determineOrder();
            initiateAction();
        }
        game.updateGameboard();

    }

    public void initiateAction(){
        broadcastGenericMessage("Now playing "+ turnController.getActivePlayer());
        turnController.moveMaker();
    }


    public void moveHandler(MoveMessage moveMessage) throws noStudentException, maxSizeException {
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing their assistant", turnController.getActivePlayer());
        if (moveMessage.getMessageType() == MessageType.MOVE_ON_BOARD) {
            turnController.moveOnBoard(moveMessage.getColor(), moveMessage.getRow());
        }
        else if(moveMessage.getMessageType() == MessageType.MOVE_ON_ISLAND){
            turnController.moveOnIsland(moveMessage.getColor(), moveMessage.getIndex());
        }
        if(turnController.getMoved()<3){
            turnController.moveMaker();
        }
        else{
            VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
            virtualView.showGenericMessage("You've chosen all your students!");
            virtualView.showGenericMessage("Please choose the number of moves of mother nature");
            virtualView.askMotherMoves(turnController.getActivePlayer(),game.getPlayerByNickname(turnController.getActivePlayer()).getCardChosen().getMove());
            turnController.setMoved(0);
        }
        game.updateGameboard();
    }

    //bisogna fare un controllo su
    public void motherHandler(MoveMotherMessage message) throws noTowerException, noTowersException {
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing their assistant", turnController.getActivePlayer());
        boolean result = turnController.moveMother(message.getMoves());
        broadcastGenericMessage("Mother Nature concluded her journey.");
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());

        if(result){

            virtualView.showGenericMessage("You've placed the last tower! You're the winner");
            win();

        }
        else{
            turnController.islandMerger(game.getGameBoard().getIslands().get(game.getGameBoard().getMotherNature()));
        }
        if(game.getGameBoard().getIslands().size()==3){
            broadcastGenericMessage("The game is finished! It's a draw!");
            draw();
        }
        virtualView.showGenericMessage("Please choose the cloud you want to take!");
        virtualView.askCloud(turnController.getActivePlayer(),game.getEmptyClouds());
        //passo le vuote poi la gestisco
        game.updateGameboard();

    }


    public void getFromCloudHandler(PickCloudMessage message){
        broadcastGenericMessage("Active player picking their cloud");
        turnController.getFromCloud(message.getCloudIndex());
        if(game.getEmptyClouds().size()==game.getChosenPlayerNumber()){
            broadcastGenericMessage("All players have moved! Starting a new turn");
            turnController.newTurn();

        }
        else{
            broadcastGenericMessage("Player finished their turn!");
            turnController.next();
            initiateAction();
        }
        game.updateGameboard();
    }

    public void win(){
        broadcastWinMessage(turnController.getActivePlayer());
        endGame();
    }


    public void draw(){
        int max = 0;
        String winner=turnController.getActivePlayer();
        for(Player p : game.getPlayers()){
            if(p.getProfessors().size()>max){
                max = p.getProfessors().size();
                winner = p.getName();
            }
        }
        turnController.setActivePlayer(winner);
        win();
    }


    public void endGame() {
        game.resetInstance();
        StorageData storageData = new StorageData();
        storageData.delete();
        initGameController();
        Server.LOGGER.info("Game finished. Server ready for a new Game.");
    }

    public TurnController getTurnController() {
        return turnController;
    }

    public boolean checkLoginNickname(String nickname, View view) {
        return inputController.checkLoginNickname(nickname, view);
    }

    public void broadcastGenericMessage(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    public void broadcastGenericMessage(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
        }
    }



    //METODI VV
    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);
        //non mettiamo observer sul game ma sulla gameboard e sul player e sul cloud.....
        //game.removeObserver(vv);
        game.getGameBoard().removeObserver(vv);
        game.removePlayerByNickname(nickname, notifyEnabled);
    }

    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
        //if(virtualViewMap.size()>1) {
            //EasyGame easyGame = (EasyGame) game;
            //easyGame.addObserver(virtualView);
        //}
    }

    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    public void broadcastDisconnectionMessage(String nicknameDisconnected, String text) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(nicknameDisconnected, text);
        }
    }
    private void broadcastWinMessage(String winningPlayer) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showWinMessage(winningPlayer);
        }
    }
//    private void broadcastDrawMessage() {
//        for (VirtualView vv : virtualViewMap.values()) {
//            vv.showDrawMessage();
//        }
//    }

    private void restoreControllers(GameController savedGameController) {
        Gameboard restoredBoard = savedGameController.game.getGameBoard();
        List<Player> restoredPlayers = savedGameController.game.getPlayers();
        List<Character> restoredExperts = savedGameController.game.getExperts();
        int restoredChoosenPlayerNumber = savedGameController.game.getChosenPlayerNumber();
        this.game.restoreGame(restoredBoard, restoredPlayers, restoredExperts, restoredChoosenPlayerNumber);

        this.turnController = savedGameController.turnController;
        this.gameState = savedGameController.gameState;

        // set this gameController as Observer of all effects of all gods of all players.
        //for (int i = 0; i < game.getNumCurrentPlayers(); i++) {
          //  game.getPlayerByNickname(turnController.getNicknameQueue().get(i)).getGod().addObserverToAllEffects(this);
        //}

        inputController = new InputController(this.virtualViewMap, this, this.game);
        turnController.setVirtualViewMap(this.virtualViewMap);
    }

    public Mode getGame(){
        return this.game;
    }

    public GameState getGameState() {return this.gameState;}

}
