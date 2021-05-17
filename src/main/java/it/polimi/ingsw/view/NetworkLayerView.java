package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.network.messages.CommunicationMex;
import it.polimi.ingsw.network.messages.LobbyMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.SocketConnection;
import it.polimi.ingsw.observer.Observer;

import java.util.ArrayList;

/**
 * This is the virtual view for the server. It receives updates from the model and sends them through the connection to its
 * corresponding "real" view in the client. If the message is generated through a model update the model must pass the message
 * with the {@code update} method.
 */
public class NetworkLayerView implements View, Observer {
    private final SocketConnection connection;

    /**
     * Default constructor
     *
     * @param connection The socket connection for that particular view.
     */
    public NetworkLayerView(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void askUsername() {
    }

    @Override
    public void joinLobby() {

    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void showLobby(ArrayList<String> players) {
        connection.sendMessage(new LobbyMessage("", players));
    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {
        connection.sendMessage(new CommunicationMex("", communication, type));

    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void askFirstAction() {

    }

    @Override
    public void askSecondAction() {

    }

    @Override
    public void askGetMarket() {

    }

    @Override
    public void askSortingMarket() {

    }

    @Override
    public void askSwitchRows() {

    }

    @Override
    public void askGetProduction() {

    }

    @Override
    public void askUseBaseProduction() {

    }

    @Override
    public void askUseSpecialProduction() {

    }

    @Override
    public void askUseNormalProduction() {

    }

    @Override
    public void askFoldLeader() {

    }

    @Override
    public void askActiveLeader() {

    }


    @Override
    public void showPlayerBoard(CompressedPlayerBoard playerBoard) {

    }

    @Override
    public void showFaithPath(FaithPath faithPath) {

    }

    @Override
    public void showDecks(Decks decks) {
    }

    @Override
    public void showMarket(Market market) {

    }


    /**
     * Receives a message from the model and sends it directly to the client.
     *
     * @param message Message to be sent.
     */
    @Override
    public void update(Message message) {
        connection.sendMessage(message);
    }
}
