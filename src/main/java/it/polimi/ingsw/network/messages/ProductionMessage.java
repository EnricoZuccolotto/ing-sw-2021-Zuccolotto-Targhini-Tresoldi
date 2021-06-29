package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.tools.ExchangeResources;
/**
 * This class represents a production message containing the exchange resources.
 */
public abstract class  ProductionMessage extends Message {
    private final ExchangeResources exchangeResources;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param messageType Message type.
     * @param exchangeResources Obtained resources from all possible sources in a class container.
     */
    public ProductionMessage(String playerName, MessageType messageType, ExchangeResources exchangeResources) {
        super(playerName, messageType);
        this.exchangeResources = exchangeResources;
    }

    public ExchangeResources getExchangeResources() {
        return exchangeResources;
    }
}
