package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class handles the winner screen.
 */
public class WinnerController extends ViewObservable implements SceneController {
    @FXML
    private Label winnerLabel;
    @FXML
    private Label standingsLabel;
    @FXML
    private Label firstPlaceLabel;
    @FXML
    private Label secondPlaceLabel;
    @FXML
    private Label thirdPlaceLabel;
    @FXML
    private Label fourthPlaceLabel;
    @FXML
    private Button exitButton;

    /**
     * Sets the winner pane.
     */
    @FXML
    public void initialize() {
        Gui gui = Gui.getInstance();
        Map<String, Integer> victoryPoints = gui.getVictoryPoints();
        ArrayList<Label> labels = new ArrayList<>();
        labels.add(firstPlaceLabel);
        labels.add(secondPlaceLabel);
        labels.add(thirdPlaceLabel);
        labels.add(fourthPlaceLabel);

        if (gui.isSinglePlayer()) {
            for (Label label : labels) {
                label.setVisible(false);
            }
            standingsLabel.setVisible(false);
            if(gui.isWinner()){
                winnerLabel.setText("Congratulations, you are the Winner! Your points are: " + gui.getVictoryPointsForCurrentUser());
            } else {
                winnerLabel.setText("The bot has won! You lost. Your points are: " + gui.getVictoryPointsForCurrentUser());
            }
        } else {
            LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
            victoryPoints.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
            int index = 0;
            for(String nickname : sortedMap.keySet()){
                labels.get(index).setText(index + ". " + nickname + " - " + sortedMap.get(nickname) + " points");
            }
            winnerLabel.setVisible(false);
        }

        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> System.exit(0));
    }
}
