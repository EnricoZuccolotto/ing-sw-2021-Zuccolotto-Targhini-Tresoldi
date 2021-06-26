package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;

/**
 * Message that signals the end of a turn.
 */
public class EndTurnMessage extends Message implements ExecutableMessage {
    /**
     * Default constructor.
     *
     * @param playerName Player name.
     */
    public EndTurnMessage(String playerName) {
        super(playerName, MessageType.END_TURN);
    }

    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.END_TURN) && instance.getGameState().equals(GameState.GAME_STARTED)) {
            instance.getRoundController().handle_endTurn();
        } else instance.buildInvalidResponse(playerName);
    }
}