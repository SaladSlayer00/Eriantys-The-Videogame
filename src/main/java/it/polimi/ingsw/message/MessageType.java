package it.polimi.ingsw.message;

public enum MessageType {
        LOGIN_REQUEST, LOGIN_REPLY,
        PLAYERNUMBER_REQUEST, PLAYERNUMBER_REPLY,
        GAMEMODE_REPLY, GAMEMODE_REQUEST,
        LOBBY,
        INIT_DECK,
        ASK_DECK,
        ASK_TEAM, //BOH
        INIT_TOWERS,
        ASK_TOWER, //???
        INIT_GAMEBOARD,
        PICK_CLOUD,
        PICKCLOUD_REQUEST, //???
        DRAW_ASSISTANT,
        ASSISTANT_REQUEST, //???
        MOVE,
        MOVE_ON_ISLAND,
        MOVE_ON_BOARD,
        MOVE_MOTHER,
        GET_FROM_CLOUD,
        USE_EXPERT,
        WIN,
        WIN_FX,
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
