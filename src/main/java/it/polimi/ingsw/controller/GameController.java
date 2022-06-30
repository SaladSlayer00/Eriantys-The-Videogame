

package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.StorageData;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.message.MessageType.PLAYERNUMBER_REPLY;
import static it.polimi.ingsw.message.MessageType.GAMEMODE_REPLY;


/**
 * This class represents the action handler of the game, and is responsible for the management of
 * the players' input, calls to the turn controller, setup and endgame
 */


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
    public static int moves;


    /**
     * Constructor calls the method to setup parameters
     */

    public GameController(){
        initGameController();
    }


    /**
     * Initialization method to setup base game parameters
     */

    public void initGameController() {
        this.gameFactory = new GameFactory();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this, null);
        setGameState(GameState.SET_MODE);

    }


    /**
     * Getter for gameMode
     *
     * @return the game's gameMode
     */
    public modeEnum getGameMode() {
        return gameMode;
    }

    /**
     * Returns true if the game's started
     *
     * @return boolean value for operation's outcome
     */
    public boolean isGameStarted() {
        return gameState.equals(GameState.IN_GAME);
    }

    /**
     * Setter for the gameState
     *
     * @param gameState the new gameState
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    /**
     * State machine to call the right method based on the state te game's in
     *
     * @param receivedMessage the message sent by the player
     * @throws invalidNumberException forbidden input number
     * @throws fullTowersException too manytowers on dashboard
     * @throws noStudentException no students in sack
     * @throws noTowerException no towers on dashboard
     * @throws emptyDecktException player's deck is empty
     */


    public void onMessageReceived(Message receivedMessage) throws invalidNumberException, fullTowersException, noStudentException, noTowerException, noTowersException {

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
                    try {
                        try {
                            initState(receivedMessage, virtualView);
                        } catch (noMoreStudentsException e) {
                            draw();
                            return;
                        }
                    } catch (maxSizeException e) {
                        turnController.moveMaker();
                        return;
                    }
                }
                break;
            case IN_GAME:
                if (inputController.checkUser(receivedMessage)) {
                    try {
                        try {
                            try {
                                inGameState(receivedMessage);
                            } catch (emptyDecktException e) {
                                draw();
                                return;
                            }
                        } catch (noMoreStudentsException e) {
                            draw();
                            return;
                        }
                    } catch (maxSizeException e) {
                        turnController.moveMaker();
                        return;
                    }
                }
                break;
            default: // Should never reach this condition
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }


    /**
     * State of the game in which the parameter for the game mode is initialized
     *
     * @param receivedMessage it's a GAMEMODE_REPLY that contains the selected gameMode
     */

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

    /**
     * State of the game in which the parameter for the players number is initialized
     *
     * @param receivedMessage a PLAYERNUMBER_REPLY that contains the selected player number
     * @throws invalidNumberException if the number of players is not allowed
     */

    private void loginState(Message receivedMessage) throws invalidNumberException {
        if (receivedMessage.getMessageType() == PLAYERNUMBER_REPLY) {
            if (inputController.verifyReceivedData(receivedMessage)) {
                this.game = gameFactory.getMode(gameFactory.getType(), ((PlayerNumberReply) receivedMessage).getPlayerNumber());
                this.inputController.setGame(game);
                game.initializePlayer(new Player(receivedMessage.getNickname(), 1));
                EasyGame easyGame = (EasyGame) game;
                easyGame.addObserver(virtualViewMap.get(receivedMessage.getNickname()));
                broadcastGenericMessage("Waiting for other Players . . .");
                game.lobbyUpdate();
                if(game.getChosenPlayerNumber()==2){
                    Type.choose(Type.GREY);
                    moves = 3;
                }
                else{
                    moves = 4;
                }
            }
        }
        else {
            Server.LOGGER.warning("Wrong message received from client.");
        }
    }


    /**
     * Method that handles the connection of a new client, adds a player to the virtualView list
     * and sets the saved matches
     *
     * @param nickname nickname of the player
     * @param ID player id
     * @param virtualView virtual view of the player
     * @throws noMoreStudentsException if there's no more students in the sack
     */


    public void loginHandler(String nickname, int ID, VirtualView virtualView) throws noMoreStudentsException {

        if (virtualViewMap.isEmpty()) { // First player logged. Ask number of players.
            addVirtualView(nickname, virtualView);
            virtualView.showLoginResult(true, true, "server");
            virtualView.askGameMode(nickname, modeEnum.availableGameModes());

        } else if (game.getActives() < game.getChosenPlayerNumber() ) {
            if(virtualViewMap.get(nickname)!=null){
                Server.LOGGER.info("Player present");
                turnController.getNicknameQueue().add(nickname);
                virtualViewMap.remove(nickname);
                addVirtualView(nickname, virtualView);
                game.setActives(1);
            }
            else {
                addVirtualView(nickname, virtualView);
                game.initializePlayer(new Player(nickname, ID));
                virtualView.showLoginResult(true, true, "server");
            }

            if (game.getNumCurrentActivePlayers() == game.getChosenPlayerNumber()) { // If all players logged


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
            broadcastGenericMessage("Waiting for other Players . . .");
            virtualView.showLoginResult(true, false, "server");
        }
    }

    /**
     * Broadcasts restore messages when a saved game is found
     */

    private void broadcastRestoreMessages() {
        for (VirtualView vv : virtualViewMap.values()) {
            ArrayList<Dashboard> dashboards = new ArrayList<Dashboard>();
            for(Player p: game.getPlayers()){
                dashboards.add(p.getDashboard());
            }
            vv.updateTable(game.getGameBoard(), dashboards,game.getPlayers());
        }

        for (VirtualView vv : virtualViewMap.values()) {

            vv.showMatchInfo(game.getChosenPlayerNumber(), game.getNumCurrentActivePlayers());
        }
    }


    /**
     * Method to initialize the game when the number of players is reached,
     * calls the method to ask the first player for their deck
     */

    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this, this.game);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing their deck. . .");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askInitDeck(turnController.getActivePlayer(),Mage.notChosen());
    }


    /**
     * Switch on message type for the first phase of the game so that the right method's called
     *
     * @param receivedMessage the message received form client
     * @param virtualView the client's virtual view
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if the dashboard's full
     * @throws maxSizeException if the max size's reached
     */

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
                    startHandler((StartMessage) receivedMessage);
                }
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    /**
     * Sets the deck for the player that sent the message, calls to the next one or to the
     * next parameter
     *
     * @param receivedMessage the message received from the player
     */

    private void deckHandler(DeckMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        if (Mage.notChosen().size()>4-game.getChosenPlayerNumber()+1){
            player.setDeck(receivedMessage.getMage());
            Mage.choose(receivedMessage.getMage());
            virtualView.showGenericMessage("You chose your deck. Please wait for the other players to pick!");
            askDeckToNextPlayer();
        }
        else if(Mage.notChosen().size()==1){
            virtualView.showGenericMessage("Your mage calls you! You have the " + Mage.notChosen().get(0) + " deck!");
            player.setDeck(Mage.notChosen().get(0));
            virtualView.showGenericMessage("It's your turn now. Please pick your team.");
            virtualView.askInitType(turnController.getActivePlayer(),Type.notChosen());
        }
        else{
            player.setDeck(receivedMessage.getMage());
            Mage.choose(receivedMessage.getMage());
            virtualView.showGenericMessage("You chose your deck");
            virtualView.showGenericMessage("It's your turn now. Please pick your team.");
            virtualView.askInitType(turnController.getActivePlayer(),Type.notChosen());
        }

    }

    /**
     * Sends a deck request to the next client
     */

    private void askDeckToNextPlayer() {
        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your deck.");
        virtualView.askInitDeck(turnController.getActivePlayer(),Mage.notChosen());
    }

    /**
     * Asks the tower to the next player
     */

    private void askTowerToNextPlayer(){

        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your team.");
        virtualView.askInitType(turnController.getActivePlayer(),Type.notChosen());

    }

    /**
     * Sets the tower to the player that called the method, asks to start the game when
     * everyone's set
     *
     * @param receivedMessage the message received from the player
     */

    private void towerHandler(TowerMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        if(Type.notChosen().size()>1){
            player.getDashboard().setTeam(receivedMessage.getType());
            Type.choose(receivedMessage.getType());
            virtualView.showGenericMessage("You chose your team. Please wait for the other players to pick!");
            askTowerToNextPlayer();

        }
        else{
            virtualView.showGenericMessage("Your towers call you! You're in the " + Type.notChosen().get(0) + " team!");
            player.getDashboard().setTeam(Type.notChosen().get(0));
            broadcastGenericMessage("All decks and teams are set! The mode of the game is " + gameMode +
                    " and the number of players is "+ this.game.getChosenPlayerNumber() + ".");
            virtualView.showGenericMessage("Are you sure you want to start the game with these settings?");
            virtualView.askStart(turnController.getActivePlayer(), "START");
        }

    }

    /**
     * Asks the player if they're ready to start the game
     *
     * @param receivedMessage the message received from the player
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if the towers are full
     * @throws maxSizeException if max size's reached
     */

    private void startHandler(StartMessage receivedMessage) throws noMoreStudentsException, fullTowersException, maxSizeException {
            game.initializeGameboard();
            game.initializeDashboards();
            startGame();

    }

    /**
     * Sets the gameMode
     *
     * @param gameMode chosen gameMode
     */
    public void setGameMode(modeEnum gameMode) {
        this.gameMode = gameMode;
    }


    /**
     * Method that starts the game initializing the expert mode (if present), updating the gameboard
     * and notifying the players
     *
     * @throws noMoreStudentsException if there's no more students in the sack
     */


    private void startGame() throws noMoreStudentsException {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");
        game.getGameBoard().setMode(gameFactory.getType());
        if(gameMode.equals(modeEnum.EXPERT)){
            expertSetup();
        }
        game.updateGameboard();
        turnController.broadcastMatchInfo();
        turnController.newTurn();

    }

    /**
     * State switching over the two game phases
     *
     * @param receivedMessage the message received from the client
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws noStudentException if no students match the selected color
     * @throws noTowerException if there's no tower on the dashboard
     * @throws maxSizeException if max size's reached
     * @throws noTowersException if there are no towers left
     * @throws emptyDecktException if the deck's empty
     * @throws fullTowersException if there are all towers on the dashboard
     * @throws invalidNumberException if the chosen number is invalid
     */

    private void inGameState(Message receivedMessage) throws noMoreStudentsException, noStudentException, noTowerException, maxSizeException, noTowersException, emptyDecktException, fullTowersException, invalidNumberException {
        switch (turnController.getMainPhase()){
            case PLANNING:
                planningState(receivedMessage);
                break;
            case ACTION:
                actionState(receivedMessage);
                break;

        }
    }

    /**
     * State switching over the different planning phase sections based on the message received
     * from client
     *
     * @param receivedMessage the message received from the client
     * @throws noMoreStudentsException if there are no more students in the sack
     * @throws emptyDecktException if the player'sdeck is empty
     */


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

    /**
     * State switching over the different action phase sections based on the message received
     * from client
     *
     * @param receivedMessage the message received from client
     * @throws noTowerException if there's no tower on island
     * @throws noStudentException if there's no students matching
     * @throws maxSizeException if max size's reached
     * @throws noTowersException if there are no towers left
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws emptyDecktException if the player's deck is empty
     * @throws fullTowersException if the tower's number is full
     * @throws invalidNumberException if the selected number is invalid
     */

    private void actionState(Message receivedMessage) throws noTowerException, noStudentException, maxSizeException, noTowersException, noMoreStudentsException, emptyDecktException, fullTowersException, invalidNumberException {
        switch (receivedMessage.getMessageType()) {
            case MOVE_ON_ISLAND:
                    moveHandler((MoveMessage) receivedMessage);
                break;
            case MOVE_ON_BOARD:
                    moveHandler((MoveMessage) receivedMessage);
                break;
            case MOVE_MOTHER:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    motherHandler((MoveMotherMessage) receivedMessage);
                }
                break;

            case PICK_CLOUD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    getFromCloudHandler((PickCloudMessage) receivedMessage);
                }
                break;

            case USE_EXPERT:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    expertHandler((ExpertMessage) receivedMessage);
                }
                break;

            case INIT_GAMEBOARD:
                 StartMessage startMessage = (StartMessage) receivedMessage;
                 Character car=null;
                 for(Character c : turnController.getToReset()){
                     if(c.getName().equals(ExpertDeck.MUSICIAN) || c.getName().equals(ExpertDeck.JOKER)){
                         car=c;
                     }
                 }
                 if(startMessage.getAnswer().equalsIgnoreCase("yes")) {
                     car.useEffect();
                 }
                 else{
                     car.removeEffect();
                     //turnController.moveMaker();
                 }
                break;

            case ENABLE_EFFECT:
                EffectMessage effectMessage = (EffectMessage) receivedMessage;
                turnController.effectHandler(effectMessage);
                break;

            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    /**
     * Method that initializes the cloud selected by the player and asks for another one
     * or calls the method to initialize drawing phase
     *
     * @param receivedMessage the message received from the client
     * @throws noMoreStudentsException if there's no more students in the sack
     */
    private void pickCloudHandler(PickCloudMessage receivedMessage) throws noMoreStudentsException {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        turnController.cloudInitializer(receivedMessage.getCloudIndex());
        game.updateGameboard();
        if(game.getNoMoreStudents()){
            broadcastGenericMessage("There are no more students in the sack! The game's over.");
            draw();
        }
        if(game.getEmptyClouds().size() >= 1){
            turnController.pickCloud();
        }

        else if (game.getEmptyClouds().size() == 0) {
            turnController.resetChosen();
            turnController.drawAssistant();

        }

    }

    /**
     * Method that sets the assistant selected from the player and asks the next one or initiates
     * the action with a call to turnController, notifies the game's observers
     *
     * @param receivedMessage the message received from client
     * @throws emptyDecktException if the deck's empty
     */

    private void drawAssistantHandler(AssistantMessage receivedMessage) throws emptyDecktException {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        Assistant card = player.getDeck().draw(receivedMessage.getIndex());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        player.setCard(card);
        turnController.getChosen().add(card);
        if(player.getDeck().getNumCards()==0){
            broadcastGenericMessage("Game finished! It's a draw!");
            draw();
        }

        if(turnController.getChosen().size() < game.getActives()){
            turnController.next();
            turnController.drawAssistant();
        }
        else{
            turnController.determineOrder();
            initiateAction();
        }
        game.updateGameboard();

    }

    /**
     * Sends a message to the views indicating the playing client and calls turnController
     * method for moves
     */

    public void initiateAction(){
        broadcastGenericMessage("Now playing "+ turnController.getActivePlayer());
        turnController.moveMaker();
    }

    /**
     * Checks the move made from the player and calls the appropriate turnController methods,
     * asks the player for next action
     *
     * @param moveMessage the message received from client
     * @throws noStudentException if there's no selected student
     * @throws maxSizeException if max size's reached
     * @throws emptyDecktException if the player's deck is empty
     * @throws noMoreStudentsException if there's no more students in the sack
     * @throws fullTowersException if the tower's number is full
     * @throws noTowerException if there's no tower on island
     * @throws invalidNumberException if the selected number is invalid
     * @throws noTowersException if there's no towers on dashboard
     */

    public void moveHandler(MoveMessage moveMessage) throws noStudentException, maxSizeException, emptyDecktException, noMoreStudentsException, fullTowersException, noTowerException, invalidNumberException, noTowersException {
        if (moveMessage.getMessageType() == MessageType.MOVE_ON_BOARD) {
            turnController.moveOnBoard(moveMessage.getColor(), moveMessage.getRow());
            game.updateGameboard();
        }
        else if(moveMessage.getMessageType() == MessageType.MOVE_ON_ISLAND){
            turnController.moveOnIsland(moveMessage.getColor(), moveMessage.getIndex());
            game.updateGameboard();
        }
        if(turnController.getMoved()<moves){
            turnController.moveMaker();
            game.updateGameboard();
        }
        else{
            VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
            virtualView.showGenericMessage("Please choose the number of moves of mother nature");
            int extra = 0;
            for(Character c : turnController.getToReset()){
                if(c.getName().equals(ExpertDeck.GAMBLER)){
                    extra = 2;
                }
            }
            virtualView.askMotherMoves(turnController.getActivePlayer(),game.getPlayerByNickname(turnController.getActivePlayer()).getCardChosen().getMove()+extra);
            turnController.setMoved(0);
            game.updateGameboard();
        }
    }

    /**
     * Method that handles the moves for mother nature selected by the player, including the
     * winning scenario
     *
     * @param message the message received from client
     * @throws noTowerException if there's no tower on island
     * @throws noTowersException if there's no towers on the player's dashboard
     */
    public void motherHandler(MoveMotherMessage message) throws noTowerException, noTowersException {
        int result = turnController.moveMother(message.getMoves());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());

        if(result == 2){
            virtualView.showGenericMessage("You've placed the last tower! You're the winner");
            win();
        }
        else if(result == 1){
            turnController.islandMerger(game.getGameBoard().getIslands().get(game.getGameBoard().getMotherNature()));
        }
        if(game.getGameBoard().getIslands().size()==3){
            broadcastGenericMessage("The game is finished! It's a draw!");
            draw();
        }
        virtualView.showGenericMessage("Please choose the cloud you want to take!");
        VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askCloud(turnController.getActivePlayer(),game.getEmptyClouds());
        game.updateGameboard();

    }


    /**
     * Method that handles the cloud choice from the player and transfers students from cloud
     * to dashboard, starting a new turn or asking the next player for moves
     *
     * @param message the message received from client
     * @throws noMoreStudentsException if there's no more students in sack
     */

    public void getFromCloudHandler(PickCloudMessage message) throws noMoreStudentsException {
        turnController.getFromCloud(message.getCloudIndex());
        game.updateGameboard();
        if(game.getEmptyClouds().size()==game.getChosenPlayerNumber()){
            broadcastGenericMessage("All players have moved! Starting a new turn");
            turnController.newTurn();

        }
        else{
            turnController.next();
            initiateAction();

        }

    }

    /**
     * Method that calls the turnController method for expert handling when an expert card is
     * selected from player
     *
     * @param expertMessage the message received from client
     */

    public void expertHandler(ExpertMessage expertMessage){
        VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
        turnController.useExpertEffect(expertMessage.getCard());
    }

    /**
     * Endgame method to broadcast winning messages
     */

    public void win(){
        broadcastWinMessage(turnController.getActivePlayer());
        endGame();
    }

    /**
     * Endgame method that determines the winning player and broadcasts winning messages
     */

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

    /**
     * If expert mode is selected, this method initializes the list of expert cards and
     * gives one coin to the players
     */

    public void expertSetup(){
        ExpertDeck.choose(ExpertDeck.JOKER);
        ExpertDeck.choose(ExpertDeck.TAVERNER);
        ExpertDeck.choose(ExpertDeck.BARBARIAN);
        for(int i=0;i<0;i++) {
            int random = (int) (Math.random() * ExpertDeck.notChosen().size());
            ExpertDeck card = ExpertDeck.notChosen().get(random);
            game.getExperts().add(card);
            //card.setController(this);
            //card.setTurnController(turnController);
            ExpertDeck.choose(card);
            broadcastGenericMessage("Card chosen: " + card.getText() +"\n");
        }
        game.getExperts().add(ExpertDeck.JOKER);
        game.getExperts().add(ExpertDeck.TAVERNER);
        game.getExperts().add(ExpertDeck.BARBARIAN);

        turnController.getPrice().put(ExpertDeck.JOKER,0);
        turnController.getPrice().put(ExpertDeck.TAVERNER,0);
        turnController.getPrice().put(ExpertDeck.BARBARIAN,0);

        for(Player p : game.getPlayers()){
            p.addCoin(10);
            game.getGameBoard().removeCoin();
        }

    }

    /**
     * Endgame method that resets the game and the savefile for it
     */

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

    /**
     * Method that calls the inputController to check if the nickname's allowed
     *
     * @param nickname the nickname chosen by the player
     * @param view the view for the player
     * @return true if the nickname's allowed, false otherwise
     */
    public boolean checkLoginNickname(String nickname, View view) {
        return inputController.checkLoginNickname(nickname, view);
    }

    /**
     * Method that sends a message to every player in the game excluding one
     *
     * @param messageToNotify the message to send
     * @param excludeNickname the nickname to exclude
     */

    public void broadcastGenericMessage(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    /**
     * variant of other method that sends the message to every player in game
     * @param messageToNotify
     */
    public void broadcastGenericMessage(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
        }
    }

    /**
     * Method that removes a player's nickname when disconnecting, removes 1 from the player
     * number
     *
     * @param nickname the nickname to remove from playing list
     */

    public void removeNickname(String nickname){
        turnController.getNicknameQueue().remove(nickname);
        game.setActives(-1);
        if(nickname.equals(turnController.getActivePlayer())){
            turnController.next();
        }
    }

    /**
     * Method that removes a virtualView when disconnecting
     *
     * @param nickname the nickname of the player to remove
     * @param notifyEnabled boolean to notify observers
     */
    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);
        game.getGameBoard().removeObserver(vv);
        game.removePlayerByNickname(nickname, notifyEnabled);
    }

    /**
     * Method that adds a player's virtualView to the game as an observer
     *
     * @param nickname the player's nickname
     * @param virtualView the player's virtualView
     */

    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
        if(virtualViewMap.size()>1) {
            EasyGame easyGame = (EasyGame) game;
            easyGame.addObserver(virtualView);
        }
    }

    /**
     * Getter for virtual view map
     *
     * @return the map of the players' virtual views
     */
    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    /**
     * Broadcast of disconnection message for every player
     *
     * @param nicknameDisconnected the nickname of the disconnected player
     * @param text the text of the message
     */

    public void broadcastDisconnectionMessage(String nicknameDisconnected, String text) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(nicknameDisconnected, text);
        }
    }

    /**
     * Broadcast of win message that shpws the name of the player
     *
     * @param winningPlayer the nickname of the winner
     */
    private void broadcastWinMessage(String winningPlayer) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showWinMessage(winningPlayer);
        }
    }

    /**
     * Method that restores controllers after a server's disconnection when a saved game is found
     *
     * @param savedGameController the saved game controller
     */

    private void restoreControllers(GameController savedGameController) {
        Gameboard restoredBoard = savedGameController.game.getGameBoard();
        List<Player> restoredPlayers = savedGameController.game.getPlayers();
        int restoredChosenPlayerNumber = savedGameController.game.getChosenPlayerNumber();
        this.game.restoreGame(restoredBoard, restoredPlayers, restoredChosenPlayerNumber);
        this.turnController = savedGameController.turnController;
        this.gameState = savedGameController.gameState;
        turnController.setGameController(this);
        turnController.setGame(game);
        turnController.setToReset(savedGameController.turnController.getToReset());
        turnController.setRestored(1);
        broadcastGenericMessage("Game restored bitch\n");
        game.updateGameboard();
        inputController = new InputController(this.virtualViewMap, this, this.game);
        turnController.setVirtualViewMap(this.virtualViewMap);
    }

    /**
     * Getter for the game instance
     *
     * @return the game
     */
    public Mode getGame(){
        return this.game;
    }

    /**
     * Getter for gameState
     *
     * @return the state the game's in
     */
    public GameState getGameState() {return this.gameState;}
}
