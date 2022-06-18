package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Mode;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.*;

import java.util.List;
import java.util.Map;

/**
 * Custom observer interface for views. It supports different types of notification.
 */
public interface ViewObserver {

    /**
     * Create a new connection to the server with the updated info.
     *
     * @param serverInfo a map of server address and server port.
     */
    void onUpdateServerInfo(Map<String, String> serverInfo);

    /**
     * Sends a message to the server with the updated nickname.
     *
     * @param nickname the nickname to be sent.
     */
    void onUpdateNickname(String nickname);

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    void onUpdatePlayersNumber(int playersNumber);
    /**
     * Sends a message to the server with the type chosen by the user.
     *
     * @param type the type of tower.
     */
   void OnUpdateInitTower(Type type);
    /**
     * Sends a message to the server with the deck chosen by the user.
     *
     * @param deck the chosen deck.
     */
    void OnUpdateInitDeck(Mage deck);

    /**
     * Sends a message to the server with the game mode chosen by the user.
     *
     * @param gameMode the chosen game mode.
     */
    void OnUpdateGameMode(modeEnum gameMode);

    /**
     * Sends a message to the server with the assistant chosen by the user.
     *
     * @param assistant the chosen assistant.
     */
    void OnUpdateAssistant(Assistant assistant);

    /**
     * Sends a message to the server with the move chosen by the user.
     *
     * @param color the color of the pawn
     * @param index the index of the island.
     */
    void OnUpdateMoveOnIsland(Color color , int index, List<Island> islands);

    /**
     * Sends a message to the server with the move chosen by the user.
     *
     * @param color the color of the pawn
     * @param color the color of the row.
     */
    void OnUpdateMoveOnBoard(Color color , Color row);

    /**
     * Sends a message to the server with the move chosen by the user.
     *
     * @param moves
     * @param chosenAssistant
     */
    void OnUpdateMoveMother(int moves ,Assistant chosenAssistant);

    /**
     * Sends a message to the server with the cloud chosen by the user.
     *
     * @param index
     */
    void OnUpdatePickCloud(int index);

    /**
     * Sends a message to the server with the cloud chosen by the user.
     *
     * @param index
     */
    void OnUpdateGetFromCloud(int index);

    /**
     * Handles a disconnection wanted by the user.
     * (e.g. a click on the back button into the GUI).
     */
    void onDisconnection();

    void OnStartAnswer(String answer);

    void OnUpdateExpert(ExpertDeck c);

    void OnUpdateEffectHerald(int island);

    void OnUpdateEffectHerbalist(int island);

    void OnUpdateEffectSeller(Color c);

    void OnUpdateEffectBanker(Color c);

    void OnUpdateEffectBarbarian(Color c);

    void OnUpdateEffectMusician(Color c);

    void OnUpdateEffectJoker(Color c);

    void OnUpdateEffectTaverner(Color c);

    void OnUpdateEffectTaverner(int index);

}
