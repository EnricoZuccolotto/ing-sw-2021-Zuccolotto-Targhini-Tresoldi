package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class BoardController extends ViewObservable implements SceneController {
    private final String effect = " -fx-effect: dropshadow(three-pass-box, rgba(0,0,200,1), 20, 0, 0, 0);";
    private ArrayList<Integer> choice;
    private ArrayList<ImageView> hover;
    private boolean view = true;
    @FXML
    private BorderPane Board;
    @FXML
    private VBox FirstAction;

    //Market components
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

    //first action components
    @FXML
    private Button Confirm;

    @FXML
    private Button viewBoard;
    @FXML
    private ImageView Card1;
    @FXML
    private ImageView Card2;
    @FXML
    private ImageView Card3;
    @FXML
    private ImageView Card4;
    @FXML
    private ImageView HoverCard1;
    @FXML
    private ImageView HoverCard2;
    @FXML
    private ImageView HoverCard3;
    @FXML
    private ImageView HoverCard4;

    @FXML
    public void initialize() {
        Gui gui = Gui.getInstance();
        gui.setMarketController(this);
        //board buttons
        //market buttons
        pushColumn0Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 0));
        pushColumn1Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 1));
        pushColumn2Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 2));
        pushColumn3Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 3));
        pushRow0Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(1, 0));
        pushRow1Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(1, 1));
        pushRow2Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(1, 2));

        //first action buttons
        choice = new ArrayList<>();
        hover = new ArrayList<>();

        Confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConfirm());
        viewBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewBoard());

        Card1.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> Card1.setStyle(effect));
        Card1.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> Card1.setStyle(""));

        Card2.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> Card2.setStyle(effect));
        Card2.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> Card2.setStyle(""));

        Card3.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> Card3.setStyle(effect));
        Card3.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> Card3.setStyle(""));

        Card4.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> Card4.setStyle(effect));
        Card4.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> Card4.setStyle(""));

        Card1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(0));
        Card2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(1));
        Card3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(2));
        Card4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(3));

        hover.add(HoverCard1);
        hover.add(HoverCard2);
        hover.add(HoverCard3);
        hover.add(HoverCard4);
    }

    private void onConfirm() {
        new Thread(() -> notifyObserver(obs -> obs.firstAction(choice.get(0), choice.get(1)))).start();
    }

    private void onCardSelection(int index) {
        if (choice.contains(index)) {
            choice.remove((Integer) index);
            hover.get(index).setVisible(false);
        } else {
            choice.add(index);
            hover.get(index).setVisible(true);
        }

        Confirm.setDisable(!(choice.size() == 2));
    }

    public void updateFirstAction(PlayerBoard playerBoard) {
        Card1.setImage(new Image(playerBoard.getLeaderCard(0).getImagePath()));
        Card2.setImage(new Image(playerBoard.getLeaderCard(1).getImagePath()));
        Card3.setImage(new Image(playerBoard.getLeaderCard(2).getImagePath()));
        Card4.setImage(new Image(playerBoard.getLeaderCard(3).getImagePath()));
    }


    private void onMarketArrowButtonClick(int choice, int index) {
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

    private void viewBoard() {
        FirstAction.setDisable(view);
        FirstAction.setVisible(!view);
        Board.setVisible(view);
        Board.setDisable(true);
        if (view)
            viewBoard.setText("Return");
        else viewBoard.setText("View Board");
        view = !view;
    }

    public void showBoard() {
        Board.setVisible(true);
        Board.setDisable(false);
        FirstAction.setVisible(false);
        FirstAction.setDisable(true);
        viewBoard.setDisable(true);
        viewBoard.setVisible(false);
    }


}
