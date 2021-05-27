package it.polimi.ingsw.view.gui.controllers;

import com.sun.glass.ui.PlatformFactory;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.GuiSceneUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class UsernameController extends ViewObservable implements SceneController {
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Button connectButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
       backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButtonClick);
       connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
    }

    private void onBackButtonClick(Event event){
        connectButton.setDisable(true);
        backButton.setDisable(true);

        new Thread(() -> notifyObserver(ViewObserver::onDisconnect)).start();
        GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "menu.fxml");
    }

    private void onConnectButtonClick(Event event){
        connectButton.setDisable(true);
        backButton.setDisable(true);

        String name = nicknameTextField.getText();

        new Thread(() -> notifyObserver(obs -> obs.Nickname(name))).start();
    }
}
