package it.polimi.ingsw.model.player;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;
import it.polimi.ingsw.model.communication.Communication;
import it.polimi.ingsw.model.communication.CommunicationMessage;
import it.polimi.ingsw.model.enums.PlayerDisconnectionState;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.network.messages.CommunicationMex;
import it.polimi.ingsw.network.messages.HumanPlayerUpdateMessage;
import it.polimi.ingsw.network.messages.StateMessage;

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
    private TurnState state;
    private int playerNumber;
    private PlayerDisconnectionState playerDisconnectionState = PlayerDisconnectionState.ACTIVE;

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
        this.state = TurnState.NOT_IN_TURN;
    }

    /**
     * Gets the player's number.
     *
     * @return the player's number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Sets the player's number.
     *
     * @param playerNumber player's number.
     */
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * Sets the player's state.
     *
     * @param state State to set.
     */
    public void setState(TurnState state) {
        this.state = state;
        notifyObserver(new StateMessage(this.name, state));
    }

    /**
     * Gets the player's state.
     *
     * @return the state.
     */
    public TurnState getState() {
        return state;
    }

    /**
     * Gets the private communication between the server and this player.
     *
     * @return the private communication between the server and this player.
     */
    public Communication getPrivateCommunication() {
        return privateCommunication;
    }

    /**
     * Sets the private communication between the server and this player.
     *
     * @param communicationMessage Type of the message to set.
     * @param communication        Message to set.
     */
    public void setPrivateCommunication(String communication, CommunicationMessage communicationMessage) {
        this.privateCommunication.setCommunicationMessage(communicationMessage);
        this.privateCommunication.setMessage(communication);
        notifyObserver(new CommunicationMex(this.name, communication, communicationMessage));
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
        sendUpdateToPlayer();
    }

    /**
     * Remove the element with index index from the temporary resource storage.
     *
     * @param index index of the element to remove
     */
    public void removeItemFromTemporaryList(int index) {
        try {
            temporaryResourceStorage.remove(index);
            sendUpdateToPlayer();
        } catch (IndexOutOfBoundsException e) {
            setPrivateCommunication("You don't have enough resources", CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    /**
     * Send the updated player to the view.
     */
    public void sendUpdateToPlayer() {
        CompressedPlayerBoard playerBoardToSend = new CompressedPlayerBoard(this);
        notifyObserver(new HumanPlayerUpdateMessage(playerBoardToSend));
    }

    /**
     * Gets the PlayerDisconnectionState of this player.
     *
     * @return the PlayerDisconnectionState.
     */
    public PlayerDisconnectionState getPlayerState() {
        return playerDisconnectionState;
    }

    /**
     * Sets the PlayerDisconnectionState of this player.
     *
     * @param playerDisconnectionState PlayerDisconnectionState to set.
     */
    public void setPlayerState(PlayerDisconnectionState playerDisconnectionState) {
        this.playerDisconnectionState = playerDisconnectionState;
    }
}
