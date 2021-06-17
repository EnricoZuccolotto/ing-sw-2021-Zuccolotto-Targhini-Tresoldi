package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 * This message is sent in order to choose the starting resources for your game.
 */
public class SecondActionMessage extends Message implements ExecutableMessage {
    private final ArrayList<Resources> resources;

    /**
     * Default constructor.
     *
     * @param playerName Player name.
     * @param resources  Resources to add.
     */
    public SecondActionMessage(String playerName, ArrayList<Resources> resources) {
        super(playerName, MessageType.SECOND_ACTION);
        this.resources = resources;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }


    @Override
    public void execute(GameController instance) {
    if (instance.validateAction(Action.SECOND_ACTION) && instance.getGameState().equals(GameState.GAMESTARTED)) {
        instance.getRoundController().handle_secondAction(this);
    } else instance.buildInvalidResponse(playerName);
    }
}