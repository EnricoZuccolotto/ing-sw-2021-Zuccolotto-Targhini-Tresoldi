package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

public class DiscardResourceMessage extends Message{
    private final int receivedResourceIndex;
    public DiscardResourceMessage(String playerName, int receivedResourceIndex){
        super(playerName, MessageType.DISCARD_RESOURCE);
        this.receivedResourceIndex = receivedResourceIndex;
    }

    public int getReceivedResourceIndex() {
        return receivedResourceIndex;
    }
}
