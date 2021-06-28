package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.enums.Colors;

import java.io.Serializable;
import java.util.Stack;

/**
 * This class represents a space of production on the player board.
 * spaceProd is a stack of the card stacked in a single space of production.
 */
public class SpaceProd implements Serializable {
    private final Stack<DevelopmentCard> spaceProd;

    /**
     * Build a new SpaceProd with the first development card.
     */
    public SpaceProd() {
        spaceProd = new Stack<>();
    }

    /**
     * Gets the sum of the victory points of all the cards in this production space.
     *
     * @return the sum of the victory points of all the cards in this production space.
     */
    public int getVictoryPoints() {
        if(spaceProd.size() == 0) return 0;
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
        if(spaceProd.size() == 0) return 0;
        int cont = 0;
        for (DevelopmentCard card : spaceProd) {

            if (card.getColor().equals(c)) {

                cont += 1;
            }
        }
        return cont;
    }

    /**
     * Gets the number of color c contained in this production space with level equals to level
     *
     * @param c color of interest
     */
    public int checkColor(Colors c, int level) {
        if(spaceProd.size() == 0) return 0;
        int cont = 0;
        for (DevelopmentCard card : spaceProd) {
            if (card.getColor().equals(c) && card.getLevel() == level) {
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
        if(spaceProd.size() == 0)
            return new DevelopmentCard(0, -1, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, Colors.BLUE, 0);
        return spaceProd.peek();
    }

    /**
     * Gets the cards of the stack
     *
     * @return all of the cards in the stack.
     */
    public Stack<DevelopmentCard> getCards() {
        return spaceProd;
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
