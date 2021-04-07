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
    }

    @Override
    public void addWarehouseSpace(Resources resource, int maxQuantity) {
        enableSpecialWarehouse.set(resource.ordinal(), maxQuantity);
    }

    @Override
    public void addExtraResources(Resources resource, int quantity) {
        int resourceIndex = resource.ordinal();
        if(enableSpecialWarehouse.get(resourceIndex) != 0){
            int currentAmount = quantities.get(resourceIndex);
            if(currentAmount + quantity <= enableSpecialWarehouse.get(resourceIndex)){
                quantities.set(resourceIndex, currentAmount + quantity);
            } else {
                // exception too many resources
            }
        } else {
            // exception you cannot use this space
        }
    }

    @Override
    public void takeExtraResources(Resources resource, int quantity) {
        int resourceIndex = resource.ordinal();
        if(enableSpecialWarehouse.get(resourceIndex) != 0){
            int currentAmount = quantities.get(resourceIndex);
            if(currentAmount - quantity >= 0){
                quantities.set(resourceIndex, currentAmount - quantity);
            } else {
                // exception too few resources
            }
        } else {
            // exception you cannot use this space
        }
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        // Copy in order to avoid exposing the rep
        return new ArrayList<Integer>(Arrays.asList(quantities.get(0), quantities.get(1), quantities.get(2), quantities.get(3)));
    }


}
