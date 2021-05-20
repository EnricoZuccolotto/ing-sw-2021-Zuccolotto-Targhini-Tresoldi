package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This class represents a generic error message.
 */
public class CommunicationMex extends Message implements ExecutableViewMessage {
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

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showCommunication(this.getCommunication(), this.getCommunicationMessage()));

    }
}