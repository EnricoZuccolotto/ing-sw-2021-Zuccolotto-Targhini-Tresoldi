package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 * Message sent by the server with the resulting array of resources. Used as a "backup".
 */
public class MarketReplyMessage extends Message {
    private final ArrayList<Resources> resources;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param resources Resources obtained from the market.
     */
    public MarketReplyMessage(String playerName, ArrayList<Resources> resources){
        super(playerName, MessageType.MARKET_REPLY);
        this.resources = resources;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }
}
