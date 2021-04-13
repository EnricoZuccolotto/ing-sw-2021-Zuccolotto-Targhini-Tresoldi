package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

public class UseProductionSpecialMessage extends ProductionMessage{
    private final Resources  output;
    private final int index;

    public UseProductionSpecialMessage(String playerName, MessageType messageType,int index, int[] resSpeWar, int[] resWar, int[] resStr, Resources input, Resources output) {
        super(playerName, messageType, resSpeWar, resWar, resStr);
        this.output = output;
        this.index=index;
    }


    public Resources getOutput() {
        return output;
    }

    public int getIndex() {
        return index;
    }
}
