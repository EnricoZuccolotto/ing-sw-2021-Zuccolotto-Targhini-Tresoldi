package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.tools.ExchangeResources;

public class UseProductionBaseMessage extends ProductionMessage{
   private final Resources output;

    public UseProductionBaseMessage(String playerName, MessageType messageType, ExchangeResources exchangeResources, Resources output) {
        super(playerName, messageType, exchangeResources);
        this.output = output;
    }

    public Resources getOutput() {
        return output;
    }
}
