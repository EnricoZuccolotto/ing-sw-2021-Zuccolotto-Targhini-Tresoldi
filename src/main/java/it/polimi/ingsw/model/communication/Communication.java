package it.polimi.ingsw.model.communication;

import java.io.Serializable;

public class Communication implements Serializable {
    private CommunicationMessage communicationMessage;
    private String message;

    public Communication() {
    }

    public CommunicationMessage getCommunicationMessage() {
        return communicationMessage;
    }

    public void setCommunicationMessage(CommunicationMessage communicationMessage) {
        this.communicationMessage = communicationMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
