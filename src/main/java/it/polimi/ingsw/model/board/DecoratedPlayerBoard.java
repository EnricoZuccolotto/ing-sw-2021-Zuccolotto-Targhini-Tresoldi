package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public abstract class DecoratedPlayerBoard implements PlayerBoard {
    protected PlayerBoard subBoard;

    public DecoratedPlayerBoard(PlayerBoard subBoard){
        this.subBoard = subBoard;
    }
    @Override
    public int getLeaderCardsNumber(){return subBoard.getLeaderCardsNumber();}
    @Override
    public boolean getInkwell() {
        return subBoard.getInkwell();
    }

    @Override
    public void addLeaderCard(LeaderCard leaderCard) {
        subBoard.addLeaderCard(leaderCard);
    }

    @Override
    public void removeLeaderCard(LeaderCard c){ subBoard.removeLeaderCard(c); }

    @Override
    public int getVictoryPoints() {
        return subBoard.getVictoryPoints();
    }
    @Override
    public boolean addWarehouseResource(Resources r, int row){return subBoard.addWarehouseResource(r,row);}

    @Override
    public boolean shiftWarehouseRows(int startingRow, int newRowPosition) {
        return subBoard.shiftWarehouseRows(startingRow, newRowPosition);
    }

    @Override
    public boolean addProductionCard(DevelopmentCard c){return subBoard.addProductionCard(c);}

    @Override
    public void addWarehouseSpace(Resources resource, int maxQuantity) {
        subBoard.addWarehouseSpace(resource, maxQuantity);
    }

    @Override
    public boolean addExtraResources(Resources resources, int quantity) {
        return subBoard.addExtraResources(resources, quantity);
    }

    @Override
    public boolean takeExtraResources(Resources resource, int quantity) {
        return subBoard.takeExtraResources(resource, quantity);
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        return subBoard.getExtraResources();
    }

    @Override
    public boolean isResourceDiscounted(Resources resource) {
        return subBoard.isResourceDiscounted(resource);
    }

    @Override
    public int getResourceDiscount(Resources resource) {
        return subBoard.getResourceDiscount(resource);
    }

    @Override
    public void addDiscount(Resources resource, int amount) {
        subBoard.addDiscount(resource, amount);
    }

    @Override
    public void addSubstitute(Resources resource){
        subBoard.addSubstitute(resource);
    }

    @Override
    public ArrayList<Boolean> getSubstitutes(){
        return subBoard.getSubstitutes();
    }

    @Override
    public boolean isResourceSubstitutable(Resources resource){
        return subBoard.isResourceSubstitutable(resource);
    }

    @Override
    public void addProduction(Resources resource) {
        subBoard.addProduction(resource);
    }

    @Override
    public ArrayList<Resources> getProductions(Resources resource) {
        return subBoard.getProductions(resource);
    }
}
