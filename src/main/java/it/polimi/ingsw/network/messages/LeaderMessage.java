package it.polimi.ingsw.network.messages;

/**
 * This message is sent in order to play or discard a {@code LeaderCard}
 */
public class LeaderMessage extends Message{
    private final int index;

    /**
     * Default constructor.
     * @param playerName Player name
     * @param messageType Message type.
     * @param index Index of the {@code LeaderCard} you want to activate.
     */
    public LeaderMessage(String playerName, MessageType messageType, int index) {
        super(playerName, messageType);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
