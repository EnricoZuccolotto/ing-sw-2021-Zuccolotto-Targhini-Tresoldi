package it.polimi.ingsw.network.messages;

public enum MessageType {
    //first actionS
    FIRST_ACTION,
    SECOND_ACTION,
    // Market actions
    MARKET_REQUEST,
    MARKET_REPLY,
    SET_RESOURCE,
    RESOURCE_ACK,
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
}
