package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.tools.MORLogger;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* This class implements a thread that listens to client logins.
* Once a client tries to sign in, it starts a new connection thread for client communications.
* */
public class SocketServer implements Runnable {
    private final Server server;
    private final int port;
    ServerSocket serverSocket;

    /**
     * Default constructor
     * @param server Game Server instance.
     * @param port Server port
     */
    public SocketServer(Server server, int port){
        this.server = server;
        this.port = port;
    }

    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(port);
        } catch(IOException e){
            MORLogger.LOGGER.warning("Invalid message: " + e.getMessage());
            return;
        }

        while (!Thread.currentThread().isInterrupted()){
            try{
                Socket client = serverSocket.accept();
                new SocketConnection(this, client);
            } catch(IOException e){
                MORLogger.LOGGER.warning("Invalid message: " + e.getMessage());
            }
        }
    }

    /**
     * Ask the server to run a message.
     * @param message Message you want to handle.
     * @param connection Connection from which the message originates.
     */
    public void onMessage(Message message, SocketConnection connection){
        server.onMessage(message, connection);
    }

    /**
     * Handle a disconnection.
     * @param connection The terminating connection.
     */
    public void onDisconnect(SocketConnection connection){
        server.onDisconnect(connection);
    }
}
