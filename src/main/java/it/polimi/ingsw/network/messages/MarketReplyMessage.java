package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public class MarketReplyMessage extends Message {
    private final ArrayList<Resources> resources;
    public MarketReplyMessage(String playerName, ArrayList<Resources> resources){
        super(playerName, MessageType.MARKET_REPLY);
        this.resources = resources;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }
}
