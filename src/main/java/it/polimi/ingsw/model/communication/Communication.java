package it.polimi.ingsw.model.communication;

import java.io.Serializable;
/**
 * This class represents a communication between a player and the server.
 */
public class Communication implements Serializable {
    private CommunicationMessage communicationMessage;
    private String message;

    /**
     * Gets the type of the communication message.
     *
     * @return the type of the communication message.
     */
    public CommunicationMessage getCommunicationMessage() {
        return communicationMessage;
    }

    /**
     * Sets the type of the communication message.
     *
     * @param communicationMessage Type to set.
     */

    public void setCommunicationMessage(CommunicationMessage communicationMessage) {
        this.communicationMessage = communicationMessage;
    }

    /**
     * Gets the communication message.
     *
     * @return the communication message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the communication message.
     *
     * @param message Message to set.
     */

    public void setMessage(String message) {
        this.message = message;
    }
}
