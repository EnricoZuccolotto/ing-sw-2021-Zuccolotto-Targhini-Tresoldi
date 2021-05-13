package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.view.gui.controllers.MenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiEntryPoint extends Application  {
    @Override
    public void start(Stage stage){
        Gui view = new Gui();
        ClientManager clientManager = new ClientManager(view);
        view.addObserver(clientManager);

        // Load menu layout
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/menu.fxml"));
        Parent layout = null;
        try {
            layout = loader.load();
        } catch(IOException e){
            // TODO: error loading file
        }
        MenuController controller = loader.getController();
        controller.addObserver(clientManager);

        // Create the scene
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setWidth(1280d);
        stage.setHeight(720d);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setTitle("Masters of Renaissance");
        stage.show();
    }

    @Override
    public void stop(){
        Platform.exit();
        System.exit(0);
    }
}
