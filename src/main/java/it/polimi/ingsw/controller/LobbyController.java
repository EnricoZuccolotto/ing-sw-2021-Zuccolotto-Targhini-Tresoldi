package it.polimi.ingsw.controller;


import it.polimi.ingsw.exceptions.LobbyError;
import it.polimi.ingsw.exceptions.LobbyException;
import it.polimi.ingsw.exceptions.PlayerAlreadyExistsException;
import it.polimi.ingsw.network.messages.LobbyJoinMessage;
import it.polimi.ingsw.network.messages.LobbySetMessage;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class handles the creation of the lobby and it's related messages.
 */
public class LobbyController implements Serializable {
    private final ArrayList<String> inLobbyPlayer;
    private int playerNumber;

    /**
     * Create a new Lobby with 0 player.
     */
    public LobbyController() {
        inLobbyPlayer = new ArrayList<>();
        playerNumber = -1;
    }

    /**
     * Sets the number of players for this lobby from 1 to 4.Adds the player that set the game to the lobby.
     *
     * @param message Message used to get the number of players and the name of the player to add.
     * @throws IndexOutOfBoundsException Exception threw if it receives a message with the wrong number of players.
     * @throws LobbyException            Exception threw if someone else already set the number of players.
     */
    public void handle_setLobby(LobbySetMessage message) {
        if (playerNumber == -1) {
            if (message.getPlayerNumber() > 0 && message.getPlayerNumber() < 5) {
                playerNumber = message.getPlayerNumber();
                inLobbyPlayer.add(message.getPlayerName());
            } else throw new IndexOutOfBoundsException();
        } else throw new LobbyException(LobbyError.ALREADY_CREATED);

    }

    /**
     * Adds a player to the current lobby
     *
     * @param message Message used to get the name of the player to add.
     * @throws LobbyException Exception threw if the lobby is full or if no one created a lobby.
     */
    public void handle_addInLobby(LobbyJoinMessage message) {
        if (checkUserData(message.getPlayerName())) {
            if (isFull()) {
                throw new LobbyException(LobbyError.FULL);
            } else if (playerNumber <= 0) {
                throw new LobbyException(LobbyError.NOT_CREATED);
            }
            inLobbyPlayer.add(message.getPlayerName());
        } else throw new PlayerAlreadyExistsException();
    }

    /**
     * Removes a player to the current lobby
     *
     * @param nickname Nickname of the player to remove.
     */
    public void removeUser(String nickname) {
        inLobbyPlayer.removeIf(userToRemove -> userToRemove.equals(nickname));
    }

    /**
     * Checks if there is there is already a player with the nickname nickname in the lobby.
     *
     * @param nickname Nickname of the player to check.
     * @return true if there isn't a player with the same nickname.
     */
    private boolean checkUserData(String nickname) {
        for (String string : inLobbyPlayer) {
            if (nickname.equals(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the lobby is full.
     *
     * @return true if the lobby is full.
     */
    public boolean isFull() {
        return inLobbyPlayer.size() == playerNumber;
    }

    /**
     * Gets the players of the lobby.
     *
     * @return the players of the lobby.
     */

    public ArrayList<String> getPlayers() {
        return inLobbyPlayer;
    }
}