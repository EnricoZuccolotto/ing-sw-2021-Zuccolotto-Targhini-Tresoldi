package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.communication.CommunicationMessage;
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

    /**
     * Asks the server to join a lobby.
     */
    void joinLobby();

    /**
     * Asks the user which action he wants to do in the game, depending if the game is in the setup state or not.
     * @param state The current turn state
     */
    void askAction(TurnState state);

    /**
     * Asks the user to perform the first setup action (choose 2 {@code LeaderCards} to discard).
     */
    void askFirstAction();

    /**
     * Asks the user to perform the second setup action (choose initial resources, where applicable).
     */
    void askSecondAction();

    /**
     * Initiates a market request action.
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askGetMarket();

    /**
     * Moves a resource from the temporary storage to the warehouse.
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askSortingMarket();

    /**
     * Asks the server to swap rows in the warehouse.
     */
    void askSwitchRows();

    /**
     * Asks the server to buy a production card.
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askGetProduction();

    /**
     * Asks the server to use the base production (the common {@code PlayerBoard} one).
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askUseBaseProduction();

    /**
     * Asks the server to use the special production (the {@code LeaderCard}-enabled one).
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askUseSpecialProduction();

    /**
     * Asks the server to use the normal production (the common {@code DevelopmentCard}-enabled one).
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askUseNormalProduction();

    /**
     * Asks the server discard a {@code LeaderCard}.
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askFoldLeader();

    /**
     * Asks the server to activate a {@code LeaderCard}.
     * @return {@code false} if the user cancels the action, {@code true} otherwise.
     */
    boolean askActiveLeader();

    /**
     * Shows the {@code PlayerBoard} and saves it. It also saves other players' boards.
     * @param playerBoard The board to save and/or show.
     */
    void showPlayerBoard(CompressedPlayerBoard playerBoard);

    /**
     * Shows the new {@code FaithPath} after it has updated.
     * @param faithPath The new Faith Path received from the server.
     */
    void showFaithPath(FaithPath faithPath);

    /**
     * Shows and stores the {@code DevelopmentCard} decks.
     * @param decks The new decks received from the server.
     */
    void showDecks(Decks decks);

    /**
     * Shows the {@code Market} after it has updated.
     * @param market The new {@code Market} received from the server.
     */
    void showMarket(Market market);

    /**
     * Shows the login message
     * @param nick {@code true} if the nickname is valid and not in use, {@code false} otherwise.
     * @param accepted {@code true} if the lobby is not already full, {@code false} otherwise.
     * @param name Nickname for the user
     */
    void showLoginResult(boolean nick, boolean accepted, String name);

    /**
     * Shows the lobby status.
     * @param players All players currently in the lobby.
     */
    void showLobby(ArrayList<String> players);

    /**
     * Shows a generic error message.
     * @param error The message to show.
     */
    void showError(String error);

    /**
     * Shows a generic communication message.
     * @param communication The message itself.
     * @param type The type of communication.
     */
    void showCommunication(String communication, CommunicationMessage type);

    /**
     * Returns the current {@code ClientManager}
     * @return The active controller.
     */
    ClientManager getClientManager();
}
