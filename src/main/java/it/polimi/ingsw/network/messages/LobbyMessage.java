package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class LobbyMessage extends Message {
    private final ArrayList<String> players;

    public LobbyMessage(String playerName, ArrayList<String> players) {
        super(playerName, MessageType.LOBBY);
        this.players = players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}
