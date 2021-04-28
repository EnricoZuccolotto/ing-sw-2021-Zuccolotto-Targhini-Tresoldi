package it.polimi.ingsw.model.Communication;

public class Communication {
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
