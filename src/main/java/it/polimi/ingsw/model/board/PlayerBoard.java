package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.IllegalDecoratorException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;
import it.polimi.ingsw.model.player.SpaceProd;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.tools.ExchangeResources;

import java.util.ArrayList;

/**
 * This interface represents a player board.
 */
public interface PlayerBoard {

    /**
     * Gets the inkwell of the player.
     *
     * @return the state of the inkwell in this player board.
     */
    boolean getInkwell();

    /**
     * Adds a leader card to the player board.
     *
     * @param leaderCard Card to be added.
     */
    void addLeaderCard(LeaderCard leaderCard);


    /**
     * Get victory points of the cards for the end of the game.
     * @return Victory points of the cards for current user.
     */
    int getVictoryPointsCards();

    /**
     * Adds a resource to the warehouse.
     *
     * @param resource Resource type to add.
     * @param row      Warehouse position [1,2,3].
     * @return Success or failure of the addition.
     */
    boolean addWarehouseResource(Resources resource, WarehousePositions row);

    /**
     * Moves rows in the warehouse.
     *
     * @param startingRow    Row to move [1,2,3].
     * @param newRowPosition Desired row position after shift.
     * @return Success or failure.
     */
    boolean shiftWarehouseRows(WarehousePositions startingRow, WarehousePositions newRowPosition);


    /**
     * Adds a production card to the board.
     *
     * @param card  Card to be added.
     * @param index Position.
     * @return Success or failure.
     */
    boolean addProductionCard(DevelopmentCard card, int index);

    /**
     * Discard a leader card.
     * @param card Card to be discarded.
     */
    void removeLeaderCard(LeaderCard card);

    /**
     * Gets covered leader card number.
     *
     * @return Covered leader card number.
     */
    int getLeaderCardsNumber();

    /**
     * Controls if the player has enough colors in total.
     *
     * @param colors quantity of colors to check.
     * @return Success or failure.
     */
    boolean checkColors(int[] colors);

    /**
     * Controls if the player has 1 card of the color and level chosen.
     *
     * @param colors quantity of colors to check.
     * @param level  to check.
     * @return Success or failure.
     */
    boolean checkColorsAndLevel(int[] colors, int level);

    /**
     * Controls if the player has enough resources in total.
     *
     * @param resources quantity of colors to check.
     * @return Success or failure.
     */
    boolean checkResources(int[] resources);

    /**
     * Controls if the player has enough resources in the strongbox.
     *
     * @param r quantity of resources to check.
     * @return Success or failure.
     */

    boolean checkResourcesStrongbox(int[] r);

    /**
     * Controls if the player has enough resources in the warehouse.
     *
     * @param r quantity of resources to check.
     * @return Success or failure.
     */
    boolean checkResourcesWarehouse(int[] r);

    /**
     * Controls if the player has enough resources in the special warehouse.
     *
     * @param r quantity of resources to check.
     * @return Success or failure.
     */
    boolean checkResourcesSpecialWarehouse(int[] r);

    /**
     * Remove from the strongbox the quantities of resources contained in the array r.
     *
     * @param r quantity of resources to pay.
     * @return Success or failure.
     */
    boolean payResourcesStrongbox(int[] r);

    /**
     * Remove from the warehouse the quantities of resources contained in the array r.
     *
     * @param r quantity of resources to pay.
     * @return Success or failure.
     */
    boolean payResourcesWarehouse(int[] r);

    /**
     * Remove from the special warehouse the quantities of resources contained in the array r.
     *
     * @param r quantity of resources to pay.
     * @return Success or failure.
     */
    boolean payResourcesSpecialWarehouse(int[] r);


    /**
     * Adds a resource to the strongbox.
     *
     * @param r          Resource type to add.
     * @param quantities number of resources to add.
     */
    void addStrongboxResource(Resources r, int quantities);

    /**
     * Gets a LeaderCard from the hand of the player.
     * @param index position of the card.
     * @return the card in position index.
     */
    LeaderCard getLeaderCard(int index);

    /**
     * Gets the cost of the production in index position.
     * @param index position of the card.
     * @return the cost of the production in index position.
     */
    int[] getProductionCost(int index);

    /**
     * Gets the result of the production in index position.
     * @param index position of the card.
     * @return the result of the production in index position.
     */
    int[] getProductionResult(int index);

    /**
     * Gets the number of production card possessed by the player.
     * @return the number of production card possessed by the player.
     */
    int getProductionNumber();

    /**
     * Gets the number of resources of any type possessed by the player.
     *
     * @return the number of resources of any type possessed by the player.
     */
    int getNumberResources();

