package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.communication.CommunicationMessage;
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
    private SocketConnection connection;

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
    public boolean askGetMarket() {
        return true;
    }

    @Override
    public boolean askSortingMarket() {
        return true;
    }

    @Override
    public void askSwitchRows() {

    }

    @Override
    public boolean askGetProduction() {
        return true;
    }

    @Override
    public boolean askUseBaseProduction() {
        return true;
    }

    @Override
    public boolean askUseSpecialProduction() {
        return true;
    }

    @Override
    public boolean askUseNormalProduction() {
        return true;
    }

    @Override
    public boolean askFoldLeader() {
        return true;
    }

    @Override
    public boolean askActiveLeader() {
        return true;
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
    public void askAction(TurnState state) {

    }

    @Override
    public void showMarket(Market market) {

    }

    @Override
    public void setNickname(String string){
        // Does nothing, this method is used only on a local game.
        return;
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

    @Override
    public ClientManager getClientManager(){
        return null;
    }

    public void setConnection(SocketConnection connection){
        this.connection = connection;
    }
}
