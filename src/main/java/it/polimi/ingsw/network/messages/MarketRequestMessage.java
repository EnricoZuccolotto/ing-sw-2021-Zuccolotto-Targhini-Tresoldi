package it.polimi.ingsw.network.messages;

/**
 * Message sent by the client in order to initiate a {@code Market} action.
 */
public class MarketRequestMessage extends Message {
    private final int rowIndex, colIndex;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param rowIndex Requested row. {@value =3} if you want to select a column.
     * @param colIndex Requested column. Value is not used if you want to select a row.
     */
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
