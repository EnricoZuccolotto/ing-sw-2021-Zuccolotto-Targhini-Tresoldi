package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;

/**
 * This class implements the message sender for the Local Game Advanced Feature.
 */
public class LocalClient implements Client {
    private final GameController gameController;

    /**
     * Default constructor
     * @param gameController The {@code GameController} destination for messages.
     */
    public LocalClient(GameController gameController){
        this.gameController = gameController;
    }

    /**
     * Send a local message by directly calling {@code onMessage} from the {@code GameController}
     * @param message The message to send
     */
    @Override
    public void sendMessage(Message message){
        gameController.onMessage(message);
    }
}
