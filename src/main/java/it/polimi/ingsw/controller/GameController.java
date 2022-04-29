package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.fullTowersException;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Server;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.message.MessageType.PLAYERNUMBER_REPLY;
import static it.polimi.ingsw.message.MessageType.GAMEMODE_REPLY;


//il game controller può occuparsi delle azioni che riguardano l'azione sul gioco complessivo
public class GameController implements Observer, Serializable {
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


    public GameController() {
        this.gameFactory = new GameFactory();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this);
        setGameState(GameState.LOGIN);

    }


    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void onMessageReceived(Message receivedMessage) throws invalidNumberException {

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
                broadcastGenericMessage("Waiting for other Players. . .");
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
            game.getPlayers().add(new Player(nickname, ID));

            virtualView.showLoginResult(true, true, "server");
            virtualView.askPlayersNumber();

        } else if (virtualViewMap.size() < game.getChosenPlayersNumber()) {
            addVirtualView(nickname, virtualView);
            game.getPlayers().add(new Player(nickname, ID));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);

            if (game.getNumCurrentActivePlayers() == game.getChosenPlayersNumber()) { // If all players logged

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

    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this, this.game);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing their deck. . .");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askInitDeck(Mage.notChosen(), game.getChosenPlayersNumber());
    }


    private void initState(Message receivedMessage, VirtualView virtualView) {
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


    private void deckHandler(DeckMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        if (Mage.notChosen().size() > 1){

            player.setDeck(receivedMessage.getMage());
            Mage.choose(receivedMessage.getMage());
        }
        else if(Mage.notChosen().size()==1){
            virtualView.showGenericMessage("Your mage calls you! You have the " + Mage.notChosen().get(0) + " deck!");
            player.setDeck(Mage.notChosen().get(0));
        }
        //controllo che non sia già preso, potrebbe farlo nell'input controller
        if (!Mage.isEmpty()) {
            virtualView.showGenericMessage("You chose your deck. Please wait for the other players to pick!");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked their deck.", turnController.getActivePlayer());
            askDeckToNextPlayer();

        }
        else{
            virtualView.showGenericMessage("It's your turn now. Please pick your team.");
            virtualView.askInitTeam(Type.notChosen());
        }

    }

    private void askDeckToNextPlayer() {
        turnController.next();
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his team...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your deck.");
        virtualView.askInitDeck(Mage.notChosen());

    }

    private void askTowerToNextPlayer(){

        turnController.next();
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his team...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your team.");
        virtualView.askInitType(Type.notChosen());

    }

    private void towerHandler(TowerMessage receivedMessage) {
        if(Type.notChosen().size() > 1){
            Player player = game.getPlayerByNickname(receivedMessage.getNickname());
            player.getDashboard().setTeam(receivedMessage.getType());
            Type.choose(receivedMessage.getType());

        }
        else if(Type.notChosen().size()==1){
            virtualView.showGenericMessage("Your towers call you! You're in the " + Type.notChosen().get(0) + " team!");
            player.setDeck(Type.notChosen().get(0));
        }
        if (!Type.isEmpty()) {
            virtualView.showGenericMessage("You chose your deck. Please wait for the other players to pick!");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked their deck.", turnController.getActivePlayer());
            askTowerToNextPlayer();

        }
        else{
            broadcastGenericMessage("All decks and teams are set! The mode of the game is " + gameMode +
                    " and the number of players is "+ this.game.getChosenPlayerNumber() + ".");
            virtualView.showGenericMessage("Are you sure you want to start the game with these settings?");
            //yes or no
            virtualView.askStart();
        }


    }

    private void startHandler(StartMessage receivedMessage) throws noMoreStudentsException, fullTowersException {

        if (Mage.notChosen().size() != MAX_PLAYERS - game.getChosenPlayersNumber()) {
            turnController.next();
            VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
            vv.askInitDeck(Mage.notChosen());
        }
        else if (Type.notChosen().size() != MAX_PLAYERS - game.getChosenPlayersNumber()) {
            turnController.next();
            VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
            vv.askInitType(Type.notChosen());
        }
        else if(receivedMessage.getAnswer().toUppercase().equals("NO")){
            //chiedi al player cosa vuole editare
        }
        else {
            turnController.next();
            game.initializeGameboard();
            initializeDashboards();
            startGame();
        }

    }
    //mette il numero di torri giosto in base al numero che sarà assegnato in fase di inizializzazione
    public void initializeDashboards() throws fullTowersException {
        for(Player p : game.getPlayers()){
            for(int i = 0 ; i < p.getDashboard().getNumTowers(); i++){
                p.getDashboard().putTower();
            }
        }

    }

    public void setGameMode(modeEnum gameMode) {
        this.gameMode = gameMode;
    }

    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");
        turnController.broadcastMatchInfo();
        turnController.newTurn();
    }

    private void inGameState(Message receivedMessage){
        switch (turnController.getMainPhase()){
            case PLANNING:
                planningState(receivedMessage);
                break;
            case ACTION:
                actionState(receivedMessage);
                break;

        }
    }

    private void planningState(Message receivedMessage){
        switch(receivedMessage.getMessageType()){
            case PICK_CLOUD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    pickCloudHandler(((PickCloudMessage)receivedMessage));
                }
                break;
            case DRAW_ASSISTANT:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    drawAssistantHandler((drawAssistantMessage)receivedMessage);
                }
                break;

        }
    }

//gli handler della azione richiamano il turnController
    private void ActionState(Message receivedMessage) {
        switch (receivedMessage.getMessageType()) {
            case PICK_MOVING_WORKER:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    pickWorkerHandler(receivedMessage);
                }
                break;
            case MOVE:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    moveHandler((PositionMessage) receivedMessage);
                }
                break;
            case BUILD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    buildHandler((PositionMessage) receivedMessage);
                }
                break;
            case ENABLE_EFFECT:
                prepareEffect((PrepareEffectMessage) receivedMessage);
                break;
            case APPLY_EFFECT:
                applyEffect((PositionMessage) receivedMessage);
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    //la prima volta è richiamato nello startTurn, questo è quando arriva l'altro messaggio
    private void pickCloudHandler(PickCloudMessage receivedMessage) {
        //Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        //quello che manda deve essere activeplayer dove lo controlla??
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        turnController.cloudInitializer(receivedMessage.getCloudIndex());//metodo per prendere l'indice cloud nel messaggio
        //sarà da scrivere il messaggio col giusto formato
        if(game.getEmptyClouds().size() > 1){
            virtualView.showGenericMessage("Please pick the cloud you want to setup. ");
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is picking the clouds.", turnController.getActivePlayer());
            turnController.pickCloud();
        }

        else if (game.getEmptyClouds().size() == 0) {
            virtualView.showGenericMessage("You chose every cloud!");
            broadcastGenericMessage("All clouds are set! Ready to initiate drawing phase!");
            turnController.pickDeck();
        }

    }

}
