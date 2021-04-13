package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

public class DiscardResourceMessage extends Message{
    public DiscardResourceMessage(String playerName){
        super(playerName, MessageType.DISCARD_RESOURCE);
    }
}
