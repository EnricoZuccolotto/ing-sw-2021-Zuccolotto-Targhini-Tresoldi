package it.polimi.ingsw.network.messages;

public class UseProductionNormalMessage extends ProductionMessage{
    private final int  index;

    public UseProductionNormalMessage(String playerName, MessageType messageType, int[] resSpeWar, int[] resWar, int[] resStr, int index) {
        super(playerName, messageType, resSpeWar, resWar, resStr);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
