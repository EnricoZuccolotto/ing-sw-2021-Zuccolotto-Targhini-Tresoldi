package it.polimi.ingsw;


import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.GuiEntryPoint;
import javafx.application.Application;

import java.util.logging.Level;

/**
 * Client app.
 */
public class ClientApp {

    public static void main(String[] args) {

        boolean cli_gui = false; // default value

        for (String arg : args) {
            if (arg.equals("-cli") || arg.equals("-c")) {
                cli_gui = true;
                break;
            }
        }

        if (!cli_gui) {
            // Launch the CLI
            SocketClient.LOGGER.setLevel(Level.WARNING);
            Cli cli = new Cli();
            //CLi
        } else {
            // Launch the GUI
            Application.launch(GuiEntryPoint.class);
        }
    }
}

