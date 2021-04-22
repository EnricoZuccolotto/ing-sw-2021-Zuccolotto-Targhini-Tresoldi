package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.enums.Colors;

import java.util.Stack;
/**
 * This class represents a space of production on the player board.
 * spaceProd is a stack of the card stacked in a single space of production.
 */
public class SpaceProd {
    private final Stack<DevelopmentCard> spaceProd;

    /**
     * Build a new SpaceProd with the first development card.
     *
     * @param c first development card.
     */
    public SpaceProd(DevelopmentCard c) {
        spaceProd = new Stack<>();
        this.spaceProd.push(c);

    }

    /**
     * Gets the sum of the victory points of all the cards in this production space.
     *
     * @return the sum of the victory points of all the cards in this production space.
     */
    public int getVictoryPoints() {
        int cont = 0;
        for (DevelopmentCard c : spaceProd)
            cont += c.getVP();
        return cont;
    }

    /**
     * Adds a Development card to the stack
     *
     * @param card card to add
     */
    public void addCard(DevelopmentCard card) {
        spaceProd.push(card);
    }

    /**
     * Gets the number of color c contained in this production space
     *
     * @param c color of interest
     */
    public int checkColor(Colors c) {
        int cont = 0;
        for (DevelopmentCard card : spaceProd) {

            if (card.getColor().equals(c)) {

                cont += 1;
            }
        }
        return cont;
    }

    /**
     * Gets the first card of the stack
     *
     * @return the usable development card.
     */
    public DevelopmentCard getTop() {
        return spaceProd.peek();
    }

    /**
     * Gets the number of cards contained in the production space
     *
     * @return the number of cards contained in the production space
     */
    public int getNumbCard() {
        return spaceProd.size();
    }

    @Override
    public String toString() {
        return "SpaceProd"
                + spaceProd
                ;
    }
}
