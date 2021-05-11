package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Communication.CommunicationMessage;

/**
 * This class represents a generic error message.
 */
public class CommunicationMex extends Message {
    private final String communication;
    private final CommunicationMessage communicationMex;

    /**
     * Default constructor.
     *
     * @param playerName    Player name.
     * @param communication CommunicationMex message.
     */
    public CommunicationMex(String playerName, String communication, CommunicationMessage communicationMex) {
        super(playerName, MessageType.COMMUNICATION);
        this.communication = communication;
        this.communicationMex = communicationMex;
    }


    public String getCommunication() {
        return communication;
    }

    public CommunicationMessage getCommunicationMessage() {
        return communicationMex;
    }

}