package it.polimi.ingsw.model.player;

import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;

/**
 * This class represents a generic Player
 */
public abstract class Player extends Observable implements Serializable {
    protected final String name;

    /**
     * Default constructor
     * @param name Player's name
     */
    public Player(String name) {
        this.name=name;
    }

    /**
     * Returns the player name
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

}
