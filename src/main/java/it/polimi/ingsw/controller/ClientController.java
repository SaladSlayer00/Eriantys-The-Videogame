package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.SocketClient;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Mode;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * This class is part of the client side.
 * It is an interpreter between the network and a generic view (which in this case can be CLI or GUI).
 * It receives the messages, wraps/unwraps and pass them to the network/view.
 */
public class ClientController implements ViewObserver, Observer {
    private final View view;
    private Client client;
    private String nickname;
    private final ExecutorService taskQueue;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

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

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));

    }

    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.nickname, playersNumber));

    }

    @Override
    public void OnUpdateInitTower(Type type) {
        client.sendMessage(new TowerMessage(this.nickname, type));
    }

    @Override
    public void OnUpdateInitDeck(Mage deck) {
        client.sendMessage(new DeckMessage(this.nickname, deck));
    }

    @Override
    public void OnUpdateGameMode(modeEnum gameMode) {
        client.sendMessage(new GameModeReply(this.nickname, gameMode));
    }

    @Override
    public void OnUpdateAssistant(Assistant assistant) {
        client.sendMessage(new AssistantMessage(this.nickname, assistant));
    }

    @Override
    public void OnUpdateMoveOnIsland(Color color, int index) {
        client.sendMessage(new MoveMessage(this.nickname, color, index));
    }

    @Override
    public void OnUpdateMoveOnBoard(Color color, Color row) {
        client.sendMessage(new MoveMessage(this.nickname, color, row));
    }

    @Override
    public void OnUpdateMoveMother(int index, Assistant chosenAssistant) {
        client.sendMessage(new MoveMotherMessage(this.nickname, index, chosenAssistant));
    }

    @Override
    public void OnUpdatePickCloud(int index) {
        client.sendMessage(new PickCloudMessage(this.nickname, index));
    }

    @Override
    public void OnUpdateGetFromCloud(int index) {
        client.sendMessage(new PickCloudMessage(this.nickname, index));
    }


    @Override
    public void onDisconnection() {
        client.disconnect();

    }


    @Override
    public void update(Message message) {
        switch (message.getMessageType()) {
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
            default:
                break;
            //TODO DEVO TERMINARE AGGIUNGENDO ALTRI CASI . DEVO GUARDARE MEGLIO I MESSAGGI CHE ARRIVANO AL CLIENT DA PARTE DEL SERVER

        }
    }
}
