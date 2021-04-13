package it.polimi.ingsw.network.messages;

public enum MessageType {
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
    // Generic messages,
    ERROR,
}
