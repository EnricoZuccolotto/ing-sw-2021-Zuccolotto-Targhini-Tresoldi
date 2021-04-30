package it.polimi.ingsw.model.modelsToSend;

import it.polimi.ingsw.model.Communication.Communication;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;

import java.io.Serializable;
import java.util.ArrayList;

public class CompressedPlayerBoard implements Serializable {
    private final PlayerBoard playerBoard;
    private final ArrayList<Resources> temporaryResourceStorage;
    private final String name;
    private final Communication privateCommunication;

    public CompressedPlayerBoard(HumanPlayer player) {
        this.playerBoard = player.getPlayerBoard();
        this.temporaryResourceStorage = player.getTemporaryResourceStorage();
        this.name = player.getName();
        this.privateCommunication = player.getPrivateCommunication();
    }

    public Communication getPrivateCommunication() {
        return privateCommunication;
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
}