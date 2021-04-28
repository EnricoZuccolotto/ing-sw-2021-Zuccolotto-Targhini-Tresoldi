package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.cards.Deck;

/**
 * This class notifies the client that the {@code FaithPath} has changed its state.
 */
public class DeckUpdateMessage extends Message {
    private final Deck deck;

    /**
     * Default constructor
     *
     * @param deck Decks instance.
     */
    public DeckUpdateMessage(Deck deck) {
        super("server", MessageType.DECKS_UPDATE);
        this.deck = deck;
    }

    public Deck getDecks() {
        return deck;
    }
}
