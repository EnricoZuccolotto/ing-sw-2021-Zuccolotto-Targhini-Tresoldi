package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This class notifies the client that the {@code FaithPath} has changed its state.
 */
public class FaithPathUpdateMessage extends Message implements ExecutableViewMessage {
    private final FaithPath faithPath;

    /**
     * Default constructor
     *
     * @param faithPath Faith Path instance.
     */
    public FaithPathUpdateMessage(FaithPath faithPath) {
        super("server", MessageType.FAITH_PATH_UPDATE);
        this.faithPath = faithPath;
    }

    public FaithPath getFaithPath() {
        return faithPath;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showFaithPath(this.getFaithPath()));
    }
}
