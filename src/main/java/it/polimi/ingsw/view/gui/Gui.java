package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Gui extends ViewObservable implements View {
    private String nickname = "";

    @Override
    public void askUsername() {
        Platform.runLater(() -> {
            GuiSceneUtils.changeActivePanel(observers, "username.fxml");
        });
    }

    @Override
    public void askPlayersNumber() {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initOwner(GuiSceneUtils.getActiveScene().getWindow());
            dialog.setTitle("Lobby size");
            dialog.setHeaderText("How many player do you want in your game?");
            dialog.setContentText("Insert a number between 1 and 4:");

            boolean exit = false;
            int numberOfPlayers = 0;

            do{
                Optional<String> numberString = dialog.showAndWait();
                if(numberString.isPresent()){
                    numberOfPlayers = Integer.parseInt(numberString.get());
                    if(numberOfPlayers >= 1 && numberOfPlayers <= 4) exit = true;
                    else GuiSceneUtils.showAlertWindow(AlertType.WARNING, "Error", "Invalid number of players, try again!");
                }
            } while(!exit);

            int finalNumberOfPlayers = numberOfPlayers;
            notifyObserver(obs -> obs.PlayersNumber(finalNumberOfPlayers));
        });
    }

    @Override
    public void joinLobby() {
        Platform.runLater(() -> notifyObserver(ViewObserver::addPlayerLobby));
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

    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {
        if(accepted){
            if(nick){
                this.nickname = name;
                askJoinOrSet();
            } else {
                Platform.runLater(() -> {
                    GuiSceneUtils.showAlertWindow(AlertType.ERROR, "Error", "Username is already taken! Try again.");
                    GuiSceneUtils.changeActivePanel(observers, "username.fxml");
                });
            }
        } else {
            if(nick){
                Platform.runLater(() -> {
                    GuiSceneUtils.showAlertWindow(AlertType.ERROR, "Lobby is full", "The lobby is currently full! Try again later in a new game.");
                    System.exit(2);
                });
            } else {
                Platform.runLater(() -> {
                    GuiSceneUtils.showAlertWindow(AlertType.ERROR, "Error", "Could not find the server!");
                    GuiSceneUtils.changeActivePanel(observers, "menu.fxml");
                });
            }
        }
    }

    private void askJoinOrSet(){
        Platform.runLater(() -> {
            Alert joinOrSetAlert = new Alert(AlertType.INFORMATION, "Do you want to join a game or set up a new one?", ButtonType.OK, ButtonType.YES);
            joinOrSetAlert.initOwner(GuiSceneUtils.getActiveScene().getWindow());
            joinOrSetAlert.setTitle("Connection successful!");
            joinOrSetAlert.setHeaderText("Welcome");
            Button joinButton = (Button)joinOrSetAlert.getDialogPane().lookupButton(ButtonType.OK);
            Button setButton = (Button)joinOrSetAlert.getDialogPane().lookupButton(ButtonType.YES);
            joinButton.setText("Join a lobby");
            setButton.setText("Start up a new game");
            Optional<ButtonType> result = joinOrSetAlert.showAndWait();
            if(result.isPresent()){
                if(result.get() == ButtonType.OK){
                    joinLobby();
                } else if(result.get() == ButtonType.YES){
                    askPlayersNumber();
                }
            }
        });
    }

    @Override
    public void showLobby(ArrayList<String> players) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {

    }
}
