package it.polimi.ingsw.network.messages;

public class ShiftWarehouseMessage extends Message {
    private final int startingPos, newRowPos;
    public ShiftWarehouseMessage(String playerName, int rowIndex, int colIndex){
        super(playerName, MessageType.SHIFT_WAREHOUSE);
        this.startingPos = rowIndex;
        this.newRowPos = colIndex;
    }

    public int getStartingPos() {
        return startingPos;
    }

    public int getNewRowPos() {
        return newRowPos;
    }
}
