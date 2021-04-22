package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.FaithPath;


public class FaithPathUpdateMessage extends Message {
    private final FaithPath faithPath;

    public FaithPathUpdateMessage(FaithPath faithPath){
        super("server", MessageType.FAITH_PATH_UPDATE);
        this.faithPath = faithPath;
    }

    public FaithPath getFaithPath() {
        return faithPath;
    }
}
