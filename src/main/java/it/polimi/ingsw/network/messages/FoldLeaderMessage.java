package it.polimi.ingsw.network.messages;

public class FoldLeaderMessage extends Message{
    private final int index;

    public FoldLeaderMessage(String playerName, MessageType messageType, int index) {
        super(playerName, messageType);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
