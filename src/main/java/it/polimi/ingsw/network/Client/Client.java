package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.network.messages.Message;

public interface Client {
    void sendMessage(Message message);
}
