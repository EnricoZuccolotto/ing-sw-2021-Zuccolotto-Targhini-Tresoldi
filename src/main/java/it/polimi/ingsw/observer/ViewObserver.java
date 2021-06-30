
package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 * Custom observer interface for views. It supports different types of notification.
 */
public interface ViewObserver {

    /**
     * Create a new connection to the server with the updated info.
     *
     * @param address of the server.
     * @param port    of the server.
     */
    void ServerInfo(String address, String port);

    /**
     * Sends a message to the server with the updated nickname.
     *
     * @param nickname the nickname to be sent.
     */
    void Nickname(String nickname);

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    void PlayersNumber(int playersNumber);

    /**
     * Send a message to the server with the two leader card to discard
     *
     * @param index1 for the first card
     * @param index2 for the second card
     */
    void firstAction(int index1, int index2);

    /**
     * Send a message to the server with the resources you want to get for not been first in turn
     *
     * @param resources for the array list of the resources
     */
    void secondAction(ArrayList<Resources> resources);

    /**
     * Send a message to the server with the parameter of the market
     *
     * @param choice to choose between row or column
     * @param index for the index
     */
    void getMarket(int choice, int index);

    /**
     * Send a message for sort the resources obtained from the market
     *
     * @param choice for the resource to sort
     * @param row for the index of the row in the warehouse
     * @param index for the index of the resource in the temporary storage
     */
    void sortingMarket(Resources choice, int row, int index);

    /**
     * Send a message for switch rows in the warehouse
     *
     * @param row1 for the first row
     * @param row2 for the second row
     */
    void switchRows(int row1, int row2);

    /**
     * Send a message to move a resource in the warehouse
     *
     * @param resources for the type of resource
     * @param position for the old position
     * @param newPosition for the new position
     */
    void moveBetweenWarehouses(Resources resources, int position, int newPosition);

    /**
     * Send a message to the server for getting a Development Card
     *
     * @param color for the card color
     * @param level for the card level
     * @param pos for the position of the resources (Warehouse, Strongbox or Special Warehouse)
     * @param index for the index of space production where to put the card
     * @param a for the actual card cost
     */
    void getProduction(int color, int level, ArrayList<Integer> pos, int index, int[] a);

    /**
     * Send a message to the server to active the Base production
     *
     * @param value for the list of the position of the resources used to pay (Warehouse, Strongbox, Special Warehouse)
     * @param pass for the list of the Resources used to pay
     * @param obtain for the resource obtained
     */
    void useBaseProduction(ArrayList<Integer> value, ArrayList<Resources> pass, Resources obtain);

    /**
     * Send a message to the server for activating the production of a card
     *
     * @param index for the Space production index of the card
     * @param pos   for the position of the resources used to pay
     * @param a     for the actual production cost
     */
    void useNormalProduction(int index, ArrayList<Integer> pos, int[] a);

    /**
     * Send a message for activating the special production
     *
     * @param index    for the index of the leader card
     * @param choice   for the position of the resource used to pay
     * @param resource for the resource to get
     * @param res      for the resource used to pay
     */
    void useSpecialProduction(int index, int choice, Resources resource, Resources res);

    /**
     * Send a message to active a leader card.
     *
     * @param index Index of the leader card.
     */
    void activeLeader(int index);

    /**
     * Send a message to add himself to the lobby.
     */
    void addPlayerLobby();

    /**
     * Send a message to fold a leader card.
     *
     * @param index Index of the leader card.
     */
    void foldLeader(int index);

    /**
     * Send a message to end the turn.
     */
    void endTurn();

    /**
     * Handle on disconnection.
     */
    void onDisconnect();
}