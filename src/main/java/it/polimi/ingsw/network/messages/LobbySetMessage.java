package it.polimi.ingsw.network.messages;

public class LobbySetMessage extends Message {
    private int playerNumber;

    public LobbySetMessage(String playerName, int p) {
        super(playerName, MessageType.SET_GAME);
        this.playerNumber =p;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
