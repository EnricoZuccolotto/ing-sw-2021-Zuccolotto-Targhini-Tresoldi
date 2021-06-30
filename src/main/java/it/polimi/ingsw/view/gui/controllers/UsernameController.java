package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiSceneUtils;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;


public class UsernameController extends ViewObservable implements SceneController {
    private final String s = "WAITING FOR OTHER PLAYERS TO CONNECT...";
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Button connectButton;
    @FXML
    private Button backButton;
    @FXML
    private VBox lobby;
    @FXML
    private AnchorPane username;

    @FXML
    public void initialize() {
        Gui gui = Gui.getInstance();
        gui.setUsernameController(this);
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButtonClick);
        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
        connectButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onConnectButtonClick(keyEvent);
        });
        username.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onConnectButtonClick(keyEvent);
        });
        if (Gui.getInstance().isLocal())
            connectButton.setText("Start...");
    }

    /**
     * Returns to the menu.
     */
    private void onBackButtonClick(Event event) {
        connectButton.setDisable(true);
        backButton.setDisable(true);
        if (!Gui.getInstance().isLocal())
            new Thread(() -> notifyObserver(ViewObserver::onDisconnect)).start();
        GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "menu.fxml");
    }

    /**
     * Sends the nickname chosen by the player to the server.
     */
    private void onConnectButtonClick(Event event) {
        connectButton.setDisable(true);
        backButton.setDisable(true);

        String name = nicknameTextField.getText();

        new Thread(() -> notifyObserver(obs -> obs.Nickname(name))).start();

    }

    /**
     * Change the pane to the lobby pane.
     */
    public void changeToLobby() {
        username.setDisable(true);
        username.setVisible(false);
        lobby.setVisible(true);
    }

    /**
     * Update the lobby.
     *
     * @param players Players in the lobby.
     */
    public void updateLobby(ArrayList<String> players) {
        ObservableList<Node> lobbyChildren = lobby.getChildren();
        for (int i = 0; i < 5; i++) {
            Text text = (Text) lobbyChildren.get(i + 1);
            if (i < players.size()) {
                text.setText((i + 1) + ". " + players.get(i));
            } else text.setText("");
        }
        Text text = (Text) lobbyChildren.get(players.size() + 1);
        text.setText(s);
    }
}
