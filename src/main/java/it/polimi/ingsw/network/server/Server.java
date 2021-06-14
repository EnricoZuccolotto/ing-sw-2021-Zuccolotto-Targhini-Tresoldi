package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
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
        if ((clients.get(nickname) != null)) {
            connection.sendMessage(new LoginMessage(nickname, true, false));
        } else if (!gameController.getGameState().equals(GameState.LOBBY))
            connection.sendMessage(new LoginMessage(nickname, false, true));
        else {
            gameController.addView(nickname, view);
            connection.sendMessage(new LoginMessage(nickname, true, true));
            synchronized (lock) {
                clients.put(nickname, connection);
            }
        }


    }

    /**
     * This handles all received messages, including the login.
     * @param message Received message
     * @param connection {@code SocketConnection} object where the message originates. Useful only for login messages.
     */
    public synchronized void onMessage(Message message, SocketConnection connection){
        System.out.println(message.getMessageType());
        if (message.getMessageType() == MessageType.LOGIN)
            onLogin(message.getPlayerName(), connection);
        else
            gameController.onMessage(message);
    }

    public void onDisconnect(SocketConnection connection){
        String nickname = fromConnectionToNickname(connection);

        // Remove from the game
        synchronized (lock){
            clients.remove(nickname);
        }
        gameController.removeView(nickname);
        if(gameController.getGameState().equals(GameState.LOBBY)){
            // In lobby, remove the client from the lobby and send updates.
            gameController.getLobby().removeUser(nickname);
            gameController.sendLobby();
        } else {
            // Game started, remember the client in case it reconnects.
            gameController.getInstance().getPlayer(nickname).setActive(false);
        }
    }

    public String fromConnectionToNickname(SocketConnection connection){
        for(String nickname : clients.keySet()){
            if(clients.get(nickname).equals(connection))
                return nickname;
        }
        return null;
    }
}
