package it.polimi.ingsw.model.modelsToSend;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;

import java.io.Serializable;
import java.util.ArrayList;

public class CompressedPlayerBoard implements Serializable {
    private final PlayerBoard playerBoard;
    private final ArrayList<Resources> temporaryResourceStorage;
    private final String name;
    private final int playerNumber;

    public CompressedPlayerBoard(HumanPlayer player) {
        this.playerBoard = player.getPlayerBoard();
        this.temporaryResourceStorage = player.getTemporaryResourceStorage();
        this.name = player.getName();
        this.playerNumber = player.getPlayerNumber();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public ArrayList<Resources> getTemporaryResourceStorage() {
        return temporaryResourceStorage;
    }

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
