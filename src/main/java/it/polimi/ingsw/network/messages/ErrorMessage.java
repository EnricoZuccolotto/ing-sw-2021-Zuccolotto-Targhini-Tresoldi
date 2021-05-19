package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This class represents a generic error message.
 */
public class ErrorMessage extends Message implements ExecutableViewMessage {
    private final String error;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param error      Error message.
     */
    public ErrorMessage(String playerName, String error) {
        super(playerName, MessageType.ERROR);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showError(this.getError()));
    }
}
