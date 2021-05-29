package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;

import java.util.ArrayList;

public abstract class DecoratedPlayerBoard implements PlayerBoard {
    protected PlayerBoard subBoard;

    public DecoratedPlayerBoard(PlayerBoard subBoard){
        this.subBoard = subBoard;
    }
    @Override
    public int getLeaderCardsNumber(){return subBoard.getLeaderCardsNumber();}
    @Override
    public int getNumberResources(){return subBoard.getNumberResources();}
    @Override
    public int getProductionNumber(){return subBoard.getProductionNumber();}
    @Override
    public boolean getInkwell() {
        return subBoard.getInkwell();
    }
    @Override
    public void setVP(int VP){subBoard.setVP(VP);}
    @Override
    public boolean checkColors(int [] colors){return subBoard.checkColors(colors);}
    @Override
    public boolean checkResources(int [] resources){return subBoard.checkResources(resources);}
    @Override
    public void addLeaderCard(LeaderCard leaderCard) {
        subBoard.addLeaderCard(leaderCard);
    }
    @Override
    public boolean checkResourcesStrongbox(int [] r){return subBoard.checkResourcesStrongbox(r);}
    @Override
    public boolean checkResourcesWarehouse(int [] r){return subBoard.checkResourcesWarehouse(r);}
    @Override
    public boolean payResourcesStrongbox(int [] r){return subBoard.payResourcesStrongbox(r);}

    @Override
    public boolean payResourcesWarehouse(int[] r) {
        return subBoard.payResourcesWarehouse(r);
    }

    @Override
    public void removeLeaderCard(LeaderCard c) {
        subBoard.removeLeaderCard(c);
    }

    @Override
    public LeaderCard getLeaderCard(int index) {
        return subBoard.getLeaderCard(index);
    }

    @Override
    public int[] getProductionCost(int index) {
        return subBoard.getProductionCost(index);
    }

    @Override
    public boolean checkColorsAndLevel(int[] colors, int level) {
        return subBoard.checkColorsAndLevel(colors, level);
    }

    @Override
    public int[] getProductionResult(int index) {
        return subBoard.getProductionResult(index);
    }

    @Override
    public int getVictoryPointsCards() {
        return subBoard.getVictoryPointsCards();
    }

    @Override
    public boolean addWarehouseResource(Resources r, WarehousePositions row) {
        return subBoard.addWarehouseResource(r, row);
    }

    @Override
    public boolean checkResourcesSpecialWarehouse(int[] r) {
        return subBoard.checkResourcesSpecialWarehouse(r);
    }

    @Override
    public boolean payResourcesSpecialWarehouse(int[] r) {
        return subBoard.payResourcesSpecialWarehouse(r);
    }

    @Override
    public void addStrongboxResource(Resources r, int quantities) {
        subBoard.addStrongboxResource(r, quantities);
    }

    @Override
    public boolean shiftWarehouseRows(WarehousePositions startingRow, WarehousePositions newRowPosition) {
        return subBoard.shiftWarehouseRows(startingRow, newRowPosition);
    }

    @Override
    public boolean addProductionCard(DevelopmentCard c, int index) {
        return subBoard.addProductionCard(c, index);
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
    public ArrayList<Resources> getSubstitutableResources(){return subBoard.getSubstitutableResources();}

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

    @Override
    public String toString(boolean mine) {
        return subBoard.toString(mine);
    }
}
