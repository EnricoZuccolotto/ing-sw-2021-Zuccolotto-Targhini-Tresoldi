package it.polimi.ingsw.network.messages;

public class Ping extends Message {
    public Ping() {
        super("", MessageType.PING);
    }
}
