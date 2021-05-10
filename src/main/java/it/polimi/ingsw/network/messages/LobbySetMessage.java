package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;

public class LobbySetMessage extends Message implements ExecutableMessage {
    private final int playerNumber;

    public LobbySetMessage(String playerName, int p) {
        super(playerName, MessageType.SET_GAME);
        this.playerNumber = p;
    }

    @Override
    public void execute(GameController instance) {
        if (instance.getGameState().equals(GameState.LOBBY)) {
            instance.getLobby().handle_setLobby(this);
        } else {
            instance.buildInvalidResponse(playerName);
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
