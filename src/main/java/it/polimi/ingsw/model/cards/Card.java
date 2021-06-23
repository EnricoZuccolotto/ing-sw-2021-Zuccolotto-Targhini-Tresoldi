package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.view.cli.ColorsCLI;

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

    /**
     * Gets the image path of the card
     *
     * @return the image path of the card
     */
    public String getImagePath() {
        return "Image/Cards/Faith/" + this.ID + ".png";
    }

    @Override
    public String toString() {
        return "VP=" + ColorsCLI.YELLOW_BOLD + VP + ColorsCLI.RESET;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        return other.VP == VP && other.ID == ID;
    }
}
