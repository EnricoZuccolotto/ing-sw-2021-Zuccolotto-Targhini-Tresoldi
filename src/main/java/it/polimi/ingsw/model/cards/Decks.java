package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.DecksUpdateMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;

/**
 * This class represents the collection of Development Card decks in a Game Board.
 */
public class Decks extends Observable implements Serializable {
    private final Deck [][] decks;

    /**
     * Default constructor. Populates the decks via the JSON file.
     */
    public  Decks(){
        this.decks = CardParser.parseDevCards();
    }

    /**
     * Returns a deck given its position.
     * @param color Color of the deck.
     * @param level Level of the deck.
     * @return The corresponding deck.
     */
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
        getDeck(color, level).popLastCard();
        notifyObserver(new DecksUpdateMessage(this));
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Decks:\n");
        for (int i = 1; i < 4; i++) {
            for (Colors colors : Colors.values()) {
                if (this.getDeck(colors, i) != null)
                    stringBuilder.append(this.getDeck(colors, i).getFirstCard());
                else
                    stringBuilder.append("-----------");
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
