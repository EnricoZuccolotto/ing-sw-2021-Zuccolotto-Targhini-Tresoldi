package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.tools.ExchangeResources;

/**
 * This class is sent by the client in order to activate Development Card driven production.
 */
public class UseProductionNormalMessage extends ProductionMessage implements ExecutableMessage {
    private final int index;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param exchangeResources Resources that are spent in order to activate production.
     * @param index Index of the used {@code DevelopmentCard}.
     */
    public UseProductionNormalMessage(String playerName, ExchangeResources exchangeResources, int index) {
        super(playerName, MessageType.USE_NORMAL_PRODUCTION, exchangeResources);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.STD_USEPRODUCTION))
            instance.getRoundController().handle_useNormalProduction(this);
        else instance.buildInvalidResponse(playerName);
    }
}
