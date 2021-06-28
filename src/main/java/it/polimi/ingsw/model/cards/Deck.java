package it.polimi.ingsw.model.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This class represents a deck of Development card.
 */
public class Deck implements Serializable {
    private final ArrayList<DevelopmentCard> deck;

    /**
     * Build a deck with size equals a zero.
     */
    public Deck() {
        deck = new ArrayList<>();
    }

    /**
     * Gets the deck.
     */
    public ArrayList<DevelopmentCard> getDeck() {
        return deck;
    }

    /**
     * Add a Development card to the deck.
     *
     * @param c card to add
     * @throws NullPointerException if the card is null.
     */
    public void addCard(DevelopmentCard c) {
        if (c == null) throw new NullPointerException("DevelopmentCard cannot be null");
        deck.add(c);
    }

    /**
     * Gets the number of cards contained in the deck.
     *
     * @return the size of the deck.
     */
    public int DeckLength() {
        return deck.size();
    }

    /**
     * Gets the first card of the deck.
     *
     * @return the first card of the deck.
     */
    public DevelopmentCard getFirstCard() {
        try {
            return (DevelopmentCard) deck.toArray()[0];
        } catch(ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            System.out.println(deck.size());
            System.exit(0);
            return null;
        }

    }

    /**
     * Remove the first card of the deck.
     *
     * @throws IndexOutOfBoundsException if the deck has 0 cards.
     */
    public void popFirstCard() {
        deck.remove(0);
    }

    /**
     * Remove the last card of the deck.
     *
     * @throws IndexOutOfBoundsException if the deck has 0 cards.
     */
    public void popLastCard() {
        deck.remove(deck.size() - 1);
    }

    /**
     * Shuffle the cards in the deck.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

}
