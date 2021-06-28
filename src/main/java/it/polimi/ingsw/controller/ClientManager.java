package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;
import it.polimi.ingsw.model.tools.ExchangeResources;
import it.polimi.ingsw.model.tools.MORLogger;
import it.polimi.ingsw.network.Client.Client;
import it.polimi.ingsw.network.Client.LocalClient;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager implements ViewObserver, Observer {

    private final View view;
    private final ExecutorService taskQueue;
    private Client client;
    private String nickname;
    private final boolean local;

    /**
     * Constructs Client Controller.
     *
     * @param view the view to be controlled.
     */
    public ClientManager(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        local = false;
    }

    public ClientManager(View view, GameController gameController){
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        local = true;
        client = new LocalClient(gameController);
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
            SocketClient clientPointer = (SocketClient) client;
            clientPointer.addObserver(this);
            clientPointer.readMessage(); // Starts an asynchronous reading from the server.
            clientPointer.enablePinger(true);
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
        view.setNickname(nickname); // On a local game the Nickname is always valid.
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
    public void firstAction(int index1, int index2) {
        client.sendMessage(new FirstActionMessage(this.nickname, index1, index2));
    }

    @Override
    public void secondAction(ArrayList<Resources> resources) {
        client.sendMessage(new SecondActionMessage(this.nickname, resources));
    }

    @Override
    public void getMarket(int choice, int index) {
        if(choice==2) {
            client.sendMessage(new MarketRequestMessage(this.nickname, 3, index));
        } else {
            client.sendMessage(new MarketRequestMessage(this.nickname, index, 4));
        }
    }

    @Override
    public void sortingMarket(Resources choice, int row, int index) {
        if (row >= 0 && row <= 3) {
            client.sendMessage(new SetResourceMessage(this.nickname, choice, WarehousePositions.transform(row), index));
        } else {
            client.sendMessage(new DiscardResourceMessage(this.nickname, index));
        }
    }

    @Override
    public void switchRows(int row1, int row2) {
        client.sendMessage(new ShiftWarehouseMessage(this.nickname, WarehousePositions.transform(row1), WarehousePositions.transform(row2)));
    }

    @Override
    public void moveBetweenWarehouses(Resources resources, int position, int newPosition) {
        client.sendMessage(new moveBetweenWarehouseMessage(this.nickname, resources, WarehousePositions.transform(position), WarehousePositions.transform(newPosition)));
    }

    @Override
    public void getProduction(int color, int level, ArrayList<Integer> pos, int index, int[] a) {
        int[][] matr = new int[3][4];
        int count = 0, count2 = 0;
        ExchangeResources ex;
        for (int i = 0; i < 4; i++) {
            while (count != a[i]) {
                matr[pos.get(count2)][i] += 1;
                count2++;
                count++;
            }
            count = 0;
        }
        ex = new ExchangeResources(matr[0], matr[1], matr[2]);
        client.sendMessage(new GetProductionCardMessage(this.nickname, ex, Colors.transform(color), level, index));
    }


    @Override
    public void useNormalProduction(int index, ArrayList<Integer> pos, int[] a) {
        int[][] matr = new int[3][4];
        int count = 0, count2 = 0;
        ExchangeResources ex;
        for (int i = 0; i < 4; i++) {
            while (count != a[i]) {
                matr[pos.get(count2)][i] += 1;
                count2++;
                count++;
            }
            count = 0;
        }
        ex = new ExchangeResources(matr[0], matr[1], matr[2]);
        client.sendMessage(new UseProductionNormalMessage(this.nickname, ex, index));
    }

    @Override
    public void useBaseProduction(ArrayList<Integer> value, ArrayList<Resources> pass, Resources obtain) {
        int[][] matr = new int[3][4];
        matr[value.get(0)][pass.get(0).ordinal()]=1;
        matr[value.get(1)][pass.get(1).ordinal()]+=1;
        ExchangeResources ex= new ExchangeResources(matr[0], matr[1], matr[2]);
        System.out.println(ex);
        System.out.println("value" +value);
        client.sendMessage(new UseProductionBaseMessage(this.nickname, ex, obtain));
    }

    @Override
    public void useSpecialProduction(int index, int choice, Resources resource, Resources res){
        int[][] matr= new int[3][4];
        matr[choice][res.ordinal()]=1;
        ExchangeResources ex= new ExchangeResources(matr[0], matr[1], matr[2]);
        client.sendMessage(new UseProductionSpecialMessage(this.nickname, ex, resource, index));
    }

    @Override
    public void endTurn() {
        client.sendMessage(new EndTurnMessage(this.nickname));
    }

    @Override
    public void activeLeader(int index) {
        client.sendMessage(new LeaderMessage(this.nickname, MessageType.ACTIVE_LEADER, index));
    }

    @Override
    public void foldLeader(int index) {
        client.sendMessage(new LeaderMessage(this.nickname, MessageType.FOLD_LEADER, index));
    }

    @Override
    public void addPlayerLobby() {
        client.sendMessage(new LobbyJoinMessage(this.nickname));
    }

    @Override
    public void update(Message message) {

        try {

            ExecutableViewMessage currentMessage = (ExecutableViewMessage) message;
            if (message.getMessageType().equals(MessageType.COMMUNICATION) || message.getMessageType().equals(MessageType.STATE)) {
                if (message.getPlayerName().equals(nickname) || message.getPlayerName().equals(""))
                    currentMessage.executeOnView(view, taskQueue);
            } else currentMessage.executeOnView(view, taskQueue);


        } catch (ClassCastException ex) {
            MORLogger.LOGGER.warning("Invalid message: " + ex.getMessage());
        }
    }

    @Override
    public void onDisconnect(){
        if(!local){
            ((SocketClient) client).disconnect();
        } else {
            System.exit(0);
        }
    }
}