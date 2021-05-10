package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;

/**
 * This message is sent during the setup turn in order to discard 2 leader cards.
 */
public class FirstActionMessage extends Message implements ExecutableMessage {
    int index1, index2;

    /**
     * Default constructor
     *
     * @param playerName Player name
     * @param index1     Index of the first leader card to discard.
     * @param index2     Index of the second leader card to discard.
     */
    public FirstActionMessage(String playerName, int index1, int index2) {
        super(playerName, MessageType.FIRST_ACTION);
        this.index1 = index1;
        this.index2 = index2;
    }


    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.FIRST_ACTION) && instance.getGameState().equals(GameState.GAMESTARTED)) {
            instance.getRoundController().handle_firstAction(this);
        } else instance.buildInvalidResponse(playerName);
    }
}