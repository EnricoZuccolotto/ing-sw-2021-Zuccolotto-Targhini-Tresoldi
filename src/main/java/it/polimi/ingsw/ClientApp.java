package it.polimi.ingsw;


import it.polimi.ingsw.model.tools.MORLogger;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.GuiEntryPoint;

import java.util.logging.Level;

/**
 * Client app.
 */
public class ClientApp {


    public static void main(String[] args) {

        boolean cli = false; // default value

        for (String arg : args) {
            if (arg.equals("--gui") || arg.equals("-g")) {
                cli = true;
                break;
            }

        }

        MORLogger.LOGGER.setLevel(Level.WARNING);

        if (!cli) {
            // Launch the CLI
            new Cli();
        } else {
            // Launch the GUI
            GuiEntryPoint.main(args);
        }
    }
}

