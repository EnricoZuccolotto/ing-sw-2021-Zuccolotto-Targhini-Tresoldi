package it.polimi.ingsw.network.messages;

public enum MessageType {
    // Market actions
    MARKET_REQUEST,
    MARKET_REPLY,
    SET_RESOURCE,
    RESOURCE_ACK,
    DISCARD_RESOURCE,
    SHIFT_WAREHOUSE,
    // Generic messages,
    ERROR,
}
