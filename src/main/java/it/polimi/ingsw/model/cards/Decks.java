package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.DecksUpdateMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;

public class Decks extends Observable implements Serializable {
    private final Deck [][] decks;
    public  Decks(){
        this.decks = CardParser.parseDevCards();
    }
    public Deck getDeck(Colors color, int level) {
        level--;
        return decks[color.ordinal()][level];
    }

    /**
     * Remove the first card of the deck.
     *
     * @throws IndexOutOfBoundsException if the deck has 0 cards.
     */
    public void popFirstCard(Colors color, int level) {
        getDeck(color,level).popFirstCard();
        notifyObserver(new DecksUpdateMessage(this));
    }

    /**
     * Remove the last card of the deck.
     *
     * @throws IndexOutOfBoundsException if the deck has 0 cards.
     */
    public void popLastCard(Colors color, int level) {
       getDeck(color,level).popLastCard();
       notifyObserver(new DecksUpdateMessage(this));
    }
}
