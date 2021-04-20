package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.tools.ExchangeResources;

public class UseProductionNormalMessage extends ProductionMessage{
    private final int  index;

    public UseProductionNormalMessage(String playerName, MessageType messageType, ExchangeResources exchangeResources, int index) {
        super(playerName, messageType, exchangeResources);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
