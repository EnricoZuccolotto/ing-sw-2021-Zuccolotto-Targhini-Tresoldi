package it.polimi.ingsw.network.messages;

/**
 * Message that the client sends in order to shift rows in the {@code Warehouse}
 */
public class ShiftWarehouseMessage extends Message {
    private final int startingPos, newRowPos;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param startingPos Row you want to shift.
     * @param newRowPos Position you want the shifted row to be in.
     */
    public ShiftWarehouseMessage(String playerName, int startingPos, int newRowPos){
        super(playerName, MessageType.SHIFT_WAREHOUSE);
        this.startingPos = startingPos;
        this.newRowPos = newRowPos;
    }

    public int getStartingPos() {
        return startingPos;
    }

    public int getNewRowPos() {
        return newRowPos;
    }
}
