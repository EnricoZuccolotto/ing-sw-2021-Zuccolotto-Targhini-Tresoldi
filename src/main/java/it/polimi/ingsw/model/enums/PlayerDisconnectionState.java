package it.polimi.ingsw.model.enums;

/**
 * Represents all possible player states.
 */
public enum PlayerDisconnectionState {
    /**
     * The player is currently connected.
     */
    ACTIVE,
    /**
     * The player is currently disconnected and the server is waiting for him to reconnect.
     */
    INACTIVE,
    /**
     * The player has disconnected in the setup turns, so it's not possible for him to reconnect.
     */
    TERMINAL
}
