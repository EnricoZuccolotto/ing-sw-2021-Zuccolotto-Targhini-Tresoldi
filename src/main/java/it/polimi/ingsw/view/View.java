package it.polimi.ingsw.view;

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

    void askPlayersNumber();

    void joinLobby();

    void askAction(TurnState state);

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


    void showPlayerBoard(CompressedPlayerBoard playerBoard);

    void showFaithPath(FaithPath faithPath);

    void showDecks(Decks decks);

    void showMarket(Market market);


    void showLoginResult(boolean nick, boolean accepted, String name);

    void showLobby(ArrayList<String> players);

    void showError(String error);

    void showCommunication(String communication, CommunicationMessage type);
}
