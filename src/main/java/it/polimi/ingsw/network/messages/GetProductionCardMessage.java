package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.tools.ExchangeResources;

public class GetProductionCardMessage extends ProductionMessage{
    private final int  color;
    private final int  level;
    private  final int index;

    public GetProductionCardMessage(String playerName, MessageType messageType, ExchangeResources exchangeResources, int color, int level, int index) {
        super(playerName, messageType, exchangeResources);
        this.color = color;
        this.level = level;
        this.index = index;
    }

    public int getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getIndex() {
        return index;
    }


}
