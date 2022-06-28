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
     * @param moves number of moves
     * @param chosenAssistant the assistant selected
     */
    void OnUpdateMoveMother(int moves ,Assistant chosenAssistant);

    /**
     * Sends a message to the server with the cloud chosen by the user.
     *
     * @param index index of cloud chosen
     */
    void OnUpdatePickCloud(int index);

    /**
     * Sends a message to the server with the cloud chosen by the user.
     *
     * @param index index from cloud chosen
     */
    void OnUpdateGetFromCloud(int index);

    /**
     * Handles a disconnection wanted by the user.
     *
     * (e.g. a click on the back button into the GUI).
     */
    void onDisconnection();

    /**
     * Updates the answer of the player to the start of the game
     *
     * @param answer answer to be sent
     */
    void OnStartAnswer(String answer);

    /**
     * Updates the expert to be sent to the server
     *
     * @param c expert card
     */
    void OnUpdateExpert(ExpertDeck c);

    /**
     * Updates the effect for herald
     *
     * @param island index of island chosen
     */
    void OnUpdateEffectHerald(int island);

    /**
     * Updates the effect for herbalist
     *
     * @param island index of island chosen
     */
    void OnUpdateEffectHerbalist(int island);

    /**
     * Updates the effect for seller
     *
     * @param c color chosen
     */
    void OnUpdateEffectSeller(Color c);

    /**
     * Updates the effect for banker
     *
     * @param c color chosen
     */
    void OnUpdateEffectBanker(Color c);

    /**
     * Updates the effect for barbarian
     *
     * @param c color chosen
     */
    void OnUpdateEffectBarbarian(Color c);

    /**
     * Updates the effect for musician
     *
     * @param c color chosen
     */
    void OnUpdateEffectMusician(Color c);

    /**
     * Updates the effect for joker
     *
     * @param c color chosen
     */
    void OnUpdateEffectJoker(Color c);

    /**
     * Updates the effect for taverner (color version)
     *
     * @param c color chosen
     */
    void OnUpdateEffectTaverner(Color c);

    /**
     * Updates the effect for taverner (island version)
     *
     * @param index idex of island chosen
     */
    void OnUpdateEffectTaverner(int index);

}
