package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.tools.ExchangeResources;

/**
 * Message sent in order to put a {@code DevelopmentCard} in the {@code PlayerBoard} reserved space.
 */
public class GetProductionCardMessage extends ProductionMessage implements ExecutableMessage {
    private final Colors color;
    private final int level;
    private final int index;

    /**
     * Default constructor
     *
     * @param playerName        Player name
     * @param exchangeResources {@code LeaderCard} resources.
     * @param color             {@code LeaderCard} color.
     * @param level             {@code LeaderCard} level.
     * @param index             Index of the deck where you want to put your new {@code LeaderCard}.
     */
    public GetProductionCardMessage(String playerName, ExchangeResources exchangeResources, Colors color, int level, int index) {
        super(playerName, MessageType.GET_PRODUCTIONCARD, exchangeResources);
        this.color = color;
        this.level = level;
        this.index = index;
    }

    public Colors getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getIndex() {
        return index;
    }


    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.BUY_DEVELOPMENT_CARD) && instance.getGameState().equals(GameState.GAME_STARTED)) {
            instance.getRoundController().handle_getProduction(this);
        } else instance.buildInvalidResponse(playerName);
    }
}