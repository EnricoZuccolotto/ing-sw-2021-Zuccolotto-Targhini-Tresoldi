package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This message is sent every time the player changes its state.
 */
public class HumanPlayerUpdateMessage extends Message implements ExecutableViewMessage {
    private final CompressedPlayerBoard humanPlayer;

    /**
     * Default constructor.
     *
     * @param humanPlayer Player that has changed.
     */
    public HumanPlayerUpdateMessage(CompressedPlayerBoard humanPlayer) {
        super(humanPlayer.getName(), MessageType.HUMAN_PLAYER_UPDATE);
        this.humanPlayer = humanPlayer;
    }

    public CompressedPlayerBoard getHumanPlayer() {
        return humanPlayer;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showPlayerBoard(this.getHumanPlayer()));
    }
}
