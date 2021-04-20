package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public class FirstActionMessage extends Message{
    int index1,index2;


    public FirstActionMessage(String playerName, MessageType messageType, int index1, int index2) {
        super(playerName, messageType);
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
