package it.polimi.ingsw.network.messages;

public class LoginMessage extends Message {
    boolean connection, name;

    public LoginMessage(String playerName) {
        super(playerName, MessageType.LOGIN);
    }

    public LoginMessage(String playerName, boolean connection, boolean name) {
        super(playerName, MessageType.LOGIN);
        this.connection = connection;
        this.name = name;
    }

    public boolean isConnection() {
        return connection;
    }

    public boolean isName() {
        return name;
    }
}
