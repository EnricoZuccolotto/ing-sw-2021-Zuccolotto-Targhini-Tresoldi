package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;

/**
 * Message that the client sends when it decides to put amove the resource between warehouses.
 */
public class moveBetweenWarehouseMessage extends Message implements ExecutableMessage {

    private final WarehousePositions position, newPosition;
    private final Resources resource;

    /**
     * Default constructor.
     *
     * @param playerName  Player name.
     * @param resource    Resource type.
     * @param position    Position to remove resource. 0 indicates special warehouse; 1-2-3 represent warehouse rows.
     * @param newPosition Position to place resource. 0 indicates special warehouse; 1-2-3 represent warehouse rows.
     */
    public moveBetweenWarehouseMessage(String playerName, Resources resource, WarehousePositions position, WarehousePositions newPosition) {
        super(playerName, MessageType.MOVE_BETWEEN_WAREHOUSE);
        this.resource = resource;
        this.position = position;
        this.newPosition = newPosition;
    }

    public WarehousePositions getPosition() {
        return position;
    }

    public Resources getResource() {
        return resource;
    }

    public WarehousePositions getNewPosition() {
        return newPosition;
    }

    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.SORTING_WAREHOUSES) && instance.getGameState().equals(GameState.GAME_STARTED)) {
            instance.getRoundController().handle_moveBetweenWarehouse(this);
        } else instance.buildInvalidResponse(playerName);
    }
}

