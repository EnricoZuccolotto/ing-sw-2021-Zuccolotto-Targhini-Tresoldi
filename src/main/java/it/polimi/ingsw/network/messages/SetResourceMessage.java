package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;

/**
 * Message that the client sends when it decides to put a resource into a specific row.
 */
public class SetResourceMessage extends Message{
    private final WarehousePositions position;
    private final Resources resource;
    private final int receivedResourceIndex;

    /**
     * Default constructor.
     *
     * @param playerName            Player name.
     * @param resource              Resource type.
     * @param position              Position to add resource. 0 indicates special warehouse; 1-2-3 represent warehouse rows.
     * @param receivedResourceIndex Index of said resource in the temporary array obtained from the market.
     */
    public SetResourceMessage(String playerName, Resources resource, WarehousePositions position, int receivedResourceIndex) {
        super(playerName, MessageType.SET_RESOURCE);
        this.resource = resource;
        this.position = position;
        this.receivedResourceIndex = receivedResourceIndex;
    }

    public WarehousePositions getPosition() {
        return position;
    }

    public Resources getResource() {
        return resource;
    }

    public int getReceivedResourceIndex() {
        return receivedResourceIndex;
    }
}
