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
     * @return Covered leader card number.
     */
    int getLeaderCardsNumber();

    boolean checkColors(int[] colors);
    boolean checkResources(int [] resources);
    boolean checkResourcesStrongbox(int [] r);
    boolean checkResourcesWarehouse(int [] r);
    boolean payResourcesStrongbox(int [] r);
    boolean payResourcesWarehouse(int [] r);
    boolean payResourcesSpecialWarehouse(int [] r);
    boolean checkResourcesSpecialWarehouse(int [] r);
    void addStrongboxResource(Resources r, int quantities);
    LeaderCard getLeaderCard(int index );
    int[] getProductionCost(int index);
    int[] getProductionResult(int index);
    int getProductionNumber();
    int getNumberResources();
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
