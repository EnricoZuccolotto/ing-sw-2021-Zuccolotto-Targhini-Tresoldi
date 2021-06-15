package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * Generic message class. Every message class needs to extend this class.
 */
public abstract class Message implements Serializable {
    protected final String playerName;
    protected final MessageType messageType;

    /**
     * Default constructor
     * @param playerName Player name or {@code "server"} in case of a generic broadcast message.
     * @param messageType Message type
     */
    Message(String playerName, MessageType messageType){
        this.playerName = playerName;
        this.messageType = messageType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

}
