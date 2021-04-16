package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;

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

    public void onLogin(String nickname, SocketConnection connection){
        // TODO: Remember to synchronize every access to clients.
    }

    public void onMessage(Message message){
        gameController.onMessage(message);
    }

    public void onDisconnect(SocketConnection connection){

    }
}
