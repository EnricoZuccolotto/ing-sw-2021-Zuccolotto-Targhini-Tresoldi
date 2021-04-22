package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Advantages;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * This class represents a leader card.
 * costResources represents the number of resources needed to activate the card, each position represents the quantities of Resources needed to activate this card.(Servant,Coin,Stone,Shield)
 * costColor represents the number of colors needed to activate the card, each position represents the quantities of Colors needed to activate this card.(Blue,Yellow,Purple,Green)
 * advantage represents the type of advantage provided by this card.(Sales,Production,Warehouse,Change).
 * effect1 for each advantage this array has a different meaning:
 * Sales: each position represent the amount of discount to apply to each resource
 * Production: each position represents the quantity of resource needed to make the special production
 * Warehouse: each position represents the quantity of resource that the player can store in this special warehouse
 * Change: each position represents the possibility to change the white resource to the corresponding index resource
 * uncovered represents the state of the card, if is true then the card is active, else no.
 */
public class LeaderCard extends Card {
    private final int[] costResources;
    private final int[] costColor;
    private final Advantages advantage;
    private final int[] effect;

    /**
     * Build a card
     *
     * @param VP            number of victory points of the card
     * @param ID            identifier of the card
     * @param costColor     the number of resources needed to activate the card, each position represents the quantities of Resources needed to activate this card.(Servant,Coin,Stone,Shield)
     * @param costResources the number of colors needed to activate the card, each position represents the quantities of Colors needed to activate this card.(Blue,Yellow,Purple,Green)
     * @param advantage     the type of advantage provided by this card.(Sales,Production,Warehouse,Change).
     * @param effect        for each advantage this array has a different meaning
     */

    public LeaderCard(int VP, int ID, int[] costResources, int[] costColor, Advantages advantage, int[] effect) {
        super(VP, ID);
        this.costResources = costResources;
        this.costColor = costColor;
        this.advantage = advantage;
        this.effect = effect;
    }

    /**
     * Gets the resources needed to activate this card.
     *
     * @return the resources needed to activate this card. (array int).
     */
    public int[] getCostResources() {
        return costResources;
    }

    /**
     * Gets the number of colors needed to activate this card.
     *
     * @return the number of colors needed to activate this card.(array int).
     */
    public int[] getCostColor() {
        return costColor;
    }

    /**
     * Gets the advantage of card.
     *
     * @return the advantage of the card (type Advantage).
     */
    public Advantages getAdvantage() {
        return advantage;
    }

    /**
     * Gets the effect of card.
     *
     * @return the effect of the card (arraylist int).
     */
    public ArrayList<Integer> getEffect() {
        ArrayList<Integer> result = new ArrayList<>(4);
        for (int effect : effect) {
            result.add(effect);
        }
        return result;
    }


    @Override
    public String toString() {
        return "LeaderCard{" +
                super.toString() +
                "costResources=" + Arrays.toString(costResources) +
                ", costColor=" + Arrays.toString(costColor) +
                ", advantage=" + advantage +
                ", effect1=" + Arrays.toString(effect) +
                '}';
    }
}
