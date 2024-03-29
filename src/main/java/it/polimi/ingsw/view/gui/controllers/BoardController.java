package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.communication.CommunicationMessage;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * This class controls the board scene.
 */
public class BoardController extends ViewObservable implements SceneController {
    private final String effect = "-fx-border-color: green;" +
            "-fx-border-radius: 3px;" +
            "-fx-border-width: 3px;";
    private final String effect2 = "-fx-border-color: red;" +
            "-fx-border-radius: 3px;" +
            "-fx-border-width: 3px;";


    private boolean view = true;
    private boolean flag = false;//if flag is false, we did the first action
    private boolean notInTurn = true; //if true this is not our turn
    private boolean panelViewBoardsActive = false;//if false we cannot see other player's boards
    private boolean movingWarehouse = true;///if true we are moving between warehouse, if false we are moving between temporary storage and warehouses,used in drag interaction
    private boolean singlePlayer = false;
    private Node activePanel;

    private CompressedPlayerBoard activePlayerBoard;
    private ArrayList<AnchorPane> playerBoardsToView;
    private ArrayList<Integer> choice, production;
    private ArrayList<Resources> resourcesToSend;
    private Colors colors;
    private Decks deck;

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
    @FXML
    private FlowPane discard_activate_Leader;
    //botActions
    @FXML
    private ImageView botActions;
    //player board components
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
    //how to pay
    @FXML
    private VBox productionBox;
    @FXML
    private GridPane productionPane;
    @FXML
    private Button productionConfirm, abort;
    @FXML
    private ImageView temporaryCard, baseProduction;

