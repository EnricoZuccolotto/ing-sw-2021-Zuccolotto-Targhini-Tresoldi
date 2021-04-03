package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;

public class HumanPlayer extends Player {
    private PlayerBoard playerBoard;

    public HumanPlayer(String name,boolean inkwell){
        super(name);
        this.playerBoard=new SimplePlayerBoard(inkwell);

    }
    public void doAction() {

    }
}
