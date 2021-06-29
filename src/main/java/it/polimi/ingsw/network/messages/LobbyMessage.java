package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class LobbyMessage extends Message implements ExecutableViewMessage {
    private final ArrayList<String> players;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param players    Players in this lobby.
     */
    public LobbyMessage(String playerName, ArrayList<String> players) {
        super(playerName, MessageType.LOBBY);
        this.players = players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showLobby(this.getPlayers()));
    }
}
