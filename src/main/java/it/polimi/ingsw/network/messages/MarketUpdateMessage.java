package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Market;

/**
 * This message is sent to all users every time the market is modified.
 */
public class MarketUpdateMessage extends Message {
    private final Market market;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param market Pointer to the current market state.
     */
    public MarketUpdateMessage(String playerName, Market market) {
        super(playerName, MessageType.MARKET_CHANGED);
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }
}
