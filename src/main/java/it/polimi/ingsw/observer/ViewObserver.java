
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

    void firstAction(int index1, int index2);

    void secondAction(ArrayList<Resources> resources);

    void getMarket(int choice, int index);

    void sortingMarket(Resources choice, int row, int index);

    void addPlayerLobby();

    void endTurn();
}