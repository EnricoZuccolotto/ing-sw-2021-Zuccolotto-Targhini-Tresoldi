package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.player.Player;

public class LobbyController {
    private Player inLobbyPlayer;
    private  int Playernumber;

    public LobbyController(){
    }
    public void checkUserData(){
    }
    public boolean isFull(){
        return true;
    }
    public void setGameSize(int size){
        this.Playernumber=size;
    }
    public int getPlayernumber(){
        return Playernumber;
    }

}
