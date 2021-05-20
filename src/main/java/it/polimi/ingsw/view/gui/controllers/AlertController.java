package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.GuiSceneUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertController extends ViewObservable implements SceneController {
    private final Stage stage;

    @FXML
    private Label titleLabel;
    @FXML
    public Label textLabel;
    @FXML
    public Button closeButton;

    public AlertController(){
        stage = new Stage();
        stage.initOwner(GuiSceneUtils.getActiveScene().getWindow());
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    @FXML
    public void initialize(){
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCloseButtonClick);
    }

    public void setTitleLabelMessage(String title){
        titleLabel.setText(title);
    }

    public void setTextLabelMessage(String text){
        textLabel.setText(text);
    }

    private void onCloseButtonClick(Event event){
        stage.close();
    }

    public void setScene(Scene scene){
        stage.setScene(scene);
    }

    public void show(){
        stage.showAndWait();
    }
}
