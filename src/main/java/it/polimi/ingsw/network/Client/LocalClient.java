package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;

public class LocalClient implements Client {
    private final GameController gameController;

    public LocalClient(GameController gameController){
        this.gameController = gameController;
    }

    @Override
    public void sendMessage(Message message){
        gameController.onMessage(message);
    }
}
