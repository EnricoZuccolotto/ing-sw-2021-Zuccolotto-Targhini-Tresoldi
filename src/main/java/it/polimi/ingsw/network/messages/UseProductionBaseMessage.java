package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

public class UseProductionBaseMessage extends ProductionMessage{
   private final Resources output;

    public UseProductionBaseMessage(String playerName, MessageType messageType, int[] resSpeWar, int[] resWar, int[] resStr, Resources output) {
        super(playerName, messageType, resSpeWar, resWar, resStr);
        this.output = output;
    }

    public Resources getOutput() {
        return output;
    }
}
