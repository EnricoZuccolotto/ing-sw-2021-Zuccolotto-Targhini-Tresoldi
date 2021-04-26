package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.tools.ExchangeResources;

/**
 *
 */
public class GetProductionCardMessage extends ProductionMessage {
    private final Colors color;
    private final int level;
    private final int index;

    public GetProductionCardMessage(String playerName, ExchangeResources exchangeResources, Colors color, int level, int index) {
        super(playerName, MessageType.GET_PRODUCTIONCARD, exchangeResources);
        this.color = color;
        this.level = level;
        this.index = index;
    }

    public Colors getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getIndex() {
        return index;
    }


}
