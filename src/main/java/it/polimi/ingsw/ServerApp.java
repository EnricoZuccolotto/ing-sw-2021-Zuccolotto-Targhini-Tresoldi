package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.SocketServer;

/**
 * Server app.
 */
public class ServerApp {
    public static void main(String[] args) {
        int serverPort = 12221; // default value

        // Input argument parsing
        for (int i = 0; i < args.length; i++) {
            if (args.length >= 2 && (args[i].equals("-port") || args[i].equals("-p"))) {
                try {
                    serverPort = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    SocketClient.LOGGER.warning("Invalid message: " + e.getMessage());
                    System.out.println("Invalid port number. Retry.");
                    System.exit(1);
                }
            }
        }

        // Create the controller and start the connection listener thread.
        GameController gameController = new GameController(false);
        Server server = new Server(gameController);

        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver");
        thread.start();
    }
}
