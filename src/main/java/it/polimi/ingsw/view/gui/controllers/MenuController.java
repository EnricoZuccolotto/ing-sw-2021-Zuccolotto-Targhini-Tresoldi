package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiSceneUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MenuController extends ViewObservable implements SceneController {
    @FXML
    private Button exitButton;
    @FXML
    private Button connectButton;
    @FXML
    private Button localButton;

    @FXML
    public void initialize() {

        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
        exitButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) System.exit(0);
        });
        connectButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onConnectButtonClick(keyEvent);
        });
        localButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLocal);

        localButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onLocal(keyEvent);
        });

    }

    /**
     * Starts an online game.
     */
    private void onConnectButtonClick(Event event) {
        setObservers(false);
        GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "connect.fxml");
    }

    /**
     * Starts a local game.
     */
    private void onLocal(Event event) {
        setObservers(true);
        GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "username.fxml");
    }

    /**
     * Sets the observer for the local or online game.
     */
    private void setObservers(boolean local) {
        Gui view = Gui.getInstance();
        GameController gameController = null;
        if (local) {
            gameController = new GameController(true);
            gameController.setLocalView(view);
        }
        ClientManager clientManager = view.createClientManager(gameController);
        view.removeAllObservers();
        this.removeAllObservers();
        view.addObserver(clientManager);
        this.addObserver(clientManager);
    }
}
