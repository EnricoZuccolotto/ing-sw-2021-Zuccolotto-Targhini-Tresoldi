package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.enums.BotActions;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.controllers.BoardController;
import it.polimi.ingsw.view.gui.controllers.UsernameController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Gui extends ViewObservable implements View {
    private String nickname = "";
    private int playerNumber = 5;
    private static Gui instance = null;
    private BoardController boardController;
    private UsernameController usernameController;
    private CompressedPlayerBoard CompressedPlayerBoard;
    private ClientManager clientManager;
    private boolean local = false;
    private boolean singlePlayer = false;

    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }

    public boolean isLocal() {
        return local;
    }

    @Override
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Creates the client manager
     * @param gameController GameController for local game. If network game this should be null.
     */
    public ClientManager createClientManager(GameController gameController){
        if(gameController != null){
            clientManager = new ClientManager(this, gameController);
            local = true;
            singlePlayer = true;
        } else {
            clientManager = new ClientManager(this);
            local = false;
            singlePlayer = false;
        }

        return clientManager;
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
                if (numberString.isPresent()) {
                    try {
                        numberOfPlayers = Integer.parseInt(numberString.get());
                    } catch (NumberFormatException e) {
                        numberOfPlayers = 0;
                    }
                    if (numberOfPlayers >= 1 && numberOfPlayers <= 4) exit = true;
                    else
                        GuiSceneUtils.showAlertWindow(AlertType.WARNING, "Error", "Invalid number of players, try again!");
                }
            } while (!exit);



            int finalNumberOfPlayers = numberOfPlayers;
            notifyObserver(obs -> obs.PlayersNumber(finalNumberOfPlayers));
            if (numberOfPlayers != 1)
                Platform.runLater(() -> usernameController.changeToLobby());
            else singlePlayer = true;
        });
    }

    public void askPlayersNumber(int number){
        notifyObserver(obs -> obs.PlayersNumber(number));
    }

    @Override
    public void joinLobby() {
        Platform.runLater(() -> notifyObserver(ViewObserver::addPlayerLobby));
    }

    @Override
    public void askAction(TurnState state) {
        System.out.println(state);
        Platform.runLater(() ->
                boardController.clearChoices());
        switch (state) {
            case FIRST_TURN: {
                Platform.runLater(() -> boardController.notInTurn(true));
                askFirstAction();
                break;
            }
            case SECOND_TURN: {
                Platform.runLater(() -> boardController.notInTurn(true));
                askSecondAction();
                break;
            }
            case FIRST_LEADER_ACTION:
            case NORMAL_ACTION: {
                Platform.runLater(() -> {
                    boardController.notInTurn(false);
                    boardController.showBoard(singlePlayer);
                    boardController.activeDecks(true);
                    boardController.activeMarket(true);
                    boardController.activeProductions(true);
                });
                break;
            }
            case PRODUCTION_ACTIONS:
                Platform.runLater(() -> {

                    boardController.activeEndTurn(true);
                    boardController.activeDecks(false);
                    boardController.activeMarket(false);
                    boardController.activeProductions(true);
                });
                break;
            case WAREHOUSE_ACTION:
                Platform.runLater(() -> {

                    boardController.activeEndTurn(true);
                    boardController.activeDecks(false);
                    boardController.activeMarket(false);
                    boardController.activeProductions(false);
                });
                break;
            case LAST_LEADER_ACTION: {
                Platform.runLater(() -> {
                    boardController.activeEndTurn(true);
                    boardController.activeDecks(false);
                    boardController.activeMarket(false);
                    boardController.activeProductions(false);
                });
                break;
            }
            case NOT_IN_TURN:
                Platform.runLater(() -> boardController.notInTurn(true));
                break;
        }

    }

    @Override
    public void askFirstAction() {
        Platform.runLater(() ->
                boardController.updateFirstAction(CompressedPlayerBoard.getPlayerBoard()));
    }

    @Override
    public void askSecondAction() {

        Platform.runLater(() ->
                boardController.showBoard(singlePlayer));
        if (playerNumber == 0) {
            Platform.runLater(() ->
                    boardController.showCommunication("You are the first player.Waiting for other players to make their choices", true));
            Platform.runLater(() -> boardController.notInTurn(true));
            new Thread(this::communication).start();
        } else {
            Platform.runLater(() ->
            {
                boardController.setChooseResourceText("YOU ARE THE " + (playerNumber + 1) + " PLAYER.CHOOSE 1 RESOURCE");
                boardController.askResource(true);
            });
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
                    boardController.askResource(true);
                });
                while (boardController.getResourcesToSend().size() != 2)
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException E) {
                        System.exit(3);
                    }
            }

            notifyObserver(obs -> obs.secondAction(boardController.getResourcesToSend()));
        }
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
        Platform.runLater(() -> boardController.activeBotActions(singlePlayer));
        if (playerBoard.getName().equals(nickname)) {
            this.CompressedPlayerBoard = playerBoard;
            this.playerNumber = playerBoard.getPlayerNumber();
            Platform.runLater(() -> boardController.updatePlayerBoard(playerBoard));
        } else {
            int n = playerBoard.getPlayerNumber();
            if (n < this.playerNumber) n++;
            int finalN = n;
            Platform.runLater(() -> boardController.updatePlayerBoard2(playerBoard, finalN));
        }
    }

    @Override
    public void showFaithPath(FaithPath faithPath) {
        Platform.runLater(() -> boardController.updateFaithPath(faithPath, playerNumber));
    }

    @Override
    public void showDecks(Decks decks) {
        Platform.runLater(() -> boardController.updateDecks(decks));
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
                if(!local){
                    askJoinOrSet();
                } else {
                    askPlayersNumber(1);
                }
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
                    Platform.runLater(() -> usernameController.changeToLobby());
                } else if(result.get() == ButtonType.YES){
                    askPlayersNumber();
                }
            }
        });
    }

    @Override
    public void showLobby(ArrayList<String> players) {
        Platform.runLater(() -> usernameController.updateLobby(players));
    }

    @Override
    public void showError(String error) {
        Platform.runLater(() -> GuiSceneUtils.showAlertWindow(AlertType.ERROR, "Error", error));
    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {
        switch (type) {
            case STARTING_GAME:
                Platform.runLater(() -> GuiSceneUtils.changeActivePanel(observers, "board.fxml"));
                break;
            case ILLEGAL_LOBBY_ACTION:
                Platform.runLater(() -> {
                    GuiSceneUtils.showAlertWindow(AlertType.WARNING, "Warning", "There aren't any active games in the server, try again or join an existing lobby!");
                    askJoinOrSet();
                });
                break;
            case BOT_ACTION:
                Platform.runLater(() -> boardController.setBotActions(new Image(BotActions.valueOf(communication).getImagePath())));
                break;
            case ILLEGAL_ACTION:
                Platform.runLater(() -> boardController.showCommunication(communication, true));
                new Thread(this::communication).start();
                break;
            case END_GAME:
                //

        }
    }

    private void communication() {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException E) {
            System.exit(3);
        }

        Platform.runLater(() -> boardController.showCommunication("communication", false));

    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setUsernameController(UsernameController usernameController) {
        this.usernameController = usernameController;
    }
    @Override
    public ClientManager getClientManager(){
        return clientManager;
    }
}
