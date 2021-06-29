package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
/**
 * This class represents an executable message.
 */
public interface ExecutableMessage {
    /**
     * Execute the action associated with this message.
     *
     * @param instance GameController used to perform the action.
     */
    void execute(GameController instance);
}
