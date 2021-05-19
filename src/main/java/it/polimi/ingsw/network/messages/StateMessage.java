package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

public class StateMessage extends Message implements ExecutableViewMessage {
    private final TurnState state;


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
