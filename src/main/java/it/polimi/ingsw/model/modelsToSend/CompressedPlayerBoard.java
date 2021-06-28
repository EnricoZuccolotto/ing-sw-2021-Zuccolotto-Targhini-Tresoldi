package it.polimi.ingsw.model.modelsToSend;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Wrapper class for the things you want to send to the player.
 */
public class CompressedPlayerBoard implements Serializable {
    private final PlayerBoard playerBoard;
    private final ArrayList<Resources> temporaryResourceStorage;
    private final String name;
    private final int playerNumber;

    /**
     * Default constructor
     * @param player The player you want to generate the Player Board package
     */
    public CompressedPlayerBoard(HumanPlayer player) {
        this.playerBoard = player.getPlayerBoard();
        this.temporaryResourceStorage = player.getTemporaryResourceStorage();
        this.name = player.getName();
        this.playerNumber = player.getPlayerNumber();
    }

    /**
     * Get a player's position number.
     * @return The player's number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Return the player's board
     * @return The {@code PlayerBoard} associated to that player.
     */
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Returns the resourced obtained from the Market and not yet stored or discarded.
     *
     * @return The temporary resource array.
     */
    public ArrayList<Resources> getTemporaryResourceStorage() {
        return temporaryResourceStorage;
    }

    /**
     * Modify the temporary resource storage
     *
     * @param index    Position of the resource to change.
     * @param resource New resource.
     */
    public void setTemporaryResource(int index, Resources resource) {
        temporaryResourceStorage.set(index, resource);
    }

    /**
     * Get the player's name
     *
     * @return The Player Name.
     */
    public String getName() {
        return name;
    }


    public String toString(boolean mine) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ").append(this.getName());
        stringBuilder.append("\n");
        stringBuilder.append("PlayerNumber: ").append(this.getPlayerNumber());
        stringBuilder.append("\n");
        stringBuilder.append(this.getPlayerBoard().toString(mine));
        if (temporaryResourceStorage.size() > 0) {
            stringBuilder.append("\n temporaryResourceStorage=");
            stringBuilder.append(temporaryResourceStorage);
        }
        return stringBuilder.toString();
    }
}
