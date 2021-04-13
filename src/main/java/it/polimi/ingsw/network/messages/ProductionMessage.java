package it.polimi.ingsw.network.messages;

public class ProductionMessage extends Message{
    private   final int  [] resSpeWar;
    private   final int  [] resWar;
    private   final int  [] resStr;

    public ProductionMessage(String playerName, MessageType messageType, int[] resSpeWar, int[] resWar, int[] resStr) {
        super(playerName, messageType);
        this.resSpeWar = resSpeWar;
        this.resWar = resWar;
        this.resStr = resStr;
    }

    public int[] getResSpeWar() {
        return resSpeWar;
    }

    public int[] getResWar() {
        return resWar;
    }

    public int[] getResStr() {
        return resStr;
    }
}
