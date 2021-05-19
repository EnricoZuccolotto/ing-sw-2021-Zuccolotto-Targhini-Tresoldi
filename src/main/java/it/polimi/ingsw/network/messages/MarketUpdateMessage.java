package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This message is sent to all users every time the market is modified.
 */
public class MarketUpdateMessage extends Message implements ExecutableViewMessage {
    private final Market market;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param market     Pointer to the current market state.
     */
    public MarketUpdateMessage(String playerName, Market market) {
        super(playerName, MessageType.MARKET_UPDATE);
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showMarket(this.getMarket()));
    }
}
