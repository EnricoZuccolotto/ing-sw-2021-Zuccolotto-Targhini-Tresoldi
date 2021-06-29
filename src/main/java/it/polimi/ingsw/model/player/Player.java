package it.polimi.ingsw.model.player;

import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;

public abstract class Player extends Observable implements Serializable {
    protected final String name;

    public Player(String name) {
        this.name=name;
    }


    public String getName() {
        return name;
    }

}
