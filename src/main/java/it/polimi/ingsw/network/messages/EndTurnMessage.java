package it.polimi.ingsw.network.messages;

public class EndTurnMessage extends Message {
    public EndTurnMessage(String playerName) {
        super(playerName, MessageType.END_TURN);
    }
}