    @FXML
    public void initialize() {

        Gui gui = Gui.getInstance();
        gui.setBoardController(this);
        //set first pane to show
        activePanel = Board;
        activePanel.setVisible(true);
        activePanel.setDisable(true);
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
                movingWarehouse = false;
                notActiveWarehouse(false);
                activeSpaceProds(false);
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
        discard_activate_Leader.getChildren().get(1).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            new Thread(() -> notifyObserver(obs -> obs.activeLeader(choice.get(0)))).start();
            discard_activate_Leader.setVisible(false);
            discard_activate_Leader.setDisable(true);
        });
        discard_activate_Leader.getChildren().get(3).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            new Thread(() -> notifyObserver(obs -> obs.foldLeader(choice.get(0)))).start();
            discard_activate_Leader.setVisible(false);
            discard_activate_Leader.setDisable(true);
        });
        discard_activate_Leader.getChildren().get(2).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            clearChoices();
            gui.askAction(null);
            discard_activate_Leader.setVisible(false);
            discard_activate_Leader.setDisable(true);
        });
        leaderCard1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickLeaderInactive(0));
        leaderCard2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickLeaderInactive(1));
        active1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onSpecialProduction(0));
        active2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onSpecialProduction(1));

        //production
        production = new ArrayList<>();
        abort.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            clearChoices();
            gui.askAction(null);
            productionBox.setVisible(false);
            productionBox.setDisable(true);
        });
        productionConfirm.setDisable(true);
        productionConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickChoose());
        new DragController(productionBox, true);
        for (int j = 7; j < 19; j = j + 1) {
            SpinnerValueFactory<Integer> spinnerV = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
            ((Spinner) productionPane.getChildren().get(j)).setValueFactory(spinnerV);
            (productionPane.getChildren().get(j)).addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> onClickCheckConfirm());
        }
        spaceProd0.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> NormalProduction(0));
        spaceProd1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> NormalProduction(1));
        spaceProd2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> NormalProduction(2));

        temporaryCard.setOnDragDetected(event -> {
            /* drag was detected, start drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = temporaryCard.startDragAndDrop(TransferMode.ANY);
            /* put the image on dragBoard */
            movingWarehouse = false;
            notActiveWarehouse(true);
            activeSpaceProds(true);
            ClipboardContent content = new ClipboardContent();
            content.putImage(temporaryCard.getImage());
            db.setContent(content);
            event.consume();
        });
        initializeProductionDrag();

        baseProduction.setDisable(true);
        baseProduction.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> BaseProduction());

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
                view = false;
            });
        }
    }
    //FIRST ACTION PANE

    /**
     * Sens to the server the indexes of the leader cards to discard in the first turn.
     */
    private void onConfirm() {
        new Thread(() -> notifyObserver(obs -> obs.firstAction(choice.get(0), choice.get(1)))).start();
        changeActivePane(Board);
        flag = false;
        view = true;
    }

    /**
     * Adds the index of the leader card chosen by the player to the array choice.
     * Sets an image hover the selected card and disables the selected card.
     *
     * @param index Index of the selected card.
     */
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

    /**
     * Shows the cards possessed by the player.
     * Change the active pane to the FIRST ACTION pane.
     *
     * @param playerBoard Player's board.
     */
    public void updateFirstAction(PlayerBoard playerBoard) {
        changeActivePane(FirstAction);
        flag = true;
        Card1.setImage(new Image(playerBoard.getLeaderCard(0).getImagePath()));
        Card2.setImage(new Image(playerBoard.getLeaderCard(1).getImagePath()));
        Card3.setImage(new Image(playerBoard.getLeaderCard(2).getImagePath()));
        Card4.setImage(new Image(playerBoard.getLeaderCard(3).getImagePath()));
    }

    //market methods

    /**
     * Sends to the server a getMarket request.
     *
     * @param choice 1 for rows and 2 for columns.
     * @param index  Index of the row/column.
     */
    private void onMarketArrowButtonClick(int choice, int index) {
        new Thread(() -> notifyObserver(obs -> obs.getMarket(choice, index))).start();
    }

    /**
     * Shows the market on the BOARD pane.
     *
     * @param m Market to show.
     */
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

    /**
     * Shows the top cards of the decks on the BOARD pane.
     *
     * @param NewDecks Decks to show.
     */
    public void updateDecks(Decks NewDecks) {
        Image image;

        ObservableList<Node> decksChildren = decks.getChildren();
        for (Colors colors : Colors.values())
            for (int j = 0; j < 3; j++) {
                {
                    ImageView imageView = (ImageView) decksChildren.get(colors.ordinal() * 3 + (j));
                    try {
                        if (NewDecks.getDeck(colors, j + 1).getDeck().size() > 0)
                            image = new Image((NewDecks.getDeck(colors, j + 1).getFirstCard().getImagePath()));
                        else {
                            image = new Image(Resources.WHITE.getImagePath());
                            imageView.setDisable(true);
                        }
                    } catch (NullPointerException e) {
                        image = new Image(Resources.WHITE.getImagePath());
                        imageView.setDisable(true);
                    }
                    imageView.setImage(image);
                }

            }
        deck = NewDecks;
    }

    /**
     * Selects a card from the decks.Starts the buy action if possible.
     * Saves the color of the card.
     *
     * @param level  Card's level.
     * @param colors Card's color.
     */
    private void onDecksCardSelection(Colors colors, int level) {
        this.colors = colors;
        int[] a = deck.getDeck(colors, level).getFirstCard().getCostCard();
        if (activePlayerBoard.getPlayerBoard().checkResources(a)) {
            buyCard(colors, level);
        }
    }

    /**
     * Buys a card from the decks.
     * Saves the level of the card.
     * Sets the spinners for the buy card action.
     * Asks for payment(ProductionBox pane).
     *
     * @param level  Card's level.
     * @param colors Card's color.
     */
    private void buyCard(Colors colors, int level) {
        production.add(level);
        production.add(0);
        int[] a = deck.getDeck(colors, level).getFirstCard().getCostCard();

        for (int i = 0; i < 4; i++) {
            // Handle resource discounts
            a[i] -= activePlayerBoard.getPlayerBoard().getResourceDiscount(Resources.transform(i));
            if (a[i] < 0) a[i] = 0;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 7 + i; j < 19; j = j + 4) {
                setSpinnerValue(a[i], j);
            }
        }
        askPayment(true);
    }

    /**
     * Starts a base production action.
     * Sets the spinners for the base production.
     * Asks the output's resource(chooseResource pane).
     */
    private void BaseProduction() {
        production.add(1000);
        production.add(10);
        for (int j = 7; j < 19; j++) {
            setSpinnerValue(2, j);
        }
        askResource(true);
    }

    /**
     * Starts a special production action of a leader card.
     * Sets the spinners for the special production.
     * Asks the output's resource(chooseResource pane).
     *
     * @param index Leader card's index.
     */
    private void onSpecialProduction(int index) {
        production.add(index);
        production.add(11);
        choice.addAll(production);
        ArrayList<Integer> a;
        a = activePlayerBoard.getPlayerBoard().getLeaderCard(index).getEffect();
        for (int i = 0; i < 4; i++)
            for (int j = 7 + i; j < 19; j = j + 4) {
                setSpinnerValue(a.get(i), j);
            }
        askResource(true);
    }

    /**
     * Starts a normal production action.
     * Sets the spinners for the normal production.
     * Asks for payment(ProductionBox pane).
     *
     * @param index Leader card's index.
     */
    private void NormalProduction(int index) {
        production.add(index);
        production.add(1);
        int[] a = activePlayerBoard.getPlayerBoard().getProductionCost(index);

        for (int i = 0; i < 4; i++)
            for (int j = 7 + i; j < 19; j = j + 4) {
                setSpinnerValue(a[i], j);
            }
        askPayment(true);
    }

    /**
     * Sets the value of the spinner in position j according to the value i.
     *
     * @param i Number of resource to pay.
     * @param j Index of the spinner.
     */
    private void setSpinnerValue(int i, int j) {
        if (j < 11 && activePlayerBoard.getPlayerBoard().getWarehouse().getNumberResource(Resources.transform((int) Math.floor((j - 7) % 4))) < i) {
            SpinnerValueFactory<Integer> spinnerV = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, activePlayerBoard.getPlayerBoard().getWarehouse().getNumberResource(Resources.transform((int) Math.floor((j - 7) % 4))), 0);
            ((Spinner) productionPane.getChildren().get(j)).setValueFactory(spinnerV);
        } else if (j < 15 && j > 10 && activePlayerBoard.getPlayerBoard().getStrongbox().getResources(Resources.transform((int) Math.floor((j - 7) % 4))) < i) {
            SpinnerValueFactory<Integer> spinnerV = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, activePlayerBoard.getPlayerBoard().getStrongbox().getResources(Resources.transform((int) Math.floor((j - 7) % 4))), 0);
            ((Spinner) productionPane.getChildren().get(j)).setValueFactory(spinnerV);
        } else if (j > 14 && activePlayerBoard.getPlayerBoard().getExtraResources().get(j - 15) < i) {
            SpinnerValueFactory<Integer> spinnerV = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, activePlayerBoard.getPlayerBoard().getExtraResources().get(j - 15), 0);
            ((Spinner) productionPane.getChildren().get(j)).setValueFactory(spinnerV);
        } else {
            SpinnerValueFactory<Integer> spinnerV = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, i, 0);
            ((Spinner) productionPane.getChildren().get(j)).setValueFactory(spinnerV);
        }
    }

    /**
     * Sends to the server the message to perform a production.
     */
    private void onClickChoose() {
        notInTurn(true);
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Resources> res = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            for (int i = 7 + j; i < 19; i = i + 4) {
                if ((Integer) ((Spinner) productionPane.getChildren().get(i)).getValue() != 0) {
                    production.add((Integer) ((Spinner) productionPane.getChildren().get(i)).getValue());
                    for (int c = 0; c < (Integer) ((Spinner) productionPane.getChildren().get(i)).getValue(); c++) {
                        res.add(Resources.transform(j));
                        pos.add((int) Math.floor((i - 7) / 4));
                    }
                }
            }
        }
        askPayment(false);
        switch (production.get(1)) {
            case 0:
                    askWhereToPutCard();
                break;
            case 1:
                new Thread(() -> notifyObserver(obs -> obs.useNormalProduction(production.get(0), pos, activePlayerBoard.getPlayerBoard().getProductionCost(production.get(0))))).start();
                break;
            case 10:
                new Thread(() -> notifyObserver(obs -> obs.useBaseProduction(pos, res, resourcesToSend.get(0)))).start();
                break;
            case 11:
                new Thread(() -> notifyObserver(obs -> obs.useSpecialProduction(production.get(0), pos.get(0), resourcesToSend.get(0), res.get(0)))).start();
                break;
            default:
                break;
        }
    }

    /**
     * Asks to the player where he wants to put the card he's buying.
     */
    private void askWhereToPutCard() {
        boolean flag = false;
        for (SpaceProd spaceProd : activePlayerBoard.getPlayerBoard().getProductionSpaces())
            if (spaceProd.getTop().getLevel() == production.get(0) - 1)
                flag = true;
        if (flag) {
            temporaryCard.setDisable(false);
            temporaryCard.setVisible(true);
            notInTurn(true);
            temporaryCard.setImage(new Image(deck.getDeck(colors, production.get(0)).getFirstCard().getImagePath()));
            changeActivePane(playerBoard);
            view = false;
        } else
            Gui.getInstance().showCommunication("You don't have a card that meets the requirement", CommunicationMessage.ILLEGAL_ACTION);
    }

    /**
     * Disables the confirm button on the production Box if the resources chosen are not enough or they are too much.
     */
    private void onClickCheckConfirm() {
        int cont = 0, value;
        boolean bool = false;
        int[] c = {0, 0, 0, 0};
        switch (production.get(1)) {
            case 0:
                int[] a = deck.getDeck(colors, production.get(0)).getFirstCard().getCostCard();
                for (int i = 0; i < 4; i++) {
                    a[i] = (a[i] - activePlayerBoard.getPlayerBoard().getResourceDiscount(Resources.transform(i)));
                    if (a[i] > 0) {
                        bool = isBool(bool, c, a, i);
                    }
                }
                productionConfirm.setDisable(bool);
                break;
            case 1:
                int[] b = activePlayerBoard.getPlayerBoard().getProductionCost(production.get(0));
                for (int i = 0; i < 4; i++) {
                    bool = isBool(bool, c, b, i);
                }
                productionConfirm.setDisable(bool);
                break;
            case 10:
                for (int j = 7; j < 19; j++) {
                    value = (int) ((Spinner) productionPane.getChildren().get(j)).getValue();
                    cont = cont + value;
                }
                if (cont != 2) {
                    bool = true;
                }
                productionConfirm.setDisable(bool);
                break;
            case 11:
                for (int j = 7; j < 19; j++) {
                    value = (int) ((Spinner) productionPane.getChildren().get(j)).getValue();
                    cont = cont + value;
                }
                if (cont != 1) {
                    bool = true;
                }
                productionConfirm.setDisable(bool);
                break;
            default:
                productionConfirm.setDisable(true);
        }
    }

    private boolean isBool(boolean bool, int[] c, int[] a, int i) {
        int value;
        for (int j = 7 + i; j < 19; j = j + 4) {
            value = (int) ((Spinner) productionPane.getChildren().get(j)).getValue();
            c[i] = c[i] + value;
        }
        if (c[i] != a[i]) {
            bool = true;
        }
        return bool;
    }

    /**
     * Updates the playerBoard of the player playing on this computer.
     */
    //playerBoard method
    public void updatePlayerBoard(CompressedPlayerBoard playerBoard) {
        ImageView imageViews;
        //views other player boards method
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
                if (Resources.WHITE.equals(resources))
                    imageViews.setImage(new Image(Resources.WHITE.getBallImagePath()));
                else
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
                } else {
                    leader[i].setImage(new Image(card.getImagePath()));
                    leader[i].setDisable(false);
                }
            } catch (IndexOutOfBoundsException e) {
                leader[i].setDisable(true);
                leader[i].setVisible(false);
            }
        }
    }

    /**
     * Updates the playerBoards of the other players.
     */
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

    /**
     * Updates the special warehouses of the player board.
     */
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

    /**
     * When a resource it's dropped on a warehouse position,sends a sorting market or warehouse message.
     *
     * @param index Index of teh resource.
     * @param row   Row of the warehouse.
     */
    //warehouse
    private void onDropOnWarehouse(int index, int row) {
        if (!movingWarehouse) {
            new Thread(() -> notifyObserver(obs -> obs.sortingMarket(activePlayerBoard.getTemporaryResourceStorage().get(index), row, index))).start();
        } else {
            new Thread(() -> notifyObserver(obs -> obs.moveBetweenWarehouses(resourcesToSend.get(0), index, row))).start();
        }
        choice.clear();
    }

    /**
     * Actives the possibility to move a resource between warehouses.
     *
     * @param active true for activating.
     */
    public void activeMovingWarehouse(boolean active) {
        int i;
        ImageView[] warehouses = createImageViewsOfWarehouses();

        for (i = 0; i < 6; i++)
            if (!activePlayerBoard.getPlayerBoard().getResourceWarehouse(i).equals(Resources.WHITE))
                warehouses[i].setDisable(!active);
            else warehouses[i].setDisable(true);

        //special warehouse
        for (i = 0; i < 2; i++)
            try {
                if (activePlayerBoard.getPlayerBoard().getLeaderCard(i).getAdvantage().equals(Advantages.WAREHOUSE)) {
                    int res = 0;
                    for (int j = 0; j < 4; j++)
                        if (activePlayerBoard.getPlayerBoard().getLeaderCard(i).getEffect().get(j) != 0)
                            res = j;
                    for (int j = 0; j < 2; j++) {
                        if (j < activePlayerBoard.getPlayerBoard().getExtraResources().get(res)) {
                            warehouses[6 + i * 2 + j].setDisable(!active);
                        } else
                            warehouses[6 + i * 2 + j].setDisable(true);
                    }
                }
            } catch (IndexOutOfBoundsException ignored) {

            }


    }

    /**
     * Disables the possibility to move a resource in the warehouses.
     *
     * @param active true for disabling.
     */
    public void notActiveWarehouse(boolean active) {
        firstRow.setDisable(active);
        secondRow.setDisable(active);
        thirdRow.setDisable(active);
        if (!movingWarehouse) {
            notActiveBin(active);
        } else {
            notActiveBin(true);
        }
        HBox[] spec = new HBox[]{specialWarehouse1, specialWarehouse2};
        for (int i = 0; i < 2; i++)
            try {
                if (activePlayerBoard.getPlayerBoard().getLeaderCard(i).getUncovered() && activePlayerBoard.getPlayerBoard().getLeaderCard(i).getAdvantage().equals(Advantages.WAREHOUSE))
                    spec[i].setDisable(active);
            } catch (IndexOutOfBoundsException ignored) {

            }
    }

    /**
     * Disables the bin.
     *
     * @param active true for disabling.
     */
    private void notActiveBin(boolean active) {
        bin.setDisable(active);
        bin.setVisible(!active);
    }

    /**
     * Switch the warehouse rows.
     */
    private void onClickShiftRows(int row1, int row2) {
        new Thread(() -> notifyObserver(obs -> obs.switchRows(row1, row2))).start();

    }

    /**
     * When a card it's dropped on a production space, sends a request to buy this card and position it,where the card was dropped.
     *
     * @param index Index of the space of production.
     */
    private void onDropOnProduction(int index) {
        ArrayList<Integer> pos = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            for (int i = 7 + j; i < 19; i = i + 4) {
                if ((Integer) ((Spinner) productionPane.getChildren().get(i)).getValue() != 0) {
                    production.add((Integer) ((Spinner) productionPane.getChildren().get(i)).getValue());
                    for (int c = 0; c < (Integer) ((Spinner) productionPane.getChildren().get(i)).getValue(); c++) {
                        pos.add((int) Math.floor((i - 7) / 4));
                    }
                }
            }
        }

        int[] discountedCosts = new int[4];
        // Consider discounted costs for the card. Otherwise IndexOutOfBound...
        for (int i = 0; i < 4; i++) {
            discountedCosts[i] = (deck.getDeck(colors, production.get(0)).getFirstCard().getCostCard())[i] - activePlayerBoard.getPlayerBoard().getResourceDiscount(Resources.transform(i));
            if (discountedCosts[i] < 0) discountedCosts[i] = 0;
        }

        new Thread(() -> notifyObserver(obs -> obs.getProduction(colors.ordinal(), production.get(0), pos, index, discountedCosts))).start();
        activeSpaceProds(false);

        temporaryCard.setDisable(true);
        temporaryCard.setVisible(false);
    }

    /**
     * Actives the possibility to drag a card on the production spaces.
     *
     * @param active true for activating.
     */
    private void activeSpaceProds(boolean active) {
        spaceProd2.setDisable(!active);
        spaceProd1.setDisable(!active);
        spaceProd0.setDisable(!active);
    }

    /**
     * Initializes the production spaces.
     */
    private void initializeProductionDrag() {
        Node[] spaceProds = new Node[]{spaceProd0, spaceProd1, spaceProd2};

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            spaceProds[i].setOnDragEntered(event -> {
                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != firstRow &&
                        event.getDragboard().hasImage()) {
                    spaceProds[finalI].setStyle(effect);
                }
                event.consume();
            });
            removeEffectWhenExited(spaceProds, i, finalI);

            spaceProds[i].setOnDragDropped(event -> {
                /* data dropped */
                /* if there is a string data on dragBoard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    onDropOnProduction(finalI);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }

    }

    /**
     * Removes the effect when the drag is exited.
     */
    private void removeEffectWhenExited(Node[] spaceProds, int i, int finalI) {
        spaceProds[i].setOnDragExited(event -> {
            /* mouse moved away, remove the graphical cues */
            spaceProds[finalI].setStyle("");
            event.consume();
        });

        spaceProds[i].setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != spaceProds[finalI] &&
                    event.getDragboard().hasImage()) {
                /* allow for moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    /**
     * Initializes the warehouses.
     */
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
            removeEffectWhenExited(warehouseRows, i, finalI);
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

        ImageView[] images = createImageViewsOfWarehouses();

        //dragController = new DragController(imageView, true, true);


        for (int i = 0; i < 10; i++) {
            int finalI = i;
            int n = 0;
            int k = 0;
            if (i <= 0) n = 1;
            else if (i <= 2) {
                k = 1;
                n = 2;
            } else if (i <= 5) {
                n = 3;
                k = 3;
            } else if (i >= 8)
                k = 1;
            int finalN = n;
            int finalK = k;
            images[i].setOnDragDetected(event -> {
                /* drag was detected, start drag-and-drop gesture*/
                /* allow any transfer mode */
                Dragboard db = images[finalI].startDragAndDrop(TransferMode.ANY);
                /* put the image on dragBoard */
                ClipboardContent content = new ClipboardContent();
                choice.add(0, finalN);
                if (finalI < 6)
                    resourcesToSend.add(0, activePlayerBoard.getPlayerBoard().getResourceWarehouse(finalK));
                else {
                    for (int j = 0; j < 4; j++)
                        if (activePlayerBoard.getPlayerBoard().getLeaderCard(finalK).getEffect().get(j) != 0) {
                            resourcesToSend.add(0, Resources.transform(j));
                            break;
                        }
                }
                movingWarehouse = true;
                notActiveWarehouse(false);
                content.putImage(images[finalI].getImage());
                db.setContent(content);
                event.consume();
            });
        }
    }

    /**
     * Creates an array of image views containing all of the warehouses.
     */
    private ImageView[] createImageViewsOfWarehouses() {
        return new ImageView[]{(ImageView) firstRow.getChildren().get(0),
                (ImageView) secondRow.getChildren().get(0), (ImageView) secondRow.getChildren().get(1),
                (ImageView) thirdRow.getChildren().get(0), (ImageView) thirdRow.getChildren().get(1), (ImageView) thirdRow.getChildren().get(2),
                (ImageView) specialWarehouse1.getChildren().get(0), (ImageView) specialWarehouse1.getChildren().get(1),
                (ImageView) specialWarehouse2.getChildren().get(0), (ImageView) specialWarehouse2.getChildren().get(1)};
    }

    /**
     * Checks if the resource is white.
     * If the there are 2 changes, asks which resource the player wants.
     *
     * @param index Index of the resource selected.
     */
    //on temporary resource white selection
    private void onResourceWhite(int index) {

        if (activePlayerBoard.getTemporaryResourceStorage().get(index).equals(Resources.WHITE)) {
            notInTurn(true);
            chooseResource.setDisable(false);
            choice.add(0, -2);
            choice.add(1, index);
            notInTurn(true);
            chooseResource.setVisible(true);
            Button[] buttons = new Button[]{servantButton, coinButton, stoneButton, shieldButton};
            for (int i = 0; i < 4; i++)
                buttons[i].setDisable(!activePlayerBoard.getPlayerBoard().isResourceSubstitutable(Resources.transform(i)));

        }
    }

    /**
     * Sends the server an end turn request.
     */
    //end turn
    private void onEndTurn() {
        new Thread(() -> notifyObserver(ViewObserver::endTurn)).start();
        endTurn.setDisable(true);
        endTurn.setVisible(false);
        clearChoices();
    }


    /**
     * Actives the end turn button.
     *
     * @param active True for activating.
     */
    public void activeEndTurn(boolean active) {
        endTurn.setVisible(active);
        endTurn.setDisable(!active);
    }

    /**
     * Asks the player to discard or active the leader card selected.
     *
     * @param index Index of the leader card.
     */
    //leader card method
    private void onClickLeaderInactive(int index) {
        choice.add(0, index);
        notInTurn(true);
        discard_activate_Leader.setDisable(false);
        discard_activate_Leader.setVisible(true);

    }

    /**
     * Updates the faith path for the player number playerNumber.
     *
     * @param faithPath    FaithPath to show.
     * @param playerNumber Player's number.
     */
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

    /**
     * Updates the faith cards for the player number playerNumber.
     *
     * @param playerNumber Player's number.
     * @param faithPath    FaithPath to show.
     * @param faith1       First faith card.
     * @param faith2       Second faith card.
     * @param faith3       Third faith card.
     */
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

    /**
     * Moves the faith cross along the faith path.
     *
     * @param faithMarker Marker to move.
     * @param position    Position of the marker.
     */
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

    /**
     * Select a resource.
     * Asks for payment if the player is doing a special or base production action.
     * Set the Resource from white to a new resource in the temporary storage.
     *
     * @param resources Resource selected.
     */
    //choose resource methods
    private void onResourceSelection(Resources resources) {
        resourcesToSend.add(resources);
        askResource(false);

        Button[] buttons = new Button[]{servantButton, coinButton, stoneButton, shieldButton};
        for (int i = 0; i < 4; i++)
            buttons[i].setDisable(false);

        if (production.size() > 0) {
            if (production.get(1) == 11) {
                // Special production, ask payment.
                askPayment(true);
            } else if (production.get(0) > 9) {
                askPayment(true);
            }
        } else {
            if (choice.size() > 0 && choice.get(0) < 0) {
                ((ImageView) resourcesToSort.getChildren().get(choice.get(1))).setImage(new Image(resourcesToSend.get(0).getImagePath()));
                activePlayerBoard.setTemporaryResource(choice.get(1), resourcesToSend.get(0));
            }
        }

    }

    /**
     * Show the chooseResource pane.
     *
     * @param visible True if the pane is visible and playable.
     */
    public void askResource(boolean visible) {
        chooseResource.setVisible(visible);
        chooseResource.setDisable(!visible);
        notInTurn(true);
    }

    /**
     * Show the askForPayment pane.
     *
     * @param playable True if the pane is visible and playable.
     */
    public void askPayment(boolean playable) {
        productionBox.setVisible(playable);
        productionBox.setDisable(!playable);
        productionConfirm.setDisable(true);
        notInTurn(true);
    }

    /**
     * Gets the resources to send.
     */
    public ArrayList<Resources> getResourcesToSend() {
        return resourcesToSend;
    }

    /**
     * Sets the chooseResource text.
     */
    public void setChooseResourceText(String s) {
        resourceText.setText(s);
    }

    /**
     * Exchange between the player board and the board, if flag is false.
     * Exchange between the First action and the board, if flag is true.
     *
     * @param flag False if we already did the first action.
     */
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

    /**
     * Show the Board.
     */
    public void showBoard() {
        changeActivePane(Board);
        flag = false;
        view = true;

    }

    /**
     * Sets the game to single player.
     */
    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
        if (!singlePlayer) {
            viewBoards.setVisible(true);
            viewBoards.setDisable(false);
        }
    }

    /**
     * Clears all the choices made by the player.
     */
    public void clearChoices() {
        resourcesToSend.clear();
        choice.clear();
        movingWarehouse = true;
        production.clear();
    }

    /**
     * Change the active pane to the Node node.
     *
     * @param node New Active pane.
     */
    public void changeActivePane(Node node) {
        activePanel.setVisible(false);
        activePanel.setDisable(true);
        activePanel = node;
        node.setVisible(true);
        node.setDisable(false);
    }

    /**
     * Active the bot Actions.
     *
     * @param active True if we are in single player.
     */
    public void activeBotActions(boolean active) {
        botActions.setVisible(active);
    }

    /**
     * Sets the bot Action token.
     *
     * @param image Image to set.
     */
    public void setBotActions(Image image) {
        botActions.setImage(image);
    }

    /**
     * Active the market.
     *
     * @param active True for activating the market.
     */
    public void activeMarket(boolean active) {
        pushColumn0Button.setDisable(!active);
        pushColumn1Button.setDisable(!active);
        pushColumn2Button.setDisable(!active);
        pushColumn3Button.setDisable(!active);
        pushRow0Button.setDisable(!active);
        pushRow1Button.setDisable(!active);
        pushRow2Button.setDisable(!active);
    }

    /**
     * Active the decks.
     *
     * @param active True for activating the decks.
     */
    public void activeDecks(boolean active) {
        decks.setDisable(!active);
    }

    /**
     * Active the productions.
     *
     * @param active True for activating the productions.
     */
    public void activeProductions(boolean active) {
        StackPane[] spaces = new StackPane[]{spaceProd0, spaceProd1, spaceProd2};
        for (int i = 0; i < 3; i++) {
            if (i < activePlayerBoard.getPlayerBoard().getProductionSpaces().size()) {
                if (activePlayerBoard.getPlayerBoard().getProductionSpaces().get(i).getNumbCard() > 0 && activePlayerBoard.getPlayerBoard().checkResources(activePlayerBoard.getPlayerBoard().getProductionCost(i))) {
                    spaces[i].setDisable(!active);
                } else spaces[i].setDisable(true);
            } else spaces[i].setDisable(true);
        }
        ImageView[] leader = new ImageView[]{active1, active2};
        for (int i = 0; i < 2; i++) {
            try {
                int[] a = {0, 0, 0, 0};
                for (int j = 0; j < 4; j++) {
                    a[j] = activePlayerBoard.getPlayerBoard().getLeaderCard(i).getEffect().get(i);
                }
                if (activePlayerBoard.getPlayerBoard().getLeaderCard(i).getUncovered() && activePlayerBoard.getPlayerBoard().getLeaderCard(i).getAdvantage().equals(Advantages.PROD) && activePlayerBoard.getPlayerBoard().checkResources(a)) {
                    leader[i].setDisable(!active);
                }
                else{ leader[i].setDisable(true); }
            } catch (IndexOutOfBoundsException ignored) {
                leader[i].setDisable(true);
            }
        }
        if (activePlayerBoard.getPlayerBoard().getNumberResources() > 1) {
            baseProduction.setDisable(!active);
        } else {
            baseProduction.setDisable(true);
        }
    }

    /**
     * Disables everything if the player is not in turn.
     *
     * @param active True to disable everything.
     */
    public void notInTurn(boolean active) {
        notInTurn = active;
        movingWarehouse = true;
        decks.setDisable(active);
        leaderCard1.setDisable(active);
        leaderCard2.setDisable(active);
        active1.setDisable(active);
        active2.setDisable(active);
        activeMarket(!active);
        activeProductions(!active);
        activeMovingWarehouse(!active);
        notActiveWarehouse(active);
        //sort warehouse
        shiftRow12.setDisable(active);
        shiftRow23.setDisable(active);
        shiftRow13.setDisable(active);
    }

    /**
     * Show communications.
     *
     * @param visible       True if ti's visible.
     * @param communication Communication to sets.
     */
    //communication
    public void showCommunication(String communication, boolean visible) {
        ((Text) this.communication.getChildren().get(0)).setText(communication);
        this.communication.setVisible(visible);
    }


}
