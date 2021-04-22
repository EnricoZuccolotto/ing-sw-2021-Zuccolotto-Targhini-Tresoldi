package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 * This message is sent to all users every time the market is modified.
 */
public class MarketMessage extends Message {
    private final Market market;
    public MarketMessage(String playerName, Market market){
        super(playerName, MessageType.MARKET_CHANGED );
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }
}
