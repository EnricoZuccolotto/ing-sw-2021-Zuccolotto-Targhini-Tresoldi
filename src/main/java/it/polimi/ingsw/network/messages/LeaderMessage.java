package it.polimi.ingsw.network.messages;

public class LeaderMessage extends Message{
    private final int index;

    public LeaderMessage(String playerName, MessageType messageType, int index) {
        super(playerName, messageType);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
