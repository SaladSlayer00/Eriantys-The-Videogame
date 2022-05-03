package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.emptyDecktException;
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

    public void onMessageReceived(Message receivedMessage) throws invalidNumberException, noMoreStudentsException, fullTowersException {

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


    private void initState(Message receivedMessage, VirtualView virtualView) throws noMoreStudentsException, fullTowersException {
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
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());

        if(Type.notChosen().size() > 1){
            player.getDashboard().setTeam(receivedMessage.getType());
            Type.choose(receivedMessage.getType());

        }
        else if(Type.notChosen().size()==1){
            virtualView.showGenericMessage("Your towers call you! You're in the " + Type.notChosen().get(0) + " team!");
            player.getDashboard().setTeam(Type.notChosen().get(0));
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
        else if(receivedMessage.getAnswer().equalsIgnoreCase("NO")){
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

    private void inGameState(Message receivedMessage) throws noMoreStudentsException {
        switch (turnController.getMainPhase()){
            case PLANNING:
                planningState(receivedMessage);
                break;
            case ACTION:
                actionState(receivedMessage);
                break;

        }
    }

    private void planningState(Message receivedMessage) throws noMoreStudentsException {
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
            case MOVE_ON_ISLAND:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    moveHandler((MoveOnIslandMessage) receivedMessage);
                }
                break;
            case MOVE_ON_BOARD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    moveHandler((MoveOnBoardMessage) receivedMessage);
                }
                break;
            case MOVE_MOTHER:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    motherHandler((MoveMotherMessage) receivedMessage);
                }
                break;

            case GET_FROM_CLOUD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    getFromCloudHandler((GetFromCloudMessage) receivedMessage);
                }
                break;

            case ENABLE_EFFECT:
                prepareEffect((PrepareEffectMessage) receivedMessage);
                break;
            case APPLY_EFFECT:
                applyEffect((PositionMessage) receivedMessage);
                break;

            case USE_EXPERT:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    expertHandler((UseExpertMessage) receivedMessage);
                }
                break;

            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    //la prima volta è richiamato nello startTurn, questo è quando arriva l'altro messaggio
    private void pickCloudHandler(PickCloudMessage receivedMessage) throws noMoreStudentsException {
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
        player.setCard(card);
        turnController.getChosen().add(card);

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
        }


    }

}
