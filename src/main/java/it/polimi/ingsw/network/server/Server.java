package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.enums.PlayerDisconnectionState;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.*;
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
        TurnState currentTurnState = gameController.getRoundController().getTurnState();
        NetworkLayerView view = new NetworkLayerView(connection);
        if ((clients.get(nickname) != null)) {
            connection.sendMessage(new LoginMessage(nickname, true, false));
        } else if (!gameController.getGameState().equals(GameState.LOBBY)) {
            if (currentTurnState.equals(TurnState.FIRST_TURN) || currentTurnState.equals(TurnState.SECOND_TURN)) {
                // We are in the setup, prevent logins.
                connection.sendMessage(new LoginMessage(nickname, false, true));
            } else {
                // Check if nicknames match, then reconnect.
                HumanPlayer reconnectingPlayer = gameController.getInstance().getPlayer(nickname);
                if(reconnectingPlayer != null && reconnectingPlayer.getPlayerState().equals(PlayerDisconnectionState.INACTIVE)){
                    NetworkLayerView networkView = (NetworkLayerView) gameController.getViewFromMap(nickname);
                    networkView.setConnection(connection);
                    reconnectingPlayer.setPlayerState(PlayerDisconnectionState.ACTIVE);
                    reconnectingPlayer.setPrivateCommunication("The game is restarting", CommunicationMessage.STARTING_GAME);
                    gameController.getInstance().sendGameUpdateToAllPlayers();
                    reconnectingPlayer.sendUpdateToPlayer();
                    if(gameController.getInstance().getActivePlayersCount() == 1){
                        gameController.getRoundController().handle_endTurn();
                    }

                } else {
                    // TODO: send error message.
                }
            }
        } else {
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
        SocketClient.LOGGER.info("Nickname " + nickname + " has disconnected!");

        // Remove from the game
        synchronized (lock){
            clients.remove(nickname);
        }

        GameState currentGameState = gameController.getGameState();
        TurnState currentTurnState = gameController.getRoundController().getTurnState();
        if(currentGameState.equals(GameState.LOBBY)){
            // We are in the lobby: remove the client from the lobby and send updates.
            gameController.getLobby().removeUser(nickname);
            gameController.sendLobby();
        } else if(currentTurnState.equals(TurnState.FIRST_TURN)){
            // TODO: Remove code duplicates
            // We are during a setup turn, remove the client from the game.
            // We handle removal through a "false" message: since message handling is thread-safe,
            // we fakely send a first action message in order for the game logic to be consistent.
            HumanPlayer disconnectedPlayer = gameController.getInstance().getPlayer(nickname);
            if(gameController.getInstance().getActivePlayersCount() == 1){
                SocketClient.LOGGER.info("Every player has disconnected, kill the game.");
                System.exit(0);
            }
            if(disconnectedPlayer.getPlayerBoard().getInkwell()){
                SocketClient.LOGGER.info("The inkwell player has disconnected. The game cannot proceed.");
                System.exit(0);
            }
            disconnectedPlayer.setPlayerState(PlayerDisconnectionState.TERMINAL);
            onMessage(new FirstActionMessage(nickname, 0, 1), connection);
        } else if (currentTurnState.equals(TurnState.SECOND_TURN)){
            HumanPlayer disconnectedPlayer = gameController.getInstance().getPlayer(nickname);
            if(gameController.getInstance().getActivePlayersCount() == 1){
                SocketClient.LOGGER.info("Every player has disconnected, kill the game.");
                System.exit(0);
            }
            if(disconnectedPlayer.getPlayerBoard().getInkwell()){
                SocketClient.LOGGER.info("The inkwell player has disconnected. The game cannot proceed.");
                System.exit(0);
            }
            disconnectedPlayer.setPlayerState(PlayerDisconnectionState.TERMINAL);
            onMessage(new SecondActionMessage(nickname, null), connection);
        }
        else {
            // Game started, remember the client in case it reconnects.
            gameController.getInstance().getPlayer(nickname).setPlayerState(PlayerDisconnectionState.INACTIVE);

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
