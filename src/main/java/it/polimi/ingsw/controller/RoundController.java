package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;

public class RoundController {
    private Game gameInstance;
    private Player playerInTurn;
    private TurnState turnstate;
    public void handle_getMarket(){
    }
    public void handle_useProduction(){
    }
    public void handle_getProduction(){
    }
    public void handle_activeLeader(){
    }
    public void handle_foldLeader(){
    }

    public TurnState getTurnstate() {
        return turnstate;
    }
}
