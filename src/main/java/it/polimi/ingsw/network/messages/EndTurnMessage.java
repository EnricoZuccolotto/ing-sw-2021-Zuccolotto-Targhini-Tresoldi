package it.polimi.ingsw.network.messages;

public class EndTurnMessage extends Message{
    public EndTurnMessage(String playerName, MessageType messageType) {
        super(playerName, messageType);
    }
}
