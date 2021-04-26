package it.polimi.ingsw.network.messages;

/**
 * This class represents a generic error message.
 */
public class ErrorMessage extends Message {
    private final String error;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param error Error message.
     */
    public ErrorMessage(String playerName, String error){
        super(playerName, MessageType.ERROR);
        this.error = error;
    }
    public String getError() {
        return error;
    }
}
