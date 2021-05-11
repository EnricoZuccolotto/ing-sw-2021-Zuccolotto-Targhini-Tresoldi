package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Communication.CommunicationMessage;

import java.util.ArrayList;

/**
 * Generic view interface. It can be extended to provide different types of view (CLI, GUI, virtual network view...)
 */
public interface View {

    /**
     * Asks the user to choose a Username.
     */
    void askUsername();

    void askPlayersNumber();

    void joinLobby();

    void askFirstAction();

    void askSecondAction();

    void askGetMarket();

    void askSortingMarket();

    void askSwitchRows();

    void askGetProduction();

    void askUseBaseProduction();

    void askUseSpecialProduction();

    void askUseNormalProduction();

    void askFoldLeader();

    void askActiveLeader();


    void showPlayerBoard();

    void showFaithPath();

    void showDecks();

    void showMarket();


    void showLoginResult(boolean nick, boolean accepted, String name);

    void showLobby(ArrayList<String> players);

    void showError(String error);

    void showCommunication(String communication, CommunicationMessage type);
}
