package it.polimi.ingsw.view;

/**
 * Generic view interface. It can be extended to provide different types of view (CLI, GUI, virtual network view...)
 */
public interface View {

    /**
     * Asks the user to choose a Username.
     */
    void askUsername();

    void askPlayersNumber();

    void showLoginResult(boolean nick, boolean accepted, String name);

    void showError(String error);
}
