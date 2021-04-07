package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;
import java.util.Arrays;

public class DecoratedProductionPlayerBoard extends DecoratedPlayerBoard {
    private ArrayList<Boolean> productionInputs;

    public DecoratedProductionPlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        productionInputs = new ArrayList<Boolean>(4);
    }

    @Override
    public void addProduction(Resources resource) {
        productionInputs.set(resource.ordinal(), true);
    }

    @Override
    public ArrayList<Resources> getProductions(Resources resource) {
        if(productionInputs.get(resource.ordinal())){
            return new ArrayList<Resources>(Arrays.asList(Resources.WHATEVER, Resources.FAITH));
        } else {
            return null;
        }
    }
}
