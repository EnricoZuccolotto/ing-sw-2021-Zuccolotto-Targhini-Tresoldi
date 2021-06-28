package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.tools.MORLogger;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.controllers.SceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.List;

public class GuiSceneUtils {
    private static Scene activeScene;

    public static Scene getActiveScene(){
        return activeScene;
    }

    public static <T> T changeActivePanel(List<ViewObserver> observerList, Scene scene, String fxmlSource){
        T controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxmlSource));
            Parent root = loader.load();
            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observerList);

            activeScene = scene;
            activeScene.setRoot(root);
        } catch(IOException e){
            MORLogger.LOGGER.severe("Could not load the scene from file! Game will end!");
            System.exit(1);
        }
        return controller;
    }

    public static <T> T changeActivePanel(List<ViewObserver> observerList, String fxmlSource){
        return changeActivePanel(observerList, activeScene, fxmlSource);
    }

    public static void showAlertWindow(AlertType severity, String title, String message){
        Alert alertWindow = new Alert(severity, message);
        alertWindow.setTitle(title);
        alertWindow.setHeaderText(title);
        alertWindow.setResizable(false);
        alertWindow.initOwner(activeScene.getWindow());
        alertWindow.showAndWait();
    }

    public static boolean isAnEnterKeyEvent(KeyEvent keyEvent){
        return keyEvent.getCode() == KeyCode.ENTER;
    }
}
