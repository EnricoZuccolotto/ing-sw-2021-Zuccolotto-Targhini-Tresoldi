package it.polimi.ingsw.network.messages;
/**
 * Classic ping message.
 */
public class Ping extends Message {
    /**
     * Default constructor.
     */
    public Ping() {
        super("", MessageType.PING);
    }
}
