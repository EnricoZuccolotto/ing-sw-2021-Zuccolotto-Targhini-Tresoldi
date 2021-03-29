package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public abstract class DecoratedPlayerBoard implements PlayerBoard {
    protected PlayerBoard subBoard;

    public DecoratedPlayerBoard(PlayerBoard subBoard){
        this.subBoard = subBoard;
    }

    @Override
    public boolean getInkwell() {
        return subBoard.getInkwell();
    }

    @Override
    public void addLeaderCard(LeaderCard leaderCard) {
        subBoard.addLeaderCard(leaderCard);
    }

    @Override
    public int getVictoryPoints() {
        return subBoard.getVictoryPoints();
    }

    @Override
    public void addWarehouseSpace(Resources resource) {
        subBoard.addWarehouseSpace(resource);
    }

    @Override
    public void addExtraResources(Resources resources, int quantity) {
        subBoard.addExtraResources(resources, quantity);
    }

    @Override
    public void takeExtraResources(Resources resource, int quantity) {
        subBoard.takeExtraResources(resource, quantity);
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        return subBoard.getExtraResources();
    }
}
