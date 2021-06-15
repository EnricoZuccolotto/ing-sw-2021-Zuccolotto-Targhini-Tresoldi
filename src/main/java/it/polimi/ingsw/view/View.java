package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;

import java.util.ArrayList;

/**
 * Generic view interface. It can be extended to provide different types of view (CLI, GUI, virtual network view...)
 */
public interface View {

    /**
     * Asks the user to choose a Username.
     */
    void askUsername();

    /**
     * Sets the local nickname parameter
     * @param nickname The nickname you want to set.
     */
    void setNickname(String nickname);

    /**
     * Asks how many players you want in a game.
     */
    void askPlayersNumber();

    void joinLobby();

    void askAction(TurnState state);

    void askFirstAction();

    void askSecondAction();

    boolean askGetMarket();

    boolean askSortingMarket();

    void askSwitchRows();

    boolean askGetProduction();

    boolean askUseBaseProduction();

    boolean askUseSpecialProduction();

    boolean askUseNormalProduction();

    boolean askFoldLeader();

    boolean askActiveLeader();


    void showPlayerBoard(CompressedPlayerBoard playerBoard);

    void showFaithPath(FaithPath faithPath);

    void showDecks(Decks decks);

    void showMarket(Market market);


    void showLoginResult(boolean nick, boolean accepted, String name);

    void showLobby(ArrayList<String> players);

    void showError(String error);

    void showCommunication(String communication, CommunicationMessage type);

    ClientManager getClientManager();
}
