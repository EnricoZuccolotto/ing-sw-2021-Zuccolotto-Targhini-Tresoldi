package it.polimi.ingsw.utils;

import it.polimi.ingsw.controller.GameController;

import java.io.Serializable;

/**
 * This class represents the persistence object that will be serialized and saved to disk to implement the Persistance advanced feature.
 */
public class GameToSave implements Serializable {
    private final GameController gameController;

    /**
     * Default constructor
     * @param gameController The GameController object that will be saved to disk or restored from disk.
     */
    public GameToSave(GameController gameController){
        this.gameController = gameController;
    }

    /**
     * Getter for the gameController inner object
     * @return The saved/restored {@code GameController} instance.
     */
    public GameController getGameController() {
        return gameController;
    }
}
