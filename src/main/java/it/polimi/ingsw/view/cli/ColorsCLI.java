package it.polimi.ingsw.view.cli;

/**
 * This class contains all colors used in Cli.
 */
public enum ColorsCLI {
    //Color end string, color reset
    RESET("\033[0m"),
    CLEAR("\033[H\033[2J"),

    // RESOURCES COLORS
    GRAY("\033[37m"),
    RED("\033[0;31m"),
    YELLOW("\033[33m"),
    GREEN("\033[38;5;28m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[38;5;165m"),
    WHITE("\033[38;5;15m"),
    BLACK("\033[30;1m"),

    // Bold
    YELLOW_BOLD("\033[1;33m"),
    CYAN_BOLD("\033[1;36m");

    private final String code;

    ColorsCLI(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
