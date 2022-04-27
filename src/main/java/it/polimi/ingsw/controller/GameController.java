package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.fullTowersException;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.*;
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
                this.setModeState((GameModeReply) receivedMessage.getGameMode());
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

        turnController = new TurnController(virtualViewMap, this);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing their deck. . .");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askInitDeck(Mage.notChosen(), game.getChosenPlayersNumber());
    }


    private void initState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            case PICK_FIRST_PLAYER:
                if (inputController.checkFirstPlayerHandler(receivedMessage)) {
                    pickFirstPlayerHandler(((MatchInfoMessage) receivedMessage).getActivePlayerNickname());
                }
                break;

            case INIT_DECK:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    deckHandler((DeckMessage) receivedMessage);
                }
                break;

            case ASK_TEAM:
                if(inputController.verifyReceivedData(receivedMessage)){
                    pickTeamHandler(((MatchInfoMessage) receivedMessage).getActivePlayerNickname());
                }

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
    private void pickFirstPlayerHandler(String firstPlayerNick) {

        turnController.setActivePlayer(firstPlayerNick);

        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his deck...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your deck.");
        virtualView.askInitDeck(Mage.notChosen());
    }

    private void pickTeamHandler(String firstPlayerNick){

        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his team...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your team.");
        virtualView.askInitType(Type.notChosen());
    }

    private void deckHandler(DeckMessage receivedMessage) {
        if (Mage.notChosen().size() > 1){
            Player player = game.getPlayerByNickname(receivedMessage.getNickname());
            player.setDeck(receivedMessage.getMage());
            Mage.choose(receivedMessage.getMage());
            broadcastGenericMessage("Deck set for player " + turnController.getActivePlayer()
                    + ". Waiting for other players to pick...");

            askDeckToNextPlayer();
            //posso mandare un messaggio di conferma
        }
        //controllo che non sia già preso, potrebbe farlo nell'input controller



    }

    private void askDeckToNextPlayer() {
        // ask deck to the next player
        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askInitDeck(Mage.notChosen()); // Only 1 god requested to client.
    }

    private void askTowerToNextPlayer(){
            turnController.next();
            VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
            virtualView.askInitType(Type.notChosen()); // Only 1 god requested to client.

    }

    private void towerHandler(TowerMessage receivedMessage) {
        if(Type.notChosen().size() > 1){
            Player player = game.getPlayerByNickname(receivedMessage.getNickname());
            player.getDashboard().setTeam(receivedMessage.getType());
            Type.choose(receivedMessage.getType());
            broadcastGenericMessage("Deck set for player " + turnController.getActivePlayer()
                    + ". Waiting for other players to pick...");

            askTowerToNextPlayer();
            //askWorkersPositions(receivedMessage.getNickname());
        }


    }

    private void startHandler(StartMessage receivedMessage) throws noMoreStudentsException, fullTowersException {
        game.initializeGameboard();
        initializeDashboards();
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
        else {
            turnController.next();
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

    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");
        turnController.broadcastMatchInfo();
        turnController.newTurn();
    }

}
