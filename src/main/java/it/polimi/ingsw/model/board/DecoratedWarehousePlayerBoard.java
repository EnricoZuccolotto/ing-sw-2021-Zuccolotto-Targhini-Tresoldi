package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;
import java.util.Arrays;

public class DecoratedWarehousePlayerBoard extends DecoratedPlayerBoard {
    private ArrayList<Integer> quantities;
    // This ArrayList contains the maximum number of possible resources
    private ArrayList<Integer> enableSpecialWarehouse;

    public DecoratedWarehousePlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        quantities = new ArrayList<Integer>(4);
        // TODO: Move to an initializer static method
        quantities.add(0);
        quantities.add(0);
        quantities.add(0);
        quantities.add(0);
        enableSpecialWarehouse = new ArrayList<Integer>(4);
        enableSpecialWarehouse.add(0);
        enableSpecialWarehouse.add(0);
        enableSpecialWarehouse.add(0);
        enableSpecialWarehouse.add(0);
    }

    @Override
    public void addWarehouseSpace(Resources resource, int maxQuantity) {
        enableSpecialWarehouse.set(resource.ordinal(), maxQuantity);
    }

    @Override
    public boolean addExtraResources(Resources resource, int quantity) {
        int resourceIndex = resource.ordinal();
        if(enableSpecialWarehouse.get(resourceIndex) != 0){
            int currentAmount = quantities.get(resourceIndex);
            if(currentAmount + quantity <= enableSpecialWarehouse.get(resourceIndex)){
                quantities.set(resourceIndex, currentAmount + quantity);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean takeExtraResources(Resources resource, int quantity) {
        int resourceIndex = resource.ordinal();
        if(enableSpecialWarehouse.get(resourceIndex) != 0){
            int currentAmount = quantities.get(resourceIndex);
            if(currentAmount - quantity >= 0){
                quantities.set(resourceIndex, currentAmount - quantity);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        // Copy in order to avoid exposing the rep
        return new ArrayList<Integer>(Arrays.asList(quantities.get(0), quantities.get(1), quantities.get(2), quantities.get(3)));
    }


}
