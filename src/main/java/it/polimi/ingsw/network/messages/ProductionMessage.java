package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.tools.ExchangeResources;

public class ProductionMessage extends Message{
    private final ExchangeResources exchangeResources;

    public ProductionMessage(String playerName, MessageType messageType, ExchangeResources exchangeResources) {
        super(playerName, messageType);
        this.exchangeResources=exchangeResources;
    }

    public ExchangeResources getExchangeResources() {
        return exchangeResources;
    }
}
