package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


public class FirstActionController extends ViewObservable implements SceneController {
        private final String effect = " -fx-effect: dropshadow(three-pass-box, rgba(0,200,0,1), 20, 0, 0, 0);";
        private ArrayList<Integer> choice;
        private ArrayList<ImageView> hover;
        @FXML
        private Button Confirm;
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
                gui.setFirstActionController(this);
                choice = new ArrayList<>();
                hover = new ArrayList<>();

                Confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConfirm());

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

        public void update(PlayerBoard playerBoard) {
                Card1.setImage(new Image(playerBoard.getLeaderCard(0).getImagePath()));
                Card2.setImage(new Image(playerBoard.getLeaderCard(1).getImagePath()));
                Card3.setImage(new Image(playerBoard.getLeaderCard(2).getImagePath()));
                Card4.setImage(new Image(playerBoard.getLeaderCard(3).getImagePath()));
        }

}


