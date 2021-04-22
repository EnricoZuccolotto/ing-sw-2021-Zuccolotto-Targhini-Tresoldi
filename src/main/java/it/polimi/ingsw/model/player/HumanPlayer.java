package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.network.messages.HumanPlayerUpdateMessage;
import it.polimi.ingsw.network.messages.MarketReplyMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class HumanPlayer extends Player implements Serializable {
    private PlayerBoard playerBoard;
    private ArrayList<Resources> temporaryResourceStorage;

    public HumanPlayer(String name,boolean inkwell){
        super(name);
        this.playerBoard = new SimplePlayerBoard(inkwell);
        temporaryResourceStorage = new ArrayList<>();
    }

    public void doAction() {

    }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public void setPlayerBoard(PlayerBoard playerBoard){
        this.playerBoard = playerBoard;
    }

    public ArrayList<Resources> getTemporaryResourceStorage() {
        return temporaryResourceStorage;
    }

    public void removeItemFromTemporaryList(int index){
        try{
            temporaryResourceStorage.remove(index);
            notifyObserver(new MarketReplyMessage(super.name, temporaryResourceStorage));
        } catch(IndexOutOfBoundsException e){
            // TODO: Handle exception
        }
    }

    public void setTemporaryResourceStorage(ArrayList<Resources> temporaryResourceStorage) {
        this.temporaryResourceStorage = temporaryResourceStorage;
        notifyObserver(new MarketReplyMessage(super.name, temporaryResourceStorage));
    }

    public void sendUpdateToPlayer(){
        notifyObserver(new HumanPlayerUpdateMessage(this));
    }
}
