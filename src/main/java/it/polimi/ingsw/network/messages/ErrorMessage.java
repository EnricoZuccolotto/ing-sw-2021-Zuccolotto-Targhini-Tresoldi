package it.polimi.ingsw.network.messages;

public class ErrorMessage extends Message {
    private final String error;
    public ErrorMessage(String playerName, String error){
        super(playerName, MessageType.ERROR);
        this.error = error;
    }
    public String getError() {
        return error;
    }
}
