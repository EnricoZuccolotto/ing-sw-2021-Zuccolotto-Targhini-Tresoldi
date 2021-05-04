package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameController;

/**
 * This message is sent in order to play or discard a {@code LeaderCard}
 */
public class LeaderMessage extends Message implements ExecutableMessage {
    private final int index;

    /**
     * Default constructor.
     * @param playerName Player name
     * @param messageType Message type.
     * @param index Index of the {@code LeaderCard} you want to activate.
     */
    public LeaderMessage(String playerName, MessageType messageType, int index) {
        super(playerName, messageType);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void execute(GameController instance) {
        if(instance.validateAction(Action.LD_ACTION)){
            if(messageType == MessageType.FOLD_LEADER)
                instance.getRoundController().handle_foldLeader(this);
            else if(messageType == MessageType.ACTIVE_LEADER)
                instance.getRoundController().handle_activeLeader(this);
        } else instance.buildInvalidResponse(playerName);
    }
}
