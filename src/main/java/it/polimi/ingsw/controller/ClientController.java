package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.SocketClient;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.observation.BoardMessage;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Mode;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * This class is part of the client side.
 * It is an interpreter between the network and a generic view (which in this case can be CLI or GUI).
 * It receives the messages, wraps/unwraps and passes them to the network/view.
 */
public class ClientController implements ViewObserver, Observer {
    private final View view;
    private Client client;
    private String nickname;
    private final ExecutorService taskQueue;

    /**
     * Class constructor
     *
     * @param view an instance of view to check
     */

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    /**
     * method that updates the info from the server
     * @param serverInfo a map of server address and server port.
     */

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage();
            client.enablePinger(true);
            taskQueue.execute(view::askNickname);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, this.nickname));
        }
    }

    /**
     * method that updates the nickame to the server
     * @param nickname the nickname to be sent.
     */

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));

    }

    /**
     * method that updates the player number to the server
     * @param playersNumber the number of players.
     */

    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.nickname, playersNumber));

    }

    /**
     * method that updates the type of tower to the server
     * @param type the type of tower.
     */

    @Override
    public void OnUpdateInitTower(Type type) {
        client.sendMessage(new TowerMessage(this.nickname, type));
    }

    /**
     * method that updates the chosen deck to the server
     * @param deck the chosen deck.
     */
    @Override
    public void OnUpdateInitDeck(Mage deck) {
        client.sendMessage(new DeckMessage(this.nickname, deck));
    }

    /**
     * method that updates the chosen game mode to the server
     * @param gameMode the chosen game mode.
     */
    @Override
    public void OnUpdateGameMode(modeEnum gameMode) {
        client.sendMessage(new GameModeReply(this.nickname, gameMode));
    }


    /**
     * method that updates the chosen assistant to the server
     * @param assistant the chosen assistant.
     */
    @Override
    public void OnUpdateAssistant(Assistant assistant) {
        client.sendMessage(new AssistantMessage(this.nickname, assistant));
    }

    /**
     * method that updates the chosen island move to the server
     * @param color the color of the pawn
     * @param index the index of the island.
     * @param islands the islands list
     */
    @Override
    public void OnUpdateMoveOnIsland(Color color, int index, List<Island> islands) {
        client.sendMessage(new MoveMessage(this.nickname, color, index, islands));
    }

    /**
     * method that updates the chosen board move to the server
     * @param color the color of the student.
     * @param row the color of the row
     */
    @Override
    public void OnUpdateMoveOnBoard(Color color, Color row) {
        client.sendMessage(new MoveMessage(this.nickname, color, row));
    }

    /**
     * method that updates the chosen mother move to the server
     * @param index the index of the island
     * @param chosenAssistant the assistant selected
     */
    @Override
    public void OnUpdateMoveMother(int index, Assistant chosenAssistant) {
        client.sendMessage(new MoveMotherMessage(this.nickname, index, chosenAssistant));
    }

    /**
     * method that updates the chosen cloud index to the server
     * @param index index of cloud chosen
     */
    @Override
    public void OnUpdatePickCloud(int index) {
        client.sendMessage(new PickCloudMessage(this.nickname, index));
    }

    /**
     * method that updates the chosen cloud index to the server
     * @param index index from cloud chosen
     */
    @Override
    public void OnUpdateGetFromCloud(int index) {
        client.sendMessage(new PickCloudMessage(this.nickname, index));
    }

    /**
     * method that updates the chosen start answer to the server
     * @param answer answer to be sent
     */
    @Override
    public void OnStartAnswer(String answer){
        client.sendMessage(new StartMessage(this.nickname, answer));
    }

    /**
     * method that calls for the client's disconnection
     */
    @Override
    public void onDisconnection() {
        client.disconnect();

    }

    /**
     * method that updates the seller effect to the server
     * @param c color chosen
     */
    @Override
    public void OnUpdateEffectSeller(Color c){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.SELLER, c));
    }

    /**
     * method that updates the seller effect to the server
     * @param island index of island chosen
     */
    @Override
    public void OnUpdateEffectHerald(int island){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.HERALD, island));
    }

    /**
     * method that updates the herbalist effect to the server
     * @param island index of island chosen
     */
    @Override
    public void OnUpdateEffectHerbalist(int island){client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.HERBALIST,island));}

    /**
     * method that updates the chosen experrt card to the server
     * @param c expert card
     */
    @Override
    public void OnUpdateExpert(ExpertDeck c){ client.sendMessage(new ExpertMessage(this.nickname, c));}

    /**
     * method that updates the banker effect to the server
     * @param c color chosen
     */
    @Override
    public void OnUpdateEffectBanker(Color c){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.BANKER, c));
    }

    /**
     * method that updates the barbarian effect to the server
     * @param c color chosen
     */
    @Override
    public void OnUpdateEffectBarbarian(Color c){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.BARBARIAN, c));
    }

    /**
     * method that updates the musician effect to the server
     * @param c color chosen
     */
    @Override
    public void OnUpdateEffectMusician(Color c){
        client.sendMessage((new EffectMessage(this.nickname, ExpertDeck.MUSICIAN, c)));
    }

    /**
     * method that updates the joker effect to the server
     * @param c color chosen
     */
    @Override
    public void OnUpdateEffectJoker(Color c){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.JOKER, c));
    }

    /**
     * method that updates the taverner effect to the server (color version)
     * @param c color chosen
     */
    @Override
    public void OnUpdateEffectTaverner(Color c){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.TAVERNER, c));
    }

    /**
     * method that updates the taverner effect to the server (island version)
     * @param index idex of island chosen
     */
    @Override
    public void OnUpdateEffectTaverner(int index){
        client.sendMessage(new EffectMessage(this.nickname, ExpertDeck.TAVERNER, index));
    }

    /**
     * method that handles the execution of tasks for the views using a task queue, calling methods
     * according to the messages sent from server side
     * @param message the message sent from server
     */
    @Override
    public void update(Message message) {
        switch (message.getMessageType()) {
            case ASK_MOVE:
                AskMoveMessage askMoveMessage = (AskMoveMessage) message;
                taskQueue.execute(()->view.askMoves(askMoveMessage.getStudents(),askMoveMessage.getIslands()));
                break;
            case ASK_TOWER:
                TowerMessageRequest towerMessageRequest = (TowerMessageRequest) message;
                taskQueue.execute(() -> view.askInitType(this.nickname, towerMessageRequest.getTypes()));
                break;
            case ASK_DECK:
                DeckMessageRequest deckMessageRequest = (DeckMessageRequest) message;
                taskQueue.execute(() -> view.askInitDeck(this.nickname, deckMessageRequest.getDecks()));
                break;
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isNicknameAccepted(), loginReply.isConnectionSuccessful(), this.nickname));
                break;
            case GAMEMODE_REQUEST:
                GameModeRequest gameModeRequest = (GameModeRequest) message;
                taskQueue.execute(()->view.askGameMode(gameModeRequest.getNickname(), gameModeRequest.getModes()));
                break;
            case INIT_GAMEBOARD:
                StartMessage startMessage = (StartMessage) message;
                taskQueue.execute(()->view.askStart(message.getNickname(), "START"));
                break;
            case PLAYERNUMBER_REQUEST:
                taskQueue.execute(view::askPlayersNumber);
                break;
            case MOVE_MOTHER:
                taskQueue.execute(()->view.askMotherMoves(message.getNickname(),((MoveMotherMessage)message).getChosenAssistant().getMove()));
                //I'm not sure about that
                break;
            case PICKCLOUD_REQUEST:
                PickCloudMessageRequest pickCloudMessageRequest = (PickCloudMessageRequest) message;
                taskQueue.execute(()->view.askCloud(this.nickname ,(pickCloudMessageRequest).getClouds()));
                break;
            case ASSISTANT_REQUEST:
                AssistantMessageRequest assistantMessageRequest = (AssistantMessageRequest) message;
                taskQueue.execute(()->view.askAssistant(this.nickname ,assistantMessageRequest.getAssistants()));
                break;
            case MATCH_INFO:
                MatchInfoMessage matchInfoMessage = (MatchInfoMessage) message;
                taskQueue.execute(() -> view.showMatchInfo(
                        matchInfoMessage.getActivePlayers(),
                        matchInfoMessage.getActivePlayerNickname()));
                break;
            case BOARD:
                BoardMessage boardMessage = (BoardMessage) message;
                taskQueue.execute(()-> {
                    try {
                        view.updateTable(boardMessage.getBoard(),boardMessage.getDashboards() ,boardMessage.getPlayers());
                    } catch (noTowersException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case GENERIC_MESSAGE:
                GenericMessage genericMessage = (GenericMessage) message;
                taskQueue.execute(()->view.showGenericMessage(genericMessage.getMessage()));
                break;
            case ERROR:
                ErrorMessage errorMessage = (ErrorMessage) message;
                taskQueue.execute(()->view.errorCommunicationAndExit(message.getNickname()));
            case DISCONNECTION:
                try {
                    DisconnectionMessage disconnectionMessage = (DisconnectionMessage) message;
                    taskQueue.execute(()->view.errorCommunicationAndExit(this.nickname));
                    client.disconnect();
                    view.showDisconnectionMessage(disconnectionMessage.getNicknameDisconnected(), disconnectionMessage.getMessageStr());
                }catch (ClassCastException c){
                    taskQueue.execute(()->view.errorCommunicationAndExit(this.nickname));
                    view.showDisconnectionMessage(this.nickname, "Disconnected from server\n");
                    client.disconnect();
                }
                break;
            case WIN_FX:
                WinMessage winMessage = (WinMessage) message;
                client.disconnect();
                view.showWinMessage(winMessage.getWinnerNickname());
                break;
            case USE_EXPERT:
                ExpertMessage expertMessage = (ExpertMessage) message;
                taskQueue.execute(()->view.askExpert());
                break;
            case LOBBY:
                LobbyMessage lobbyMessage = (LobbyMessage) message;
                taskQueue.execute(() -> view.showLobby(lobbyMessage.getNicknameList(), lobbyMessage.getMaxPlayers()));
                break;
            case COLOR_MESSAGE:
                ColorMessage colorMessage = (ColorMessage) message;
                taskQueue.execute(()->view.askColor());
                break;
            default:
                break;
        }

    }

    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip the string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Checks if the given port string is in the range of allowed ports.
     *
     * @param portStr the ports to be checked.
     * @return {@code true} if the port is valid, {@code false} otherwise.
     */
    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}


