package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.controllers.AlertController;
import it.polimi.ingsw.view.gui.controllers.SceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
            // TODO: Error
        }
        return controller;
    }

    public static <T> T changeActivePanel(List<ViewObserver> observerList, String fxmlSource){
        return changeActivePanel(observerList, activeScene, fxmlSource);
    }

    public static void showAlertWindow(String title, String message){
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/alert.fxml"));

        Parent parent;
        try {
            parent = loader.load();
        } catch(IOException ex){
            // ERROR
            return;
        }
        AlertController alertController = loader.getController();
        Scene alertScene = new Scene(parent);
        alertController.setTitleLabelMessage(title);
        alertController.setTextLabelMessage(message);
        alertController.setScene(alertScene);
        alertController.show();

    }
}
