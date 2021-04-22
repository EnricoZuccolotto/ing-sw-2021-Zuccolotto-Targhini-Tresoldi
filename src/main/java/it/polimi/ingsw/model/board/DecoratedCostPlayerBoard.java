package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.model.enums.Resources;

import java.io.Serializable;
import java.util.ArrayList;

public class DecoratedCostPlayerBoard extends DecoratedPlayerBoard implements Serializable {
    private ArrayList<Integer> discounts;

    public DecoratedCostPlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        discounts = new ArrayList<Integer>(4);
        discounts.add(0);
        discounts.add(0);
        discounts.add(0);
        discounts.add(0);
    }

    @Override
    public void addDiscount(Resources resource, int amount) {
        try {
            discounts.set(resource.ordinal(), amount);
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }

    @Override
    public boolean isResourceDiscounted(Resources resource) {
        try{
            return discounts.get(resource.ordinal()) != 0;
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }

    @Override
    public int getResourceDiscount(Resources resource) {
        try {
            return discounts.get(resource.ordinal());
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }
}
