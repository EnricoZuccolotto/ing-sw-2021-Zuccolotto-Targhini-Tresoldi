package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.tools.ExchangeResources;

public class UseProductionSpecialMessage extends ProductionMessage{
    private final Resources  output;
    private final int index;

    public UseProductionSpecialMessage(String playerName, MessageType messageType, ExchangeResources exchangeResources, Resources output, int index) {
        super(playerName, messageType, exchangeResources);
        this.output = output;
        this.index = index;
    }

    public Resources getOutput() {
        return output;
    }

    public int getIndex() {
        return index;
    }
}
