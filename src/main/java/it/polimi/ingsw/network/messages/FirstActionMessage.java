package it.polimi.ingsw.network.messages;

public class FirstActionMessage extends Message {
    int index1, index2;


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
