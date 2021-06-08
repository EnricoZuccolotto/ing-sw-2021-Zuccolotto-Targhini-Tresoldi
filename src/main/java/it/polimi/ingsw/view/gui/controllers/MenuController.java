package it.polimi.ingsw.view.gui.controllers;

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
    public void initialize(){
        Gui gui = Gui.getInstance();
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
        exitButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) System.exit(0);
        });
        connectButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(GuiSceneUtils.isAnEnterKeyEvent(keyEvent)) onConnectButtonClick(keyEvent);
        });

        if(gui.isLocal()) connectButton.setText("Start...");
    }

    private void onConnectButtonClick(Event event){
        Gui gui = Gui.getInstance();
        if(gui.isLocal()){
            GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "username.fxml");
        } else {
            GuiSceneUtils.changeActivePanel(observers, ((Node) event.getSource()).getScene(), "connect.fxml");
        }
    }
}
