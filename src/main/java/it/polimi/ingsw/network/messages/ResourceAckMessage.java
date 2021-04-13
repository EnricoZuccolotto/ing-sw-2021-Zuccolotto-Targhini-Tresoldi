package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

public class ResourceAckMessage extends Message{
    private final int receivedResourceIndex;
    public ResourceAckMessage(String playerName, int receivedResourceIndex){
        super(playerName, MessageType.RESOURCE_ACK);
        this.receivedResourceIndex = receivedResourceIndex;
    }
}
