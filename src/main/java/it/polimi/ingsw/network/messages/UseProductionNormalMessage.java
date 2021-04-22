package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.tools.ExchangeResources;

public class UseProductionNormalMessage extends ProductionMessage {
    private final int index;

    public UseProductionNormalMessage(String playerName, ExchangeResources exchangeResources, int index) {
        super(playerName, MessageType.USE_NORMAL_PRODUCTION, exchangeResources);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
