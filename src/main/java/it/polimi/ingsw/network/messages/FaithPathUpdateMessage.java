package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.FaithPath;

/**
 * This class notifies the client that the {@code FaithPath} has changed its state.
 */
public class FaithPathUpdateMessage extends Message {
    private final FaithPath faithPath;

    /**
     * Default constructor
     * @param faithPath Faith Path instance.
     */
    public FaithPathUpdateMessage(FaithPath faithPath){
        super("server", MessageType.FAITH_PATH_UPDATE);
        this.faithPath = faithPath;
    }

    public FaithPath getFaithPath() {
        return faithPath;
    }
}
