package it.polimi.ingsw.network.messages;

/**
 * Message that signals the end of a turn.
 */
public class EndTurnMessage extends Message {
    /**
     * Default constructor.
     * @param playerName Player name.
     */
    public EndTurnMessage(String playerName) {
        super(playerName, MessageType.END_TURN);
    }
}
