package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;

public class LobbyJoinMessage extends Message implements ExecutableMessage {
    /**
     * Default constructor.
     *
     * @param playerName Player name.
     */
    public LobbyJoinMessage(String playerName) {
        super(playerName, MessageType.JOIN_GAME);
    }

    @Override
    public void execute(GameController instance) {
        if (instance.getGameState().equals(GameState.LOBBY)) {
            instance.getLobby().handle_addInLobby(this);
        } else {
            instance.buildInvalidResponse(playerName);
        }
    }
}