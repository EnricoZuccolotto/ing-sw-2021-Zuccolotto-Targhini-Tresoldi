package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import it.polimi.ingsw.model.Market;


public class BoardController extends ViewObservable implements SceneController {
    @FXML
    private Button pushColumn0Button;
    @FXML
    private Button pushColumn1Button;
    @FXML
    private Button pushColumn2Button;
    @FXML
    private Button pushColumn3Button;
    @FXML
    private Button pushRow0Button;
    @FXML
    private Button pushRow1Button;
    @FXML
    private Button pushRow2Button;
    @FXML
    private GridPane MarketGrid;
    @FXML
    private ImageView Slider;

    @FXML
    public void initialize() {
        Gui gui = Gui.getInstance();
        gui.setMarketController(this);
        pushColumn0Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 2, 0));
        pushColumn1Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 2, 1));
        pushColumn2Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 2, 2));
        pushColumn3Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 2, 3));
        pushRow0Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 1, 0));
        pushRow1Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 1, 1));
        pushRow2Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event, 1, 2));
    }

    private void onConnectButtonClick(Event event, int choice, int index) {
        new Thread(() -> notifyObserver(obs -> obs.getMarket(choice, index))).start();
    }

    public void updateMarket(Market m) {
        Image image;
        image = new Image(m.getSlide().getBallImagePath());

        Slider.setImage(image);
        ObservableList<Node> marketChildren = MarketGrid.getChildren();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++) {
                ImageView imageView = (ImageView) marketChildren.get(i + j * 4);
                image = new Image((m.getResource(j, i).getBallImagePath()));
                imageView.setImage(image);
            }

    }

}
