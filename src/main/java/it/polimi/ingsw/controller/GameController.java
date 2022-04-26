package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.fullTowersException;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.answers.DeckMessage;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.message.MessageType.PLAYERNUMBER_REPLY;
import static it.polimi.ingsw.network.message.MessageType.GAMEMODE_REPLY;


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


    public GameController() {
        this.gameFactory = new GameFactory();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this);
        setGameState(GameState.LOGIN);

    }


    public void onMessageReceived(Message receivedMessage) {

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
                broadcastGenericMessage("Waiting for other Players. . .");
            }
        }
         else{
             Server.LOGGER.warning("Wrong message received from client");
         }
    }

    private void loginState(Message receivedMessage) {
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

    private void initState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            case GODLIST:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    godListHandler((GodListMessage) receivedMessage, virtualView);
                }
                break;
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
        player.setDeck(receivedMessage.getMage());
        Mage.choose(receivedMessage.getMage());
        //posso mandare un messaggio di conferma


    }

    private void towerHandler(TowerMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        player.setGroup(receivedMessage.getType());
        Type.choose(receivedMessage.getType());
        //askWorkersPositions(receivedMessage.getNickname());

    }

    private void startHandler(StartMessage receivedMessage) throws noMoreStudentsException, fullTowersException {
        game.initializeGameboard();
        initializeDashboards();
        if (Mage.notChosen().size() != Game.MAX_PLAYERS - game.getChosenPlayersNumber()) {
            turnController.next();
            VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
            vv.askInitDeck(Mage.notChosen());
        }
        else if (Type.notChosen().size() != Game.MAX_PLAYERS - game.getChosenPlayersNumber()) {
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
