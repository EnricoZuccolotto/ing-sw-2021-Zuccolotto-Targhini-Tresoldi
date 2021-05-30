package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.controllers.BoardController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Gui extends ViewObservable implements View {
    private String nickname = "";
    private int playerNumber;
    private static Gui instance = null;
    private BoardController boardController;
    private PlayerBoard playerBoard;

    private Gui() {

    }

    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }


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

            do {
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
        switch (state) {
            case FIRST_TURN: {
                askFirstAction();
                break;
            }
            case SECOND_TURN: {
                askSecondAction();
                break;
            }
            case PRODUCTION_ACTIONS:
            case NORMAL_ACTION:
            case WAREHOUSE_ACTION:
            case LAST_LEADER_ACTION:
            case FIRST_LEADER_ACTION: {
                Platform.runLater(() ->
                        boardController.showBoard());
                break;
            }

        }
    }

    @Override
    public void askFirstAction() {
        Platform.runLater(() ->
                boardController.updateFirstAction(playerBoard));
    }

    @Override
    public void askSecondAction() {

        Platform.runLater(() ->
                boardController.showBoard());
        if (playerNumber == 0) { // show comunication
        } else {
            Platform.runLater(() ->
                    boardController.askResource());
            while (boardController.getResourcesToSend().size() != 1)
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException E) {
                    System.exit(3);
                }
            if (playerNumber == 3) {

                Platform.runLater(() ->
                {
                    boardController.setChooseResourceText("CHOOSE THE 2ND RESOURCE");
                    boardController.askResource();
                });
            }
            notifyObserver(obs -> obs.secondAction(boardController.getResourcesToSend()));
        }
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
        if (playerBoard.getName().equals(nickname)) {
            this.playerBoard = playerBoard.getPlayerBoard();
        }
    }

    @Override
    public void showFaithPath(FaithPath faithPath) {

    }

    @Override
    public void showDecks(Decks decks) {

    }

    @Override
    public void showMarket(Market market) {
        Platform.runLater(() -> boardController.updateMarket(market));
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
        if (type.equals(CommunicationMessage.PLAYER_NUMBER))
            this.playerNumber = Integer.parseInt(communication);
        if (type.equals(CommunicationMessage.STARTING_GAME)) {
            Platform.runLater(() -> GuiSceneUtils.changeActivePanel(observers, "board.fxml"));
        }

    }

    public void setMarketController(BoardController boardController) {
        this.boardController = boardController;
    }


}
