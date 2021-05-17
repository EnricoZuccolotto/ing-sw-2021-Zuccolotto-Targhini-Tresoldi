package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketConnection implements Runnable {
    private final Socket clientSocket;
    private final SocketServer socketServer;

    private boolean connected;

    private final Object inputLock;
    private final Object outputLock;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final Thread socketConnectionThread;

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
            // TODO: Failed to start client
        }

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
                            System.out.println(message.getMessageType());
                            if (message.getMessageType() == MessageType.LOGIN)
                                socketServer.onLogin(message.getPlayerName(), this);
                            else
                                socketServer.onMessage(message);
                        }
                    }
                }
            } catch (IOException e) {
                disconnect();
            } catch (ClassNotFoundException e) {
                // TODO: error;
            }
        }
    }

    public boolean isConnected(){
       return connected;
    }

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
                // TODO: error;
                disconnect();
            }
        }
    }

    public void disconnect(){
        if(connected){
            try{
                if(!clientSocket.isClosed()){
                    clientSocket.close();
                }
            } catch(IOException e){
                // TODO: Error
            }

            socketConnectionThread.interrupt();
            connected = false;

            socketServer.onDisconnect(this);
        }
    }
}
