package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.GuiSceneUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ConnectController extends ViewObservable implements SceneController {
    @FXML
    private Button connectButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField ipAddressTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private Label ipAddressErrorLabel;
    @FXML
    private Label portErrorLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ProgressBar loadingProgressBar;

    @FXML
    public void initialize() {
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButtonClick);
        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onConnectButtonClick(keyEvent);
        });
        backButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onBackButtonClick(keyEvent);
        });
    }

    /**
     * Return to the menu.
     */
    private void onBackButtonClick(Event event) {
        GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "menu.fxml");
    }

    /**
     * Asks the player the server info and connects him to the server.
     */
    private void onConnectButtonClick(Event event) {
        String address = ipAddressTextField.getText();
        String port = portTextField.getText();

        boolean isAddressValid = ClientManager.isValidIpAddress(address);
        boolean isPortValid = ClientManager.isValidPort(port);

        ipAddressErrorLabel.setVisible(!isAddressValid);
        portErrorLabel.setVisible(!isPortValid);

        if (isAddressValid && isPortValid) {
            loadingProgressBar.setVisible(true);
            backButton.setDisable(true);
            connectButton.setDisable(true);
            ipAddressTextField.setDisable(true);
            portTextField.setDisable(true);

            new Thread(() -> notifyObserver(obs -> obs.ServerInfo(address, port))).start();
        }
    }
}
