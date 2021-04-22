package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.HumanPlayer;

public class HumanPlayerUpdateMessage extends Message {
    private final HumanPlayer humanPlayer;

    public HumanPlayerUpdateMessage(HumanPlayer humanPlayer){
        super(humanPlayer.getName(), MessageType.HUMAN_PLAYER_UPDATE);
        this.humanPlayer = humanPlayer;
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }
}
