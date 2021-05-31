package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;

import java.util.Arrays;

/**
 * This class represents a development card.
 * costCard represents the cost of the card, each position represents the quantities of Resources needed to buy this card.(Servant,Coin,Stone,Shield)
 * costProduction represents the cost of the production, each position represents the quantities of Resources needed to make the production.(Servant,Coin,Stone,Shield)
 * productionResult represents the result of the production, each position represents the quantities of Resources produced by the production.(Servant,Coin,Stone,Shield,Faith)
 * color represents the color of the card.(Blue,Yellow,Purple,Green)
 * level represents the level of the card.(1,2,3)
 */
public class DevelopmentCard extends Card {
    private final int[] costCard;
    private final int[] costProduction;
    private final int[] productionResult;
    private final Colors color;
    private final int level;

    /**
     * Build a card
     *
     * @param VP               number of victory points of the card
     * @param ID               identifier of the card
     * @param costCard         the cost of the card, each position represents the quantities of Resources needed to buy this card.(Servant,Coin,Stone,Shield)
     * @param costProduction   the cost of the production, each position represents the quantities of Resources needed to make the production.(Servant,Coin,Stone,Shield)
     * @param productionResult the result of the production, each position represents the quantities of Resources produced by the production.(Servant,Coin,Stone,Shield,Faith)
     * @param color            the color of the card.(Blue,Yellow,Purple,Green)
     * @param level            the level of the card.(1,2,3)
     */
    public DevelopmentCard(int VP, int ID, int[] costCard, int[] costProduction, int[] productionResult, Colors color, int level) {
        super(VP, ID);
        this.color = color;
        this.costCard = costCard;
        this.level = level;
        this.costProduction = costProduction;
        this.productionResult = productionResult;
    }

    /**
     * Gets the cost of card.
     *
     * @return the cost of the card (array int).
     */
    public int[] getCostCard() {
        return costCard;
    }

    /**
     * Gets the cost of production.
     *
     * @return the cost of the production (array int).
     */
    public int[] getCostProduction() {
        return costProduction;
    }

    /**
     * Gets the result of the production.
     *
     * @return the result of the production. (array int).
     */
    public int[] getProductionResult() {
        return productionResult;
    }

    /**
     * Gets the color of card.
     *
     * @return the color of the card (type ColorsCLI).
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Gets the level of card.
     *
     * @return the level of the card (int).
     */
    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {

        return super.toString() +
                " color: " +
                color +
                "| level: " +
                level +
                "| cost: " +
                Resources.toString(costCard) +
                "| production cost: " +
                Resources.toString(costProduction) +
                "| production result: " +
                Resources.toString(productionResult);

    }

    /**
     * Gets the image path of the card
     *
     * @return the image path of the card
     */
    @Override
    public String getImagePath() {
        return "Image/Cards/Development/" + this.getID() + ".png";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevelopmentCard other = (DevelopmentCard) obj;
        return super.equals(obj) &&
                Arrays.equals(other.costCard, costCard) &&
                Arrays.equals(other.costProduction, costProduction) &&
                Arrays.equals(other.productionResult, productionResult) &&
                other.color.equals(color) &&
                other.level == level;
    }
}
