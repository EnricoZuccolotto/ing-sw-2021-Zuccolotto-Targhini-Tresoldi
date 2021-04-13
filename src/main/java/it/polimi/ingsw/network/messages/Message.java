package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final String playerName;
    private final MessageType messageType;

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
