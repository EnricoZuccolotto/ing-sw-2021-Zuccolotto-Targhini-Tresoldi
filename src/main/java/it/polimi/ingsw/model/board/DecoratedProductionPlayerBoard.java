package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.model.enums.Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents the Extra Production leader card decoration for a Player Board.
 */
public class DecoratedProductionPlayerBoard extends DecoratedPlayerBoard implements Serializable {
    private final ArrayList<Boolean> productionInputs;

    /**
     * Default constructor
     * @param subBoard The lower board in the chain of decorators.
     */
    public DecoratedProductionPlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        productionInputs = new ArrayList<>(4);
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
    public int getProductionNumber(){
        int cont=0;
        for(int i=0;i<4;i++)
            if(productionInputs.get(i))
                cont+=1;
        return cont+subBoard.getProductionNumber();}

    @Override
    public ArrayList<Resources> getProductions(Resources resource) {
        try{
            if(productionInputs.get(resource.ordinal())){
                return new ArrayList<>(Arrays.asList(Resources.WHATEVER, Resources.FAITH));
            } else {
                return null;
            }
        } catch(IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }
}
