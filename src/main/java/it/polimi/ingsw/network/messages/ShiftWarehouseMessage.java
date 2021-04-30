package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.WarehousePositions;

/**
 * Message that the client sends in order to shift rows in the {@code Warehouse}
 */
public class ShiftWarehouseMessage extends Message {
    private final WarehousePositions startingPos, newRowPos;

    /**
     * Default constructor.
     *
     * @param playerName  Player name.
     * @param startingPos Row you want to shift.
     * @param newRowPos   Position you want the shifted row to be in.
     */
    public ShiftWarehouseMessage(String playerName, WarehousePositions startingPos, WarehousePositions newRowPos) {
        super(playerName, MessageType.SHIFT_WAREHOUSE);
        this.startingPos = startingPos;
        this.newRowPos = newRowPos;
    }

    public WarehousePositions getStartingPos() {
        return startingPos;
    }

    public WarehousePositions getNewRowPos() {
        return newRowPos;
    }
}
