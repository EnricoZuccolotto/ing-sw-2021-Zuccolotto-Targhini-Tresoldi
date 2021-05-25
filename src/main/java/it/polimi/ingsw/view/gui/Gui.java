package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.controllers.MarketController;
import javafx.application.Platform;

import java.util.ArrayList;

public class Gui extends ViewObservable implements View {
    private MarketController marketController;
    private int playerNumber;
    private String name;

    @Override
    public void askUsername() {
        System.out.println("wsdfghnm");
        new Thread(() -> notifyObserver(obs -> obs.Nickname("enr")));
        new Thread(() -> notifyObserver(obs -> obs.PlayersNumber(1)));
        GuiSceneUtils.changeActivePanel(observers, "market.fxml");
    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void joinLobby() {

    }

    @Override
    public void askAction(TurnState state) {

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
        Platform.runLater(() -> marketController.updateMarket(market));
    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {
        if(!nick && !accepted){
            Platform.runLater(() -> {
                GuiSceneUtils.showAlertWindow("Error", "Could not find the server!");
                GuiSceneUtils.changeActivePanel(observers, "menu.fxml");
            });
        }

    }

    @Override
    public void showLobby(ArrayList<String> players) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {
        if (type.equals(CommunicationMessage.PLAYER_NUMBER))
            this.playerNumber = Integer.parseInt(communication);
    }
}
