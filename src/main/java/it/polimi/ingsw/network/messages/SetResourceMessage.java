package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.enums.Resources;

public class SetResourceMessage extends Message{
    private final int position;
    private final Resources resource;
    private final int receivedResourceIndex;

    public SetResourceMessage(String playerName, Resources resource, int position, int receivedResourceIndex){
        super(playerName, MessageType.SET_RESOURCE);
        this.resource = resource;
        this.position = position;
        this.receivedResourceIndex = receivedResourceIndex;
    }

    public int getPosition() {
        return position;
    }

    public Resources getResource() {
        return resource;
    }

    public int getReceivedResourceIndex() {
        return receivedResourceIndex;
    }
}
