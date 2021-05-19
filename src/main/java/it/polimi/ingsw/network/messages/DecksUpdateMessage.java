package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This class notifies the client that the {@code FaithPath} has changed its state.
 */
public class DecksUpdateMessage extends Message implements ExecutableViewMessage {
    private final Decks decks;

    /**
     * Default constructor
     *
     * @param decks Decks instance.
     */
    public DecksUpdateMessage(Decks decks) {
        super("server", MessageType.DECKS_UPDATE);
        this.decks = decks;
    }

    public Decks getDecks() {
        return decks;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showDecks(this.getDecks()));
    }
}
