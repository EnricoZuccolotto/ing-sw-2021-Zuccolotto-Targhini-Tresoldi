package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

public class LoginMessage extends Message implements ExecutableViewMessage {
    boolean connection, name;

    /**
     * Default constructor.
     *
     * @param playerName Player name
     */
    public LoginMessage(String playerName) {
        super(playerName, MessageType.LOGIN);
    }

    /**
     * Special constructor.
     *
     * @param playerName Player name.
     * @param connection True if the player is connected.
     * @param name       True if the name is accepted.
     */
    public LoginMessage(String playerName, boolean connection, boolean name) {
        super(playerName, MessageType.LOGIN);
        this.connection = connection;
        this.name = name;
    }

    @Override
    public void executeOnView(View view, ExecutorService taskQueue) {
        taskQueue.execute(() -> view.showLoginResult(this.isName(), this.isConnection(), this.getPlayerName()));
    }

    public boolean isConnection() {
        return connection;
    }

    public boolean isName() {
        return name;
    }
}
