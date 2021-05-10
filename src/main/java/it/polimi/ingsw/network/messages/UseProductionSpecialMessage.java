package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.tools.ExchangeResources;

/**
 * This class is sent by the client in order to activate Leader Card special production.
 */
public class UseProductionSpecialMessage extends ProductionMessage implements ExecutableMessage {
    private final Resources output;
    private final int index;

    /**
     * Default constructor.
     *
     * @param playerName        Player name.
     * @param exchangeResources Resources that are spent in order to activate production.
     * @param output            Expected resources output.
     * @param index             Index of current leader card.
     */
    public UseProductionSpecialMessage(String playerName, ExchangeResources exchangeResources, Resources output, int index) {
        super(playerName, MessageType.USE_SPECIAL_PRODUCTION, exchangeResources);
        this.output = output;
        this.index = index;
    }

    public Resources getOutput() {
        return output;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void execute(GameController instance) {
        if (instance.validateAction(Action.STD_USE_PRODUCTION) && instance.getGameState().equals(GameState.GAMESTARTED)) {
            instance.getRoundController().handle_useSpecialProduction(this);
        } else instance.buildInvalidResponse(playerName);
    }
}