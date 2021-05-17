package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.TurnState;

public class StateMessage extends Message {
    private final TurnState state;


    public StateMessage(String playerName, TurnState state) {
        super(playerName, MessageType.STATE);
        this.state = state;

    }

    public TurnState getState() {
        return state;
    }
}
