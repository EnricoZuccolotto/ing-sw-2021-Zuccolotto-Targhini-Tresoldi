package it.polimi.ingsw.network.messages;

public class LobbySetMessage extends Message {
    private int playernumber=0;

    public LobbySetMessage(String playerName, int p) {
        super(playerName, MessageType.SET_GAME);
        this.playernumber=p;
    }

    public int getPlayernumber() {
        return playernumber;
    }
}
