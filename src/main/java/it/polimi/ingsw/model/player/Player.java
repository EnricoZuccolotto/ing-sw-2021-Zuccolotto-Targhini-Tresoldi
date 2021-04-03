package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;

public abstract class Player {
    private String name;

    public Player(String name) {
        this.name=name;
    }


    abstract public void doAction();
}