    /**
     * Sets the victory points of the player.
     *
     * @param VP number of victory points.
     */
    void setVP(int VP);

    /**
     * Returns the quantity of a resource in the strongbox.
     * @param resources The resource you want to get its quantity.
     * @return The quantity of that resource.
     */
    int getNumberResourceStrongbox(Resources resources);

    /**
     * Gets the resource type of a particular row in the Warehouse.
     * @param position The row number.
     * @return The type of resource in that row.
     */
    Resources getResourceWarehouse(int position);

    /**
     * Get the victory point for that user.
     * @return Obtained victory points.
     */
    int getVP();

    //Decorated special warehouse method

    /**
     * Adds a special warehouse space for the resource of maxQuantities.
     *
     * @param resource    type of Resource.
     * @param maxQuantity max number of resources that the special warehouse can contain of that type.
     */
    void addWarehouseSpace(Resources resource, int maxQuantity) throws IllegalDecoratorException;

    /**
     * Adds quantity of resource to the special warehouse.
     *
     * @param resources type of Resource.
     * @param quantity  number of resource to add.
     * @return success or failure.
     */
    boolean addExtraResources(Resources resources, int quantity) throws IllegalDecoratorException;

    /**
     * Removes quantity of resource to the special warehouse.
     *
     * @param resource type of Resource.
     * @param quantity number of resource to remove.
     * @return success or failure.
     */
    boolean takeExtraResources(Resources resource, int quantity) throws IllegalDecoratorException;

    /**
     * Gets the extra resources contained in the special warehouse.
     *
     * @return the extra resources contained in the special warehouse.
     */
    ArrayList<Integer> getExtraResources();


    // Decorated cost methods

    /**
     * Adds a discount for the resource of an amount.
     *
     * @param resource type of Resource.
     * @param amount   amount of discount.
     */
    void addDiscount(Resources resource, int amount) throws IllegalDecoratorException;

    /**
     * Gets if the resources is discounted.
     *
     * @return true if the resource is discounted, else false.
     */
    boolean isResourceDiscounted(Resources resource);

    /**
     * Gets the amount of discount for the resource.
     *
     * @param resource type of Resource.
     * @return the amount of discount for the resource.
     */
    int getResourceDiscount(Resources resource);

    // Decorated white marble

    /**
     * Adds the possibility to change the resource white to the resource.
     *
     * @param resource type of Resource.
     */
    void addSubstitute(Resources resource) throws IllegalDecoratorException;

    /**
     * Gets the array list of possible substitute.
     *
     * @return the array list of possible substitute.
     */
    ArrayList<Boolean> getSubstitutes();

    /**
     * Gets all the indices of the resources that can be substituted.
     * Similar to {@code getSubstitutes()} but the list size is the same length as the number of active substitutions.
     * @return An array with the indices of the substitutable resources.
     */
    ArrayList<Resources> getSubstitutableResources();

    /**
     * Gets if the resources is substitutable.
     *
     * @return true if the resources is substitutable, else false.
     */
    boolean isResourceSubstitutable(Resources resource);

    // Decorated production

    /**
     * Adds a special production for the resource.
     *
     * @param resource type of Resource.
     */
    void addProduction(Resources resource) throws IllegalDecoratorException;

    /**
     * Gets the result of the special productions.
     *
     * @return Gets the result of the special productions.
     */
    ArrayList<Resources> getProductions(Resources resource);

    /**
     * Gets a list with the type of resources contained in the strongbox, the warehouse or the special warehouse.
     *
     * @param choice is to choose between warehouse, strongbox, or the special warehouse.
     * @param temp   is for exclude a resource from the count.
     * @return Gets a list with the type of resources contained.
     */
    ArrayList<Resources> getResources(int choice, int temp);

    /**
     * Gets the production space.
     *
     * @return The production space.
     */
    ArrayList<SpaceProd> getProductionSpaces();

    /**
     * Checks if there is space available in the production spaces for this card.
     *
     * @return true if there is the space.
     */
    boolean checkLevel(DevelopmentCard c);

    /**
     * Gets the warehouse.
     *
     * @return The warehouse.
     */
    Warehouse getWarehouse();

    /**
     * Gets the strongbox.
     *
     * @return The strongbox.
     */
    Strongbox getStrongbox();

    /**
     * Gets all the current user's resources.
     * @return All user's resources, from strongbox, warehouse and special warehouse.
     */
    ExchangeResources getExchangeResources();

    /**
     * Gets the toString of the Board.If the board is not yours, it will cover the inactive leader cards.
     *
     * @param mine true if the board is mine.
     * @return the board to string.
     */
    String toString(boolean mine);
}
