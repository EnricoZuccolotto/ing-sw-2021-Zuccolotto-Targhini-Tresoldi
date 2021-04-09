package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;
import java.util.Arrays;

public class DecoratedProductionPlayerBoard extends DecoratedPlayerBoard {
    private ArrayList<Boolean> productionInputs;

    public DecoratedProductionPlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        productionInputs = new ArrayList<Boolean>(4);
        productionInputs.add(false);
        productionInputs.add(false);
        productionInputs.add(false);
        productionInputs.add(false);
    }

    @Override
    public void addProduction(Resources resource) {
        if(resource.ordinal() < 4){
            productionInputs.set(resource.ordinal(), true);
        } else {
            throw new IllegalResourceException();
        }
    }

    @Override
    public ArrayList<Resources> getProductions(Resources resource) {
        try{
            if(productionInputs.get(resource.ordinal())){
                return new ArrayList<Resources>(Arrays.asList(Resources.WHATEVER, Resources.FAITH));
            } else {
                return null;
            }
        } catch(IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }
}
