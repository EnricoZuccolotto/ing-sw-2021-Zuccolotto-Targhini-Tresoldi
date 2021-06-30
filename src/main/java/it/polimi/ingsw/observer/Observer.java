package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

/**
 * This interface allows getting updates from the model.
 */
public interface Observer {
    /**
     * Updates.
     */
    void update(Message message);
}
