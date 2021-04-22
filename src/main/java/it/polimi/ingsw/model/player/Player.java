package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;
import it.polimi.ingsw.observer.Observable;

public abstract class Player extends Observable {
    protected final String name;

    public Player(String name) {
        this.name=name;
    }


    abstract public void doAction();

    public String getName() {
        return name;
    }

}
