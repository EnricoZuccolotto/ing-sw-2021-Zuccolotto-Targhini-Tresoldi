package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.SocketConnection;
import it.polimi.ingsw.observer.Observer;

/**
 * This is the virtual view for the server. It receives updates from the model and sends them through the connection to its
 * corresponding "real" view in the client. If the message is generated through a model update the model must pass the message
 * with the {@code update} method.
 */
public class NetworkLayerView implements View, Observer {
    private final SocketConnection connection;

    /**
     * Default constructor
     *
     * @param connection The socket connection for that particular view.
     */
    public NetworkLayerView(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void askUsername() {
    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {

    }

    @Override
    public void showError(String error) {

    }

    /**
     * Receives a message from the model and sends it directly to the client.
     *
     * @param message Message to be sent.
     */
    @Override
    public void update(Message message) {
        connection.sendMessage(message);
    }
}
