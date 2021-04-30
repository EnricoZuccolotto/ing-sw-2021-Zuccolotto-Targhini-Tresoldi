package it.polimi.ingsw.network.messages;

/**
 * Enum with all possible message types. Needed to identify each message.
 */
public enum MessageType {
    //first actionS
    FIRST_ACTION,
    SECOND_ACTION,
    // Market actions
    MARKET_REQUEST,
    MARKET_REPLY,
    MARKET_CHANGED,
    SET_RESOURCE,
    DISCARD_RESOURCE,
    SHIFT_WAREHOUSE,
    //Production action
    GET_PRODUCTIONCARD,
    USE_NORMAL_PRODUCTION,
    USE_SPECIAL_PRODUCTION,
    USE_BASE_PRODUCTION,
    //Leader actionS
    FOLD_LEADER,
    ACTIVE_LEADER,
    //END TURN ACTION
    END_TURN,
    // Generic messages,
    ERROR,
    PING,
    //Update
    HUMAN_PLAYER_UPDATE,
    FAITH_PATH_UPDATE,
    DECKS_UPDATE,
    // Lobby messages
    JOIN_GAME,
    SET_GAME,
    //Connection messages
    LOGIN,


}
