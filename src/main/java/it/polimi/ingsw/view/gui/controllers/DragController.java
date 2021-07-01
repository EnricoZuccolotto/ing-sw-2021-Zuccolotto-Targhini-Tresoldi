package it.polimi.ingsw.view.gui.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * This class controls the drag events for resources and cards.
 */
public class DragController {
    private final Node target;
    private double anchorX;
    private double anchorY;
    private double mouseOffsetFromNodeZeroX;
    private double mouseOffsetFromNodeZeroY;
    private EventHandler<MouseEvent> setAnchor;
    private EventHandler<MouseEvent> updatePositionOnDrag;
    private EventHandler<MouseEvent> commitPositionOnRelease;
    private boolean cycleStatus = false;
    private BooleanProperty isDraggable;

    public DragController(Node target, boolean isDraggable) {
        this.target = target;
        createHandlers();
        createDraggableProperty();
        this.isDraggable.set(isDraggable);
    }

    /**
     * Creates 3 handlers:
     * -setAnchor: it sets the anchor;
     * -updatePositionOnDrag: it moves the window;
     * -commitOnPositionRelease: when the player finish the drag action, it sets the new layout x and y of this window.
     */
    private void createHandlers() {
        setAnchor = event -> {
            if (event.isPrimaryButtonDown()) {
                cycleStatus = true;
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                mouseOffsetFromNodeZeroX = event.getX();
                mouseOffsetFromNodeZeroY = event.getY();
            }
            if (event.isSecondaryButtonDown()) {
                cycleStatus = false;
                target.setTranslateX(0);
                target.setTranslateY(0);
            }
        };
        updatePositionOnDrag = event -> {
            if (cycleStatus) {
                target.setTranslateX(event.getSceneX() - anchorX);
                target.setTranslateY(event.getSceneY() - anchorY);
            }
        };

        commitPositionOnRelease = event -> {
            if (cycleStatus) {
                //commit changes to LayoutX and LayoutY
                target.setLayoutX(event.getSceneX() - mouseOffsetFromNodeZeroX);
                target.setLayoutY(event.getSceneY() - mouseOffsetFromNodeZeroY);
                //clear changes from TranslateX and TranslateY
                target.setTranslateX(0);
                target.setTranslateY(0);
            }
        };
    }

    /**
     * Adds the events handlers to the object.
     */
    public void createDraggableProperty() {
        isDraggable = new SimpleBooleanProperty();
        isDraggable.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                target.addEventFilter(MouseEvent.MOUSE_PRESSED, setAnchor);
                target.addEventFilter(MouseEvent.MOUSE_DRAGGED, updatePositionOnDrag);
                target.addEventFilter(MouseEvent.MOUSE_RELEASED, commitPositionOnRelease);
            } else {
                target.removeEventFilter(MouseEvent.MOUSE_PRESSED, setAnchor);
                target.removeEventFilter(MouseEvent.MOUSE_DRAGGED, updatePositionOnDrag);
                target.removeEventFilter(MouseEvent.MOUSE_RELEASED, commitPositionOnRelease);
            }
        });
    }

}