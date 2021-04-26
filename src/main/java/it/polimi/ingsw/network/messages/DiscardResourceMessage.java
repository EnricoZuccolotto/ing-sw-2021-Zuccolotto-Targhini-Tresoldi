package it.polimi.ingsw.network.messages;

/**
 * A client sends this message if it wants to discard a resource.
 */
public class DiscardResourceMessage extends Message {
    private final int receivedResourceIndex;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param receivedResourceIndex Index of said resource in the temporary array received from the market.
     */
    public DiscardResourceMessage(String playerName, int receivedResourceIndex){
        super(playerName, MessageType.DISCARD_RESOURCE);
        this.receivedResourceIndex = receivedResourceIndex;
    }

    public int getReceivedResourceIndex() {
        return receivedResourceIndex;
    }
}
