package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

/**
 * This interface models a client as a message sender. It can be extended to support local message sending or messages-over-network.
 */
public interface Client {
    /**
     * Sends a message
     * @param message The message to send
     */
    void sendMessage(Message message);
}
