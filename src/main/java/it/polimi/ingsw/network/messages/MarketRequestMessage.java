package it.polimi.ingsw.network.messages;

public class MarketRequestMessage extends Message {
    private final int rowIndex, colIndex;

    public MarketRequestMessage(String playerName, int rowIndex, int colIndex){
        super(playerName, MessageType.MARKET_REQUEST);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
}
