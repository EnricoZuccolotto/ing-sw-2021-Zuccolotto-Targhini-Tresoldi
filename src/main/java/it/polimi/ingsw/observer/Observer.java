package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

/**
 * This interface allows getting updates from the model.
 */
public interface Observer {
    void update(Message message);
}
