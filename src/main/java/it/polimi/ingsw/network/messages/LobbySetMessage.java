package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;

/**
 * This message is sent in order to create a lobby.
 */
public class LobbySetMessage extends Message implements ExecutableMessage {
    private final int playerNumber;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param p          Player's number.
     */
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
