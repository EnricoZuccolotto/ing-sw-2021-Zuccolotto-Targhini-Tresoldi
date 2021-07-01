package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This message is sent to each player every time his state is changed.
 */
public class StateMessage extends Message implements ExecutableViewMessage {
    private final TurnState state;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param state      TurnState of the player.
     */
    public StateMessage(String playerName, TurnState state) {
        super(playerName, MessageType.STATE);
        this.state = state;

    }

    public TurnState getState() {
        return state;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.askAction(this.getState()));
    }
}
