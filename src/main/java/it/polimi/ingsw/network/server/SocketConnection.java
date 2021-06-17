package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class represents the server-side socket that exchanges messages to/from the client.
 * It starts a listener thread to read messages.
 */
public class SocketConnection implements Runnable {
    private final Socket clientSocket;
    private final SocketServer socketServer;

    private boolean connected;

    private final Object inputLock;
    private final Object outputLock;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final Thread socketConnectionThread;

    /**
     * Default constructor
     * @param socketServer The connection listener server.
     * @param client The Java Socket created by the server where you want to listen and send messages.
     */
    public SocketConnection(SocketServer socketServer, Socket client){
        this.socketServer = socketServer;
        this.clientSocket = client;
        this.connected = true;

        this.inputLock = new Object();
        this.outputLock = new Object();
        try{
            synchronized (inputLock){
                this.input = new ObjectInputStream(client.getInputStream());
            }
            synchronized (outputLock){
                this.output = new ObjectOutputStream(client.getOutputStream());
            }
        } catch (IOException e) {
            SocketClient.LOGGER.warning("Invalid message: " + e.getMessage());
        }

        // Start the listener thread.
        socketConnectionThread = new Thread(this);
        socketConnectionThread.start();
    }

    @Override
    public void run(){
        // REMEMBER: First message exchange: client connects then sends a login request with his username
        while(!Thread.currentThread().isInterrupted()){
            try {
                synchronized (inputLock){
                    Message message = (Message) input.readObject();
                    if(message != null){
                        if (message.getMessageType() != MessageType.PING) {
                            socketServer.onMessage(message, this);
                        }
                    }
                }
            } catch (IOException e) {
                disconnect();
            } catch (ClassNotFoundException e) {
                SocketClient.LOGGER.warning("Invalid message: " + e.getMessage());
            }
        }
    }

    public boolean isConnected(){
       return connected;
    }

    /**
     * Sends a message to the client
     * @param message The message you want to send.
     */
    public void sendMessage(Message message){
        if(connected) {
            try {
                synchronized (outputLock) {
                    if (message.getMessageType() != MessageType.PING)
                        System.out.println("...sending... " + message.getMessageType());
                    output.writeObject(message);
                    output.reset();
                }
            } catch (IOException e) {
                SocketClient.LOGGER.warning("Invalid message: " + e.getMessage());
                disconnect();
            }
        }
    }

    /**
     * Handle client disconnection by closing the socket and stopping the thread.
     */
    public void disconnect(){
        if(connected){
            try{
                if(!clientSocket.isClosed()){
                    clientSocket.close();
                }
            } catch(IOException e){
                SocketClient.LOGGER.warning("Invalid message: " + e.getMessage());
            }

            socketConnectionThread.interrupt();
            connected = false;

            socketServer.onDisconnect(this);
        }
    }
}
