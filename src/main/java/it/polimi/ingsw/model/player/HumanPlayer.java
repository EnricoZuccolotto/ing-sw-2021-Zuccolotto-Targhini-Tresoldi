package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Communication.Communication;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.network.messages.CommunicationMex;
import it.polimi.ingsw.network.messages.HumanPlayerUpdateMessage;
import it.polimi.ingsw.network.messages.MarketReplyMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a human player.
 * player board represents the player board of the player
 * temporaryResourceStorage represents a temporary storage used to manage the resources received from the market
 */
public class HumanPlayer extends Player implements Serializable {
    private PlayerBoard playerBoard;
    private ArrayList<Resources> temporaryResourceStorage;
    private final Communication privateCommunication;

    /**
     * Build a new human player with the name and the inkwell specified.
     *
     * @param name    name of the player
     * @param inkwell true if the player has the inkwell,else false.
     */
    public HumanPlayer(String name, boolean inkwell) {
        super(name);
        this.privateCommunication = new Communication();
        this.playerBoard = new SimplePlayerBoard(inkwell);
        temporaryResourceStorage = new ArrayList<>();
    }

    public Communication getPrivateCommunication() {
        return privateCommunication;
    }

    public void setPrivateCommunication(String communication, CommunicationMessage communicationMessage) {
        this.privateCommunication.setCommunicationMessage(communicationMessage);
        this.privateCommunication.setMessage(communication);
        notifyObserver(new CommunicationMex(this.name, communication, communicationMessage));
    }

    public void doAction() {

    }

    /**
     * Gets the player board.
     *
     * @return the player board of the player.
     */
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Sets the player board,is used to decorate the player board.
     */
    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Gets the temporary resource storage.
     *
     * @return the temporary resource storage.
     */
    public ArrayList<Resources> getTemporaryResourceStorage() {
        return temporaryResourceStorage;
    }

    /**
     * Sets the temporary resource storage to the array list temporaryResourceStorage.
     *
     * @param temporaryResourceStorage resources received from the market push.
     */
    public void setTemporaryResourceStorage(ArrayList<Resources> temporaryResourceStorage) {
        this.temporaryResourceStorage = temporaryResourceStorage;
        notifyObserver(new MarketReplyMessage(super.name, temporaryResourceStorage));
    }

    /**
     * Remove the element with index index from the temporary resource storage.
     *
     * @param index index of the element to remove
     */
    public void removeItemFromTemporaryList(int index) {
        try {
            temporaryResourceStorage.remove(index);
            notifyObserver(new MarketReplyMessage(super.name, temporaryResourceStorage));
        } catch (IndexOutOfBoundsException e) {
            // TODO: Handle exception
        }
    }

    /**
     * Send the updated player to the view.
     */
    public void sendUpdateToPlayer() {
        CompressedPlayerBoard playerBoardToSend = new CompressedPlayerBoard(this);
        notifyObserver(new HumanPlayerUpdateMessage(playerBoardToSend));
    }
}
