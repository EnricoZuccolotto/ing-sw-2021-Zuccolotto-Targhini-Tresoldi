package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.model.enums.Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class DecoratedWarehousePlayerBoard extends DecoratedPlayerBoard implements Serializable {
    private final ArrayList<Integer> quantities;
    // This ArrayList contains the maximum number of possible resources
    private final ArrayList<Integer> enableSpecialWarehouse;

    public DecoratedWarehousePlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        quantities = new ArrayList<>(4);
        // TODO: Move to an initializer static method
        quantities.add(0);
        quantities.add(0);
        quantities.add(0);
        quantities.add(0);
        enableSpecialWarehouse = new ArrayList<>(4);
        enableSpecialWarehouse.add(0);
        enableSpecialWarehouse.add(0);
        enableSpecialWarehouse.add(0);
        enableSpecialWarehouse.add(0);
    }

    @Override
    public void addWarehouseSpace(Resources resource, int maxQuantity) {
        try {
            enableSpecialWarehouse.set(resource.ordinal(), maxQuantity);
        } catch(IndexOutOfBoundsException ex) {
            throw new IllegalResourceException();
        }
    }

    @Override
    public boolean addExtraResources(Resources resource, int quantity) {
        int resourceIndex = resource.ordinal();
        try{
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
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }

    @Override
    public boolean takeExtraResources(Resources resource, int quantity) {
        int resourceIndex = resource.ordinal();
        try {
            if (enableSpecialWarehouse.get(resourceIndex) != 0) {
                int currentAmount = quantities.get(resourceIndex);
                if (currentAmount - quantity >= 0) {
                    quantities.set(resourceIndex, currentAmount - quantity);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        // Copy in order to avoid exposing the rep
        return new ArrayList<>(Arrays.asList(quantities.get(0), quantities.get(1), quantities.get(2), quantities.get(3)));
    }
    @Override
    public boolean payResourcesSpecialWarehouse(int [] r){
        ArrayList<Integer> Er=getExtraResources();
        for(int  i=0;i<4;i++){

            if(r[i]!=0)
            {
                if(r[i]- Er.get(i)<=0)
                    takeExtraResources(Resources.transform(i), r[i]);
                else return false;
            }
        }
        return true;
    }
    @Override
    public boolean checkResourcesSpecialWarehouse(int [] r){
        ArrayList<Integer> Er=getExtraResources();
        for(int  i=0;i<4;i++){
            if(r[i]!=0)
            {
                if( Er.get(i)<r[i])
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkResources(int [] resources){
        int [] tmp=new int[4];
        ArrayList<Integer> Er=getExtraResources();
        for(int  i=0; i<4; i++)
            if(resources[i]!=0) {
                tmp[i]=resources[i]-Er.get(i);
            }
        return subBoard.checkResources(tmp);

    }

    @Override
    public int getNumberResources(){
        return quantities.stream().reduce(0, Integer::sum)+subBoard.getNumberResources();
    }

}
