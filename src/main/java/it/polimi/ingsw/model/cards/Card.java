package it.polimi.ingsw.model.cards;

import java.io.Serializable;

/**
 * This class represents a card.
 * VP number of victory points of the card
 * ID identifier of the card
 * Uncovered define the state of the card (covered or uncovered).
 */
public class Card implements Serializable {
    private final int VP;
    private final int ID;
    private boolean uncovered;

    /**
     * Build a card
     *
     * @param VP number of victory points of the card
     * @param ID identifier of the card
     */
    public Card(int VP, int ID) {
        this.VP = VP;
        this.ID = ID;
        this.uncovered = false;
    }

    /**
     * Change uncovered from false to true
     */
    public void flipCard() {
        this.uncovered = true;
    }

    /**
     * Gets the state of uncovered, true or false.
     *
     * @return the state of uncovered, true or false.
     */
    public boolean getUncovered() {
        return uncovered;
    }

    /**
     * Gets the victory points of the card
     *
     * @return the victory points of the card
     */
    public int getVP() {
        return VP;
    }

    /**
     * Gets the identifier of the card (int)
     *
     * @return the identifier of the card (int)
     */
    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "VP=" + VP;
    }
}
