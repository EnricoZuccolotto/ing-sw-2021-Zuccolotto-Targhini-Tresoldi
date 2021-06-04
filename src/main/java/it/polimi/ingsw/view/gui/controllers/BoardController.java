package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;


public class BoardController extends ViewObservable implements SceneController {

    private ArrayList<ImageView> hover;
    private boolean view = true, flag = true;

    private ArrayList<Integer> choice;
    private ArrayList<Resources> resourcesToSend;
    private Colors colors;


    @FXML
    private BorderPane Board;
    @FXML
    private VBox FirstAction;
    @FXML
    private Pane chooseResource;
    @FXML
    private AnchorPane playerBoard;
    //playerboard components
    @FXML
    private HBox secondRow;
    @FXML
    private HBox thirdRow;
    @FXML
    private ImageView firstRow;
    @FXML
    private Text coinCount;
    @FXML
    private Text shieldCount;
    @FXML
    private Text servantCount;
    @FXML
    private Text stoneCount;
    //faith path components
    @FXML
    private ImageView faith1;
    @FXML
    private ImageView faith2;
    @FXML
    private ImageView faith3;
    //decks components
    @FXML
    private GridPane decks;
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
    //choose resource components
    @FXML
    private Button coinButton;
    @FXML
    private Button shieldButton;
    @FXML
    private Button servantButton;
    @FXML
    private Button stoneButton;
    @FXML
    private Text resourceText;

    @FXML
    public void initialize() {
        Gui gui = Gui.getInstance();
        gui.setBoardController(this);
        //board buttons
        viewBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewBoard(flag));
        //market buttons
        pushColumn0Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 0));
        pushColumn1Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 1));
        pushColumn2Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 2));
        pushColumn3Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(2, 3));
        pushRow0Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(1, 0));
        pushRow1Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(1, 1));
        pushRow2Button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMarketArrowButtonClick(1, 2));
        //decks button
        ObservableList<Node> decksChildren = decks.getChildren();
        for (Colors colors : Colors.values())
            for (int j = 0; j < 3; j++) {
                int finalJ = j;
                decksChildren.get(colors.ordinal() * 3 + (j)).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onDecksCardSelection(colors, finalJ + 1));
            }
        //first action buttons
        choice = new ArrayList<>();
        hover = new ArrayList<>();

        Confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConfirm());


        Card1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(0));
        Card2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(1));
        Card3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(2));
        Card4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(3));

        hover.add(HoverCard1);
        hover.add(HoverCard2);
        hover.add(HoverCard3);
        hover.add(HoverCard4);

        //choose resource buttons
        DragController dragController = new DragController(chooseResource, true);
        resourcesToSend = new ArrayList<>();
        coinButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.COIN));
        shieldButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.SHIELD));
        servantButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.SERVANT));
        stoneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.STONE));
    }

    //first methods
    private void onConfirm() {
        new Thread(() -> notifyObserver(obs -> obs.firstAction(choice.get(0), choice.get(1)))).start();
        showBoard();
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

    //market methods
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

    //decks methods
    public void updateDecks(Decks NewDecks) {
        Image image;

        ObservableList<Node> decksChildren = decks.getChildren();
        for (Colors colors : Colors.values())
            for (int j = 0; j < 3; j++) {
                ImageView imageView = (ImageView) decksChildren.get(colors.ordinal() * 3 + (j));
                image = new Image((NewDecks.getDeck(colors, j + 1).getFirstCard().getImagePath()));
                imageView.setImage(image);
            }
    }

    private void onDecksCardSelection(Colors colors, int level) {
        this.colors = colors;
        choice.add(level);
        System.out.println(colors + "  " + level);
    }

    //playerBoard method
    public void updatePlayerBoard(PlayerBoard board) {
        //strongbox
        coinCount.setText(board.getNumberResourceStrongbox(Resources.COIN) + "");
        shieldCount.setText(board.getNumberResourceStrongbox(Resources.SHIELD) + "");
        servantCount.setText(board.getNumberResourceStrongbox(Resources.SERVANT) + "");
        stoneCount.setText(board.getNumberResourceStrongbox(Resources.STONE) + "");
        //warehouse
        firstRow.setImage(new Image(board.getResourceWarehouse(0).getImagePath()));
        ((ImageView) secondRow.getChildren().get(0)).setImage(new Image(board.getResourceWarehouse(1).getImagePath()));
        ((ImageView) secondRow.getChildren().get(1)).setImage(new Image(board.getResourceWarehouse(2).getImagePath()));
        for (int i = 0; i < 3; i++)
            ((ImageView) thirdRow.getChildren().get(i)).setImage(new Image(board.getResourceWarehouse(i + 3).getImagePath()));

    }

    //faith path methods
    public void updateFaithPath(FaithPath faithPath, int playerNumber) {
        ImageView[] faith = new ImageView[]{faith1, faith2, faith3};
        for (int i = 0; i < 3; i++) {
            if (faithPath.getCard(i).getUncovered()) {
                if (faithPath.getCardsState(i, playerNumber))
                    faith[i].setImage(new Image(faithPath.getCard(i).getImagePath()));
                else
                    faith[i].setImage(new Image("Image/Resources/white.png"));
            }
        }
    }

    //choose resource methods
    private void onResourceSelection(Resources resources) {
        resourcesToSend.add(resources);
        chooseResource.setDisable(true);
        chooseResource.setVisible(false);
        playerBoard.setDisable(false);
        Board.setDisable(false);
    }

    public void askResource() {
        chooseResource.setVisible(true);
        chooseResource.setDisable(false);
        playerBoard.setDisable(true);
        Board.setDisable(true);
    }

    public ArrayList<Resources> getResourcesToSend() {
        return resourcesToSend;
    }

    public void setChooseResourceText(String s) {
        resourceText.setText(s);
    }


    //exchange views
    private void viewBoard(boolean flag) {
        if (flag) {
            FirstAction.setDisable(view);
            FirstAction.setVisible(!view);
            Board.setVisible(view);
            Board.setDisable(true);
            if (view)
                viewBoard.setText("Return");
            else viewBoard.setText("View Board");
        } else {
            Board.setVisible(!view);
            Board.setDisable(view);
            playerBoard.setVisible(view);
            if (view)
                viewBoard.setText("Game board");
            else viewBoard.setText("Player Board");
        }
        view = !view;
    }

    public void showBoard() {
        Board.setVisible(true);
        Board.setDisable(false);
        FirstAction.setVisible(false);
        FirstAction.setDisable(true);
        flag = false;
        view = true;
        viewBoard.setText("Player Board");
    }

    public void clearChoices() {
        choice.clear();
        resourcesToSend.clear();
    }


}
