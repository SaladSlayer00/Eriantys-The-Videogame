package it.polimi.ingsw.message;

/**Enumeration with the different kinds of messages sent and received by the client and server
 *
 */
public enum MessageType {
        LOGIN_REQUEST,
        LOGIN_REPLY,
        PLAYERNUMBER_REQUEST,
        PLAYERNUMBER_REPLY,
        GAMEMODE_REPLY,
        GAMEMODE_REQUEST,
        LOBBY,
        INIT_DECK,
        ASK_DECK,
        ASK_TEAM,
        INIT_TOWERS,
        ASK_TOWER,
        INIT_GAMEBOARD,
        ISLAND_MESSAGE,
        PICK_CLOUD,
        SHOW_ASSISTANT,
        PICKCLOUD_REQUEST,
        DRAW_ASSISTANT,
        ASSISTANT_REQUEST,
        MOVE,
        ASK_MOVE,
        MOVE_ON_ISLAND,
        MOVE_ON_BOARD,
        MOVE_MOTHER,
        COLOR_MESSAGE,
        GET_FROM_CLOUD,
        USE_EXPERT,
        BOARD,
        WIN,
        WIN_FX,
        DRAW,
        MOTHER_POSITION,
        LOSE,

        //utility:
        GAME_LOAD,
        MATCH_INFO,
        DISCONNECTION,
        GENERIC_MESSAGE,
        PING,
        ERROR,
        ENABLE_EFFECT,
        APPLY_EFFECT,
        PERSISTENCE

}
