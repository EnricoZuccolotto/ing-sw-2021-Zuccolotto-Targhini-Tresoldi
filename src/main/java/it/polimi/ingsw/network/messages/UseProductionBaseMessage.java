package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.tools.ExchangeResources;

/**
 * This class is sent by the client in order to activate the {@code PlayerBoard} base production.
 */
public class UseProductionBaseMessage extends ProductionMessage {
    private final Resources output;

    /**
     * Default constructor.
     * @param playerName Player name
     * @param exchangeResources Resources that are spent in order to activate production.
     * @param output Specifies the resource you want as an output.
     */
    public UseProductionBaseMessage(String playerName, ExchangeResources exchangeResources, Resources output) {
        super(playerName, MessageType.USE_BASE_PRODUCTION, exchangeResources);
        this.output = output;
    }

    public Resources getOutput() {
        return output;
    }
}
