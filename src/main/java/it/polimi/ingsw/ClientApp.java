package it.polimi.ingsw;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.GuiEntryPoint;

import java.util.logging.Level;

/**
 * Client app.
 */
public class ClientApp {

    public static GameController gameControllerToSend = null;

    public static void main(String[] args) {

        boolean cli_gui = false; // default value
        boolean local = false;

        for (String arg : args) {
            if (arg.equals("--gui") || arg.equals("-g")) {
                cli_gui = true;
            }
            if(arg.equals("--local") || arg.equals("-l")){
                local = true;
            }
        }

        SocketClient.LOGGER.setLevel(Level.WARNING);
        if(!local){
            // In case of a normal distributed game
            if (!cli_gui) {
                // Launch the CLI
                new Cli();
            } else {
                // Launch the GUI
                GuiEntryPoint.main(args);
            }
        } else {
            // In case of a local single-player game
            if(!cli_gui){
                GameController gameController = new GameController(true);
                new Cli(gameController);
            } else {
                gameControllerToSend = new GameController(true);
                GuiEntryPoint.main(args);
            }
        }
    }
}

