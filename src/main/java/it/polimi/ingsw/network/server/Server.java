package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.view.NetworkLayerView;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the base class for server communication. It receives data from all connections and the connection listener
 * and passes messages to the {@code GameController}
 */
public class Server {
    private final GameController gameController;
    private final Object lock;
    private final Map<String, SocketConnection> clients;

    /**
     * Default constructor
     * @param gameController The {@code GameController} destination for messages.
     */
    public Server(GameController gameController){
        this.gameController = gameController;
        this.lock = new Object();
        synchronized (lock){
            this.clients = new HashMap<>();
        }

    }

    /**
     * Handles a login to the system, both to enter a lobby and to reconnect.
     * @param nickname The client's nickname.
     * @param connection The connection class associated to the logging in client.
     */
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
     * @param connection {@code SocketConnection} object where the message originates. Only used for login messages.
     */
    public synchronized void onMessage(Message message, SocketConnection connection){
        // Log the message to the console.
        System.out.println(message.getMessageType());

        if (message.getMessageType() == MessageType.LOGIN)
            onLogin(message.getPlayerName(), connection);
        else
            gameController.onMessage(message);
    }

    /**
     * Handles disconnections from the server.
     * @param connection The connection which is terminated.
     */
    public void onDisconnect(SocketConnection connection){
        String nickname = fromConnectionToNickname(connection);

        // Remove from the game
        synchronized (lock){
            clients.remove(nickname);
        }
        gameController.removeView(nickname);

        GameState currentGameState = gameController.getGameState();
        TurnState currentTurnState = gameController.getRoundController().getTurnState();
        if(currentGameState.equals(GameState.LOBBY) || currentTurnState.equals(TurnState.FIRST_TURN) || currentTurnState.equals(TurnState.SECOND_TURN)){
            // In lobby or during setup turns, remove the client from the lobby and send updates.
            gameController.getLobby().removeUser(nickname);
            gameController.sendLobby();
        } else {
            // Game started, remember the client in case it reconnects.
            gameController.getInstance().getPlayer(nickname).setActive(false);
            gameController.removeView(nickname);

            // If player was in turn go to a different turn
            if(gameController.getRoundController().getPlayerInTurn().getName().equals(nickname))
                gameController.getRoundController().nextTurn();

            gameController.getInstance().sendPlayerUpdateToAllPlayers();
        }
    }

    /**
     * Utility method to reverse access the {@code clients} map by connections.
     * @param connection The connection you want to get the nickname from.
     * @return The returned nickname.
     */
    public String fromConnectionToNickname(SocketConnection connection){
        for(String nickname : clients.keySet()){
            if(clients.get(nickname).equals(connection))
                return nickname;
        }
        return null;
    }
}
