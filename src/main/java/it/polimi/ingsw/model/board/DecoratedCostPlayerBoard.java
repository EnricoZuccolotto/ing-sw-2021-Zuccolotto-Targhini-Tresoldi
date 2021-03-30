package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public class DecoratedCostPlayerBoard extends DecoratedPlayerBoard {
    private ArrayList<Integer> discounts;

    DecoratedCostPlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        discounts = new ArrayList<Integer>(4);
    }

    @Override
    public void addDiscount(Resources resource, int amount) {
        discounts.set(resource.ordinal(), amount);
    }

    @Override
    public boolean isResourceDiscounted(Resources resource) {
        return discounts.get(resource.ordinal()) != 0;
    }

    @Override
    public int getResourceDiscount(Resources resource) {
        return discounts.get(resource.ordinal());
    }
}
