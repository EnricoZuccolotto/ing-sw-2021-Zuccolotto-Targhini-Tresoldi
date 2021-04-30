package it.polimi.ingsw.network.messages;

/**
 * This message is sent during the setup turn in order to discard 2 leader cards.
 */
public class FirstActionMessage extends Message {
    int index1, index2;

    /**
     * Default constructor
     * @param playerName Player name
     * @param index1 Index of the first leader card to discard.
     * @param index2 Index of the second leader card to discard.
     */
    public FirstActionMessage(String playerName, int index1, int index2) {
        super(playerName, MessageType.FIRST_ACTION);
        this.index1 = index1;
        this.index2 = index2;
    }


    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }
}
