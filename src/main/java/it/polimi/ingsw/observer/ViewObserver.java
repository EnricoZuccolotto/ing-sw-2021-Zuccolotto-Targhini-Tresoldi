
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
     * Sends a message to the server with the indexes of the leader cards to discard in the first turn.
     *
     * @param index1 Index of the first card.
     */
    void firstAction(int index1, int index2);

    /**
     * Sends a message to the server with the resources chosen by the player in the second turn.
     *
     * @param resources Resources chosen.
     */
    void secondAction(ArrayList<Resources> resources);

    /**
     * Sends a message to get the resource from the market.
     *
     * @param choice if choice equals 2,the player requested a column, else the player requested a row.
     * @param index  Index of the row/column requested.
     */
    void getMarket(int choice, int index);

    /**
     * Sends a message to sort a resource from the temporary storage to the warehouses
     *
     * @param choice Resource to sort.
     * @param index  Index of the resource to sort.
     * @param row    Position in the warehouse.
     */
    void sortingMarket(Resources choice, int row, int index);

    /**
     * Sends a message to switch the rows in the warehouse.
     *
     * @param row1 Row to switch.
     * @param row2 Row to switch.
     */
    void switchRows(int row1, int row2);

    /**
     * Sends a message to move a resource between the warehouses.
     *
     * @param resources   Resource to sort.
     * @param position    Position of the resource.
     * @param newPosition New position of the resource.
     */
    void moveBetweenWarehouses(Resources resources, int position, int newPosition);
    //FIXME: non so cosa c'è in a.

    /**
     * Sends a message to get a development card from the decks.
     *
     * @param color Color of the card.
     * @param level Level of the card.
     * @param index Index of the production space chosen.
     */
    void getProduction(int color, int level, ArrayList<Integer> pos, int index, int[] a);

    void useBaseProduction(ArrayList<Integer> value, ArrayList<Resources> pass, Resources obtain);

    void useNormalProduction(int index, ArrayList<Integer> pos, int[] a);

    void useSpecialProduction(int index, int choice, Resources resource, Resources res);

    /**
     * Sends a message to active a leader card.
     *
     * @param index Index of the leader card to activate.
     */
    void activeLeader(int index);

    /**
     * Sends a message to add himself to the lobby.
     */
    void addPlayerLobby();

    /**
     * Sends a message to discard a leader card.
     *
     * @param index Index of the leader card to discard.
     */
    void foldLeader(int index);

    /**
     * Sends a message to end the turn.
     */
    void endTurn();

    void onDisconnect();
}