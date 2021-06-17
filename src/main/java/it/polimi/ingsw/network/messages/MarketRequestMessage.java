package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;

/**
 * Message sent by the client in order to initiate a {@code Market} action.
 */
public class MarketRequestMessage extends Message implements ExecutableMessage {
    private final int rowIndex, colIndex;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param rowIndex   Requested row. {@code 3} if you want to select a column.
     * @param colIndex   Requested column. Value is not used if you want to select a row.
     */
    public MarketRequestMessage(String playerName, int rowIndex, int colIndex) {
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

    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.GET_RESOURCES_FROM_MARKET) && instance.getGameState().equals(GameState.GAMESTARTED)) {
            instance.getRoundController().handle_getMarket(this);
        } else instance.buildInvalidResponse(playerName);
    }
}