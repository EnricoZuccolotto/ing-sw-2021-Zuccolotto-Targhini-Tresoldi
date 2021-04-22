package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.enums.Resources;

import java.util.Arrays;
/**
 * This class represents the strongbox.
 * warehouse is an array of 4 integer each one represents the quantity of resource contained(Servant,Coin,Stone,Shield).
 */
public class Strongbox {
    private final int[] strongBox;

    /**
     * Build a strongbox.
     */

    public Strongbox() {
        this.strongBox = new int[4];
        for (int i = 0; i < 4; i++)
            strongBox[i] = 0;
    }

    /**
     * Gets the number of resources of type resource
     *
     * @param resource type of resource
     * @return the number of resources of type resource
     */
    public int getResources(Resources resource) {
        return this.strongBox[resource.ordinal()];
    }

    /**
     * Sets the number of resources of type resource to number
     *
     * @param resource type of resource
     * @param number   quantity to set
     */
    public void setResources(Resources resource, int number) {
        this.strongBox[resource.ordinal()] = number;
    }

    /**
     * Adds number to the amount of resource already contained in the strongbox
     *
     * @param resource type of resource
     * @param number   quantity to add
     */
    public void addResources(Resources resource, int number) {
        this.strongBox[resource.ordinal()] += number;
    }

    /**
     * Removes the number from the amount of resource already contained in the strongbox
     *
     * @param resource type of resource
     * @param number   quantity to remove
     */
    public void removeResources(Resources resource, int number) {
        this.strongBox[resource.ordinal()] -= number;
    }

    @Override
    public String toString() {
        return "Strongbox" +
                Arrays.toString(strongBox)
                ;
    }

    /**
     * Gets the number of all of the resources contained in the strongbox
     *
     * @return the number of all of the resources contained in the strongbox
     */
    public int getNumResources() {
        return strongBox[0] + strongBox[1] + strongBox[2] + strongBox[3];
    }
}
