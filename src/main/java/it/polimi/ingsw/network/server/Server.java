package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.NetworkLayerView;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private final GameController gameController;
    private final Object lock;
    private final Map<String, SocketConnection> clients;

    public Server(GameController gameController){
        this.gameController = gameController;
        this.lock = new Object();
        synchronized (lock){
            this.clients = new HashMap<>();
        }

    }

    public void onLogin(String nickname, SocketConnection connection) {
        NetworkLayerView view = new NetworkLayerView(connection);
        if ((clients.get(nickname) != null) || !gameController.getGameState().equals(GameState.LOBBY)) {
            connection.sendMessage(new LoginMessage(nickname, true, false));
        } else {
            gameController.addView(nickname, view);
            connection.sendMessage(new LoginMessage(nickname, true, true));
        }
        synchronized (lock) {
            clients.put(nickname, connection);
        }

    }

    public void onMessage(Message message){
        gameController.onMessage(message);
    }

    public void onDisconnect(SocketConnection connection){
        // TODO: Handle disconnection
    }
}
