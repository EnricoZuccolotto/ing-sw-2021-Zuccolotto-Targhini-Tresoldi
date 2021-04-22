package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 * This interface represents a player board.
 */
public interface PlayerBoard {

    // General methods
    boolean getInkwell();

    /**
     * Adds a leader card to the player board
     * @param leaderCard Card to be added
     */
    void addLeaderCard(LeaderCard leaderCard);


    /**
     * Get victory points of the cards for the end of the game.
     * @return Victory points of the cards for current user.
     */
    int getVictoryPointsCards();

    /**
     * Adds a resource to the warehouse
     * @param resource Resource type to add
     * @param row Warehouse position [1,2,3]
     * @return Success or failure of the addition.
     */
    boolean addWarehouseResource(Resources resource, int row);

    /**
     * Moves rows in the warehouse
     * @param startingRow Row to move [1,2,3]
     * @param newRowPosition Desired row position after shift.
     * @return Success or failure
     */
    boolean shiftWarehouseRows(int startingRow, int newRowPosition);

    /**
     * Adds production card in the only possible place
     * @see PlayerBoard#addProductionCard(DevelopmentCard, int)
     */
    boolean addProductionCard(DevelopmentCard card);

    /**
     * Adds a production card to the board
     * @param card Card to be added
     * @param index Position
     * @return Success or failure
     */
    boolean addProductionCard(DevelopmentCard card, int index);

    /**
     * Discard a leader card.
     * @param card Card to be discarded
     */
    void removeLeaderCard(LeaderCard card);

    /**
     * Gets covered leader card number
     *
     * @return Covered leader card number.
     */
    int getLeaderCardsNumber();

    boolean checkColors(int[] colors);

    boolean checkResources(int[] resources);

    boolean checkResourcesStrongbox(int[] r);

    boolean checkResourcesWarehouse(int[] r);

    boolean payResourcesStrongbox(int[] r);

    boolean payResourcesWarehouse(int[] r);

    boolean payResourcesSpecialWarehouse(int[] r);

    boolean checkResourcesSpecialWarehouse(int[] r);

    /**
     * Adds a resource to the strongbox
     *
     * @param r          Resource type to add
     * @param quantities number of resources to add
     */
    void addStrongboxResource(Resources r, int quantities);

    /**
     * Gets a LeaderCard from the hand of the player
     *
     * @param index position of the card
     * @return the card in position index.
     */
    LeaderCard getLeaderCard(int index);

    /**
     * Gets the cost of the production in index position
     *
     * @param index position of the card
     * @return the cost of the production in index position.
     */
    int[] getProductionCost(int index);

    /**
     * Gets the result of the production in index position
     *
     * @param index position of the card
     * @return the result of the production in index position.
     */
    int[] getProductionResult(int index);

    /**
     * Gets the number of production card possessed by the player.
     *
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
     * @param VP number of victory points
     */
    void setVP(int VP);

    // Decorated warehouse methods
    void addWarehouseSpace(Resources resource, int maxQuantity) throws IllegalDecoratorException;

    boolean addExtraResources(Resources resources, int quantity) throws IllegalDecoratorException;

    boolean takeExtraResources(Resources resource, int quantity) throws IllegalDecoratorException;

    ArrayList<Integer> getExtraResources();

    // Decorated cost methods
    void addDiscount(Resources resource, int amount) throws IllegalDecoratorException;
    boolean isResourceDiscounted(Resources resource);
    int getResourceDiscount(Resources resource);

    // Decorated white marble
    void addSubstitute(Resources resource) throws IllegalDecoratorException;
    ArrayList<Boolean> getSubstitutes();
    boolean isResourceSubstitutable(Resources resource);

    // Decorated production
    void addProduction(Resources resource) throws IllegalDecoratorException;
    ArrayList<Resources> getProductions(Resources resource);
}
