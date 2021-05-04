package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;

public interface ExecutableMessage {
    void execute(GameController instance);
}
