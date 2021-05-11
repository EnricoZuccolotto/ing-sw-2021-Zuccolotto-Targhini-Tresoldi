package it.polimi.ingsw.controller;


import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager implements ViewObserver, Observer {

    public static final int UNDO_TIME = 5000;
    private final View view;
    private final ExecutorService taskQueue;
    private SocketClient client;
    private String nickname;

    /**
     * Constructs Client Controller.
     *
     * @param view the view to be controlled.
     */
    public ClientManager(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip the string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Checks if the given port string is in the range of allowed ports.
     *
     * @param port the ports to be checked.
     * @return {@code true} if the port is valid, {@code false} otherwise.
     */
    public static boolean isValidPort(String port) {
        try {
            int parseInt = Integer.parseInt(port);
            return parseInt >= 1 && parseInt <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Create a new Socket Connection to the server with the updated info.
     * An error view is shown if connection cannot be established.
     *
     * @param address of the server.
     * @param port    of the server.
     */
    @Override
    public void ServerInfo(String address, String port) {
        try {
            client = new SocketClient(address, Integer.parseInt(port));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askUsername);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, nickname));
        }
    }

    /**
     * Sends a message to the server with the nickname.
     * The nickname is also stored locally for later usages.
     *
     * @param nickname the nickname to be sent.
     */
    @Override
    public void Nickname(String nickname) {
        this.nickname = nickname;
        System.out.println(this.nickname);
        client.sendMessage(new LoginMessage(this.nickname));
    }

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    @Override
    public void PlayersNumber(int playersNumber) {
        client.sendMessage(new LobbySetMessage(this.nickname, playersNumber));
    }

    @Override
    public void addPlayerLobby() {
        client.sendMessage(new LobbyJoinMessage(this.nickname));
    }

    @Override
    public void update(Message message) {
        switch (message.getMessageType()) {
            case LOGIN:
                LoginMessage loginMessage = (LoginMessage) message;
                taskQueue.execute(() -> view.showLoginResult(loginMessage.isName(), loginMessage.isConnection(), this.nickname));
                break;
            case ERROR:
                ErrorMessage errorMessage = (ErrorMessage) message;
                taskQueue.execute(() -> view.showError(errorMessage.getError()));
                break;
            case COMMUNICATION:
                CommunicationMex communicationMex = (CommunicationMex) message;
                taskQueue.execute(() -> view.showCommunication(communicationMex.getCommunication(), communicationMex.getCommunicationMessage()));
                break;
            case LOBBY:
                LobbyMessage lobbyMessage = (LobbyMessage) message;
                taskQueue.execute(() -> view.showLobby(lobbyMessage.getPlayers()));
                break;
        }
    }

}