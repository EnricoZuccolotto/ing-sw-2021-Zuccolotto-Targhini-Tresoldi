package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This class represents a socket client implementation.
 */
public class SocketClient extends Observable {

    private static final int TIMEOUT = 10000;
    public static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private final ExecutorService readExecutionQueue;
    private final ScheduledExecutorService heartBeat;

    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), TIMEOUT);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
        this.heartBeat = Executors.newSingleThreadScheduledExecutor();
    }


    /**
     * Reads a message from the server via socket and notifies the ViewController.
     */
    public void readMessage() {
        readExecutionQueue.execute(() -> {

            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    message = (Message) objectInputStream.readObject();
                    LOGGER.info("Received: " + message.getMessageType() + " from " + message.getPlayerName());
                    System.out.println(message.getMessageType());
                } catch (IOException | ClassNotFoundException e) {
                    message = new ErrorMessage(null, "Connection lost with the server.\n" + e.getMessage());
                    disconnect();
                    readExecutionQueue.shutdownNow();
                }
                notifyObserver(message);
            }
        });
    }

    /**
     * Sends a message to the server via socket.
     *
     * @param message to send.
     */

    public void sendMessage(Message message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.reset();
        } catch (IOException e) {
            disconnect();
            notifyObserver(new ErrorMessage("", "Message not sent"));
        }
    }

    /**
     * Disconnect the socket from the server.
     */

    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                readExecutionQueue.shutdownNow();
                enablePinger(false);
                socket.close();
            }
        } catch (IOException e) {
            notifyObserver(new ErrorMessage("", "Cannot disconnect"));
        }
    }

    /**
     * Enable a heartbeat (ping messages) between client and server sockets to keep the connection alive.
     *
     * @param enabled set this argument to {@code true} to enable the heartbeat.
     *                set to {@code false} to kill the heartbeat.
     */
    public void enablePinger(boolean enabled) {
        if (enabled) {
            heartBeat.scheduleAtFixedRate(() -> sendMessage(new Ping()), 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            heartBeat.shutdownNow();
        }
    }
}