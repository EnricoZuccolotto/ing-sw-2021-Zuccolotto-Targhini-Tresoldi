package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.model.player.SpaceProd;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.Gui;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class BoardController extends ViewObservable implements SceneController {
    private final String effect = "-fx-border-color: green;" +
            "-fx-border-radius: 3px;" +
            "-fx-border-width: 3px;";
    private final String effect2 = "-fx-border-color: red;" +
            "-fx-border-radius: 3px;" +
            "-fx-border-width: 3px;";


    private boolean view = true, flag = true, notInTurn = true, panelViewBoardsActive = false;

    private Node activePanel;

    private CompressedPlayerBoard activePlayerBoard;
    private ArrayList<AnchorPane> playerBoardsToView;
    private ArrayList<Integer> choice;
    private ArrayList<Resources> resourcesToSend;
    private Colors colors;

    @FXML
    private VBox FirstAction;
    @FXML
    private Pane chooseResource, communication, Board;
    @FXML
    private AnchorPane playerBoard, playerBoard1, playerBoard2, playerBoard3;
    @FXML
    private HBox viewBoards;
    @FXML
    private ImageView arrow;

    //botActions
    @FXML
    private ImageView botActions;
    //playerboard components
    @FXML
    private ImageView inkWell;
    @FXML
    private Text name;
    @FXML
    private HBox firstRow, secondRow, thirdRow, bin, specialWarehouse1, specialWarehouse2;
    @FXML
    private Text coinCount, shieldCount, servantCount, stoneCount;
    @FXML
    private StackPane spaceProd0, spaceProd1, spaceProd2;
    @FXML
    private GridPane resourcesToSort;
    @FXML
    private ImageView shiftRow12, shiftRow23, shiftRow13;
    @FXML
    private ImageView leaderCard1, leaderCard2, active1, active2;
    @FXML
    private Button endTurn;
    //faith path components
    @FXML
    private ImageView faith1;
    @FXML
    private ImageView faith2;
    @FXML
    private ImageView faith3;
    @FXML
    private ImageView faithMarker;
    @FXML
    private ImageView faithMarkerBlack;
    //decks components
    @FXML
    private GridPane decks;
    //Market components
    @FXML
    private Button pushColumn0Button, pushColumn1Button, pushColumn2Button, pushColumn3Button;
    @FXML
    private Button pushRow0Button, pushRow1Button, pushRow2Button;
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
    private ImageView Card1, Card2, Card3, Card4;

    @FXML
    private ImageView HoverCard1, HoverCard2, HoverCard3, HoverCard4;

    //choose resource components
    @FXML
    private Button coinButton, shieldButton, servantButton, stoneButton;
    @FXML
    private Text resourceText;

    @FXML
    public void initialize() {

        Gui gui = Gui.getInstance();
        gui.setBoardController(this);
        //set first pane to show
        activePanel = FirstAction;
        activePanel.setVisible(true);
        activePanel.setDisable(false);
        //playerBoard to view
        playerBoardsToView = new ArrayList<>();
        playerBoardsToView.add(playerBoard);
        playerBoardsToView.add(playerBoard1);
        playerBoardsToView.add(playerBoard2);
        playerBoardsToView.add(playerBoard3);
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

        Confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConfirm());


        Card1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(0));
        Card2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(1));
        Card3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(2));
        Card4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onCardSelection(3));



        //choose resource buttons
        new DragController(chooseResource, true);
        resourcesToSend = new ArrayList<>();
        coinButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.COIN));
        shieldButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.SHIELD));
        servantButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.SERVANT));
        stoneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceSelection(Resources.STONE));

        //Temporary storage
        for (int i = 0; i < 4; i++) {
            ImageView imageView = (ImageView) resourcesToSort.getChildren().get(i);
            //dragController = new DragController(imageView, true, true);
            int finalI1 = i;
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onResourceWhite(finalI1));
            imageView.setOnDragDetected(event -> {
                /* drag was detected, start drag-and-drop gesture*/
                /* allow any transfer mode */
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                /* put the image on dragBoard */
                ClipboardContent content = new ClipboardContent();
                choice.add(0, finalI1);
                activeWarehouse(false);
                content.putImage(imageView.getImage());
                db.setContent(content);
                event.consume();
            });
        }

        //warehouse
        initializeWarehouse();
        shiftRow12.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickShiftRows(1, 2));
        shiftRow13.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickShiftRows(1, 3));
        shiftRow23.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickShiftRows(2, 3));
        //leader cards
        leaderCard1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickLeaderInactive(0));
        leaderCard2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickLeaderInactive(1));
        active1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onSpecialProduction());
        active2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onSpecialProduction());
        //end turn
        endTurn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onEndTurn());
        //communication
        new DragController(communication, true);

        //view player Boards
        viewBoards.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!panelViewBoardsActive) {
                viewBoards.setTranslateX(viewBoards.getTranslateX() - 170);
                arrow.setRotate(180);
                panelViewBoardsActive = true;
            } else {
                viewBoards.setTranslateX(viewBoards.getTranslateX() + 170);
                arrow.setRotate(0);
                panelViewBoardsActive = false;
            }


        });

        decksChildren = ((VBox) viewBoards.getChildren().get(1)).getChildren();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            decksChildren.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                changeActivePane(playerBoardsToView.get(finalI));
                view = true;
            });
        }

    }


    //first methods
    private void onConfirm() {
        new Thread(() -> notifyObserver(obs -> obs.firstAction(choice.get(0), choice.get(1)))).start();
        changeActivePane(Board);
    }

    private void onCardSelection(int index) {
        ImageView[] hover = new ImageView[]{HoverCard1, HoverCard2, HoverCard3, HoverCard4};
        if (choice.contains(index)) {
            choice.remove((Integer) index);
            hover[index].setVisible(false);
        } else {
            choice.add(index);
            hover[index].setVisible(true);
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
                {
                    ImageView imageView = (ImageView) decksChildren.get(colors.ordinal() * 3 + (j));
                    if (NewDecks.getDeck(colors, j + 1).getDeck().size() > 0)
                        image = new Image((NewDecks.getDeck(colors, j + 1).getFirstCard().getImagePath()));
                    else image = new Image(Resources.WHITE.getImagePath());
                    imageView.setImage(image);
                }

            }
    }

    private void onDecksCardSelection(Colors colors, int level) {
        this.colors = colors;
        choice.add(level);
        System.out.println(colors + "  " + level);
    }

    //playerBoard method
    public void updatePlayerBoard(CompressedPlayerBoard playerBoard) {
        ImageView imageViews;
        //vievs other player boards method
        ((VBox) viewBoards.getChildren().get(1)).getChildren().get(0).setDisable(false);
        ((VBox) viewBoards.getChildren().get(1)).getChildren().get(0).setVisible(true);
        ((Button) ((VBox) viewBoards.getChildren().get(1)).getChildren().get(0)).setText("My Board");

        activePlayerBoard = playerBoard;
        PlayerBoard board = playerBoard.getPlayerBoard();
        //name
        name.setText((playerBoard.getPlayerNumber() + 1) + ". " + playerBoard.getName());
        //inkwell
        inkWell.setVisible(playerBoard.getPlayerBoard().getInkwell());
        //strongbox
        coinCount.setText(board.getNumberResourceStrongbox(Resources.COIN) + "");
        shieldCount.setText(board.getNumberResourceStrongbox(Resources.SHIELD) + "");
        servantCount.setText(board.getNumberResourceStrongbox(Resources.SERVANT) + "");
        stoneCount.setText(board.getNumberResourceStrongbox(Resources.STONE) + "");
        //warehouse
        ((ImageView) firstRow.getChildren().get(0)).setImage(new Image(board.getResourceWarehouse(0).getImagePath()));
        ((ImageView) secondRow.getChildren().get(0)).setImage(new Image(board.getResourceWarehouse(1).getImagePath()));
        ((ImageView) secondRow.getChildren().get(1)).setImage(new Image(board.getResourceWarehouse(2).getImagePath()));
        for (int i = 0; i < 3; i++)
            ((ImageView) thirdRow.getChildren().get(i)).setImage(new Image(board.getResourceWarehouse(i + 3).getImagePath()));
        //spaceProd
        StackPane[] spacesView = new StackPane[]{spaceProd0, spaceProd1, spaceProd2};
        for (int i = 0; i < 3; i++) {
            try {
                SpaceProd spaceProd = board.getProductionSpaces().get(i);
                if (spaceProd.getNumbCard() != 0) {
                    for (DevelopmentCard card : spaceProd.getCards()) {
                        imageViews = (ImageView) spacesView[i].getChildren().get(2 - spaceProd.getCards().indexOf(card));
                        spacesView[i].setDisable(false);
                        imageViews.setImage(new Image(card.getImagePath()));
                    }
                } else spacesView[i].setDisable(true);
            } catch (IndexOutOfBoundsException e) {
                spacesView[i].setDisable(true);
            }
        }
        //temporary storage
        for (int i = 0; i < 4; i++) {
            imageViews = (ImageView) resourcesToSort.getChildren().get(i);
            if (i < playerBoard.getTemporaryResourceStorage().size()) {
                Resources resources = playerBoard.getTemporaryResourceStorage().get(i);
                imageViews.setVisible(true);
                imageViews.setDisable(false);
                imageViews.setImage(new Image(resources.getImagePath()));
            } else {
                imageViews.setVisible(false);
                imageViews.setDisable(true);
            }
        }
        //leaderCards
        ImageView[] leader = new ImageView[]{leaderCard1, leaderCard2, active1, active2};
        HBox[] warehouse = new HBox[]{specialWarehouse1, specialWarehouse2};
        for (int i = 0; i < 2; i++) {
            try {
                LeaderCard card = board.getLeaderCard(i);
                if (card.getUncovered()) {
                    leader[i].setDisable(true);
                    leader[i].setVisible(false);
                    leader[i + 2].setImage(new Image(card.getImagePath()));
                    leader[i + 2].setVisible(true);
                    //updating special warehouse
                    updateSpecialWarehouse(card, warehouse[i], activePlayerBoard);
                } else leader[i].setImage(new Image(card.getImagePath()));
            } catch (IndexOutOfBoundsException e) {
                leader[i].setDisable(true);
                leader[i].setVisible(false);
            }
        }
    }

    public void updatePlayerBoard2(CompressedPlayerBoard playerBoard, int n) {

        ImageView imageViews;
        ((VBox) viewBoards.getChildren().get(1)).getChildren().get(n).setDisable(false);
        ((VBox) viewBoards.getChildren().get(1)).getChildren().get(n).setVisible(true);
        ((Button) ((VBox) viewBoards.getChildren().get(1)).getChildren().get(n)).setText(playerBoard.getName() + "'s Board");
        ObservableList<Node> children = playerBoardsToView.get(n).getChildren();
        PlayerBoard board = playerBoard.getPlayerBoard();
        //name
        ((Text) children.get(22)).setText((playerBoard.getPlayerNumber() + 1) + ". " + playerBoard.getName());
        //inkwell
        (children.get(21)).setVisible(playerBoard.getPlayerBoard().getInkwell());

        //strongbox
        ((Text) children.get(2)).setText(board.getNumberResourceStrongbox(Resources.SERVANT) + "");
        ((Text) children.get(3)).setText(board.getNumberResourceStrongbox(Resources.COIN) + "");
        ((Text) children.get(4)).setText(board.getNumberResourceStrongbox(Resources.STONE) + "");
        ((Text) children.get(5)).setText(board.getNumberResourceStrongbox(Resources.SHIELD) + "");

        //warehouse
        ((ImageView) ((HBox) children.get(6)).getChildren().get(0)).setImage(new Image(board.getResourceWarehouse(0).getImagePath()));
        ((ImageView) ((HBox) children.get(7)).getChildren().get(0)).setImage(new Image(board.getResourceWarehouse(1).getImagePath()));
        ((ImageView) ((HBox) children.get(7)).getChildren().get(1)).setImage(new Image(board.getResourceWarehouse(2).getImagePath()));
        for (int i = 0; i < 3; i++)
            ((ImageView) ((HBox) children.get(8)).getChildren().get(i)).setImage(new Image(board.getResourceWarehouse(i + 3).getImagePath()));

        //spaceProd
        StackPane[] spacesView = new StackPane[]{(StackPane) children.get(15), (StackPane) children.get(16), (StackPane) children.get(17)};
        for (int i = 0; i < 3; i++) {
            try {
                SpaceProd spaceProd = board.getProductionSpaces().get(i);
                if (spaceProd.getNumbCard() != 0) {
                    for (DevelopmentCard card : spaceProd.getCards()) {
                        imageViews = (ImageView) spacesView[i].getChildren().get(2 - spaceProd.getCards().indexOf(card));
                        imageViews.setImage(new Image(card.getImagePath()));
                    }
                }
            } catch (IndexOutOfBoundsException ignored) {
                break;
            }
        }

        //temporary storage
        for (int i = 0; i < 4; i++) {
            imageViews = (ImageView) ((GridPane) children.get(18)).getChildren().get(i);
            if (i < playerBoard.getTemporaryResourceStorage().size()) {
                Resources resources = playerBoard.getTemporaryResourceStorage().get(i);
                imageViews.setVisible(true);
                imageViews.setImage(new Image(resources.getImagePath()));
            } else {
                imageViews.setVisible(false);
            }
        }

        //leaderCards
        ImageView[] leader = new ImageView[]{(ImageView) children.get(19), (ImageView) children.get(20)};
        HBox[] warehouse = new HBox[]{(HBox) children.get(9), (HBox) children.get(10)};
        for (int i = 0; i < 2; i++) {
            try {
                LeaderCard card = board.getLeaderCard(i);
                leader[i].setVisible(true);
                if (card.getUncovered()) {
                    leader[i].setImage(new Image(card.getImagePath()));
                    //updating special warehouse
                    updateSpecialWarehouse(card, warehouse[i], playerBoard);
                } else leader[i].setImage(new Image("Image/Cards/Leader/back.png"));
            } catch (IndexOutOfBoundsException e) {
                leader[i].setImage(new Image("Image/Resources/white.png"));
            }
        }

    }

    private void updateSpecialWarehouse(LeaderCard card, HBox warehouse, CompressedPlayerBoard playerBoard) {

        if (card.getAdvantage().equals(Advantages.WAREHOUSE)) {
            int res = 0;
            for (int j = 0; j < 4; j++)
                if (card.getEffect().get(j) != 0)
                    res = j;
            for (int j = 0; j < 2; j++) {
                if (j < playerBoard.getPlayerBoard().getExtraResources().get(res)) {
                    ((ImageView) warehouse.getChildren().get(j)).setImage(new Image(Resources.transform(res).getImagePath()));
                } else
                    ((ImageView) warehouse.getChildren().get(j)).setImage(new Image(Resources.WHITE.getImagePath()));
            }
        }
    }

    //warehouse
    private void onDropOnWarehouse(int index, int row) {

        new Thread(() -> notifyObserver(obs -> obs.sortingMarket(activePlayerBoard.getTemporaryResourceStorage().get(index), row, index))).start();
        activeWarehouse(true);
        choice.clear();
    }

    public void activeWarehouse(boolean active) {
        firstRow.setDisable(active);
        secondRow.setDisable(active);
        thirdRow.setDisable(active);
        bin.setDisable(active);
        bin.setVisible(!active);
        HBox[] spec = new HBox[]{specialWarehouse1, specialWarehouse2};
        for (int i = 0; i < 2; i++)
            try {
                if (activePlayerBoard.getPlayerBoard().getLeaderCard(i).getUncovered() && activePlayerBoard.getPlayerBoard().getLeaderCard(i).getAdvantage().equals(Advantages.WAREHOUSE))
                    spec[i].setDisable(active);
            } catch (IndexOutOfBoundsException ignored) {

            }
    }

    private void onClickShiftRows(int row1, int row2) {
        new Thread(() -> notifyObserver(obs -> obs.switchRows(row1, row2))).start();

    }

    private void initializeWarehouse() {
        Node[] warehouseRows = new Node[]{firstRow, secondRow, thirdRow, specialWarehouse1, specialWarehouse2, bin};
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            warehouseRows[i].setOnDragEntered(event -> {
                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != firstRow &&
                        event.getDragboard().hasImage()) {
                    if (finalI < 5)
                        warehouseRows[finalI].setStyle(effect);
                    else warehouseRows[finalI].setStyle(effect2);
                }
                event.consume();
            });
            warehouseRows[i].setOnDragExited(event -> {
                /* mouse moved away, remove the graphical cues */
                warehouseRows[finalI].setStyle("");
                event.consume();
            });

            warehouseRows[i].setOnDragOver(event -> {
                /* data is dragged over the target */
                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != warehouseRows[finalI] &&
                        event.getDragboard().hasImage()) {
                    /* allow for moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            warehouseRows[i].setOnDragDropped(event -> {
                /* data dropped */
                /* if there is a string data on dragBoard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    if (finalI < 3)
                        onDropOnWarehouse(choice.get(0), finalI + 1);
                    else if (finalI < 5)
                        onDropOnWarehouse(choice.get(0), 0);
                    else onDropOnWarehouse(choice.get(0), 4);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });

        }
    }

    //on temporary resource white selection
    private void onResourceWhite(int index) {
        if (activePlayerBoard.getTemporaryResourceStorage().get(index).equals(Resources.WHITE)) {
            playerBoard.setDisable(true);
            chooseResource.setDisable(false);
            chooseResource.setVisible(true);
            Button[] buttons = new Button[]{servantButton, coinButton, stoneButton, shieldButton};
            for (int i = 0; i < 4; i++)
                buttons[i].setDisable(!activePlayerBoard.getPlayerBoard().isResourceSubstitutable(Resources.transform(i)));
            while (resourcesToSend.size() != 1)
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException E) {
                    System.exit(3);
                }
            ((ImageView) resourcesToSort.getChildren().get(index)).setImage(new Image(resourcesToSend.get(0).getImagePath()));
            activePlayerBoard.getTemporaryResourceStorage().set(index, resourcesToSend.get(0));
            chooseResource.setVisible(false);
            chooseResource.setDisable(true);
        }
    }

    //end turn
    private void onEndTurn() {
        new Thread(() -> notifyObserver(ViewObserver::endTurn)).start();
        endTurn.setDisable(true);
        endTurn.setVisible(false);
        clearTemporary();
    }

    private void clearTemporary() {
        for (int i = 0; i < 4; i++) {
            ImageView imageViews = (ImageView) resourcesToSort.getChildren().get(i);
            imageViews.setDisable(true);
            imageViews.setImage(new Image(Resources.WHITE.getImagePath()));
        }
    }


    public void activeEndTurn(boolean active) {
        endTurn.setVisible(active);
        endTurn.setDisable(!active);
    }

    //leader card method
    private void onClickLeaderInactive(int index) {
        new Thread(() -> notifyObserver(obs -> obs.activeLeader(index))).start();
    }

    //productionAction
    private void onSpecialProduction() {

    }

    //faith path methods
    public void updateFaithPath(FaithPath faithPath, int playerNumber) {
        int position = faithPath.getPosition(playerNumber);
        setFaithCards(this.faith1, this.faith2, this.faith3, faithPath, playerNumber);
        setFaithMarkerPosition(position, this.faithMarker);
        if (faithPath.isSinglePlayer()) {
            faithMarkerBlack.setVisible(true);
            setFaithMarkerPosition(faithPath.getPosition(1), faithMarkerBlack);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(0);
            list.add(1);
            list.add(2);
            list.add(3);
            list.remove(playerNumber);
            for (int i = 1; i < 4; i++) {
                try {
                    ObservableList<Node> children = playerBoardsToView.get(i).getChildren();
                    setFaithCards((ImageView) children.get(11), (ImageView) children.get(12), (ImageView) children.get(13), faithPath, list.get(i - 1));
                    setFaithMarkerPosition(faithPath.getPosition(list.get(i - 1)), (ImageView) children.get(14));
                } catch (IndexOutOfBoundsException ignored) {
                    break;
                }
            }


        }
    }

    private void setFaithCards(ImageView faith1, ImageView faith2, ImageView faith3, FaithPath faithPath, int playerNumber) {
        ImageView[] faith = new ImageView[]{faith1, faith2, faith3};
        for (int i = 0; i < 3; i++) {
            if (faithPath.getCard(i).getUncovered()) {
                if (faithPath.getCardsState(i, playerNumber)) {
                    faith[i].setImage(new Image(faithPath.getCard(i).getImagePath()));
                } else
                    faith[i].setImage(new Image("Image/Resources/white.png"));
            }
        }
    }


    private void setFaithMarkerPosition(int position, ImageView faithMarker) {
        //setting x coordinates

        if (position < 3)
            faithMarker.setLayoutX(39 + 49 * position);
        else if (position < 5)
            faithMarker.setLayoutX(39 + 49 * 2);
        else if (position < 10)
            faithMarker.setLayoutX(39 + 49 * (position - 2));
        else if (position < 12)
            faithMarker.setLayoutX(39 + 49 * 7);
        else if (position < 17)
            faithMarker.setLayoutX(39 + 49 * (position - 4));
        else if (position < 19)
            faithMarker.setLayoutX(39 + 49 * 12);
        else faithMarker.setLayoutX(39 + 49 * (position - 6));
        //setting y coordinates
        if (position < 3 || position >= 11 && position <= 16)
            faithMarker.setLayoutY(124);
        else if (position >= 4 && position <= 9 || position >= 18)
            faithMarker.setLayoutY(27);
        else faithMarker.setLayoutY(76);
    }

    //choose resource methods
    private void onResourceSelection(Resources resources) {
        resourcesToSend.add(resources);
        askResource(false);
    }

    public void askResource(boolean visible) {
        chooseResource.setVisible(visible);
        chooseResource.setDisable(!visible);
        activePanel.setDisable(visible);
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

            if (view) {
                changeActivePane(Board);
            } else changeActivePane(FirstAction);

        } else {

            if (view) changeActivePane(playerBoard);
            else changeActivePane(Board);

        }

        view = !view;
    }

    public void showBoard(boolean singlePlayer) {
        changeActivePane(Board);
        flag = false;
        view = true;

        if (!singlePlayer) {
            viewBoards.setVisible(true);
            viewBoards.setDisable(false);
        }
    }

    public void clearChoices() {
        resourcesToSend.clear();
        choice.clear();
    }

    public void changeActivePane(Node node) {
        activePanel.setVisible(false);
        activePanel.setDisable(true);
        activePanel = node;
        node.setVisible(true);
        node.setDisable(false);
    }

    public void activeBotActions(boolean active) {
        botActions.setVisible(active);
    }

    public void setBotActions(Image image) {
        botActions.setImage(image);
    }

    public void activeMarket(boolean active) {
        pushColumn0Button.setDisable(!active);
        pushColumn1Button.setDisable(!active);
        pushColumn2Button.setDisable(!active);
        pushColumn3Button.setDisable(!active);
        pushRow0Button.setDisable(!active);
        pushRow1Button.setDisable(!active);
        pushRow2Button.setDisable(!active);
    }

    public void activeDecks(boolean active) {
        decks.setDisable(!active);
    }

    public void activeProductions(boolean active) {
        StackPane[] spaces = new StackPane[]{spaceProd0, spaceProd1, spaceProd2};
        for (int i = 0; i < 3; i++) {
            if (i < activePlayerBoard.getPlayerBoard().getProductionSpaces().size()) {
                if (activePlayerBoard.getPlayerBoard().getProductionSpaces().get(i).getNumbCard() == 0)
                    spaces[i].setDisable(true);
                else
                    spaces[i].setDisable(active);
            } else spaces[i].setDisable(true);
        }
        ImageView[] leader = new ImageView[]{active1, active2};
        for (int i = 0; i < activePlayerBoard.getPlayerBoard().getLeaderCardsNumber(); i++)
            if (activePlayerBoard.getPlayerBoard().getLeaderCard(i).getUncovered() && activePlayerBoard.getPlayerBoard().getLeaderCard(i).getAdvantage().equals(Advantages.PROD))
                leader[i].setDisable(!active);
    }

    public void notInTurn(boolean active) {
        notInTurn = active;
        decks.setDisable(active);
        leaderCard1.setDisable(active);
        leaderCard2.setDisable(active);
        active1.setDisable(active);
        active2.setDisable(active);
        activeMarket(!active);
        activeProductions(!active);

        //sort warehouse
        shiftRow12.setDisable(active);
        shiftRow23.setDisable(active);
        shiftRow13.setDisable(active);
    }

    //communication
    public void showCommunication(String communication, boolean visible) {
        ((Text) this.communication.getChildren().get(0)).setText(communication);
        this.communication.setVisible(visible);
    }


}
