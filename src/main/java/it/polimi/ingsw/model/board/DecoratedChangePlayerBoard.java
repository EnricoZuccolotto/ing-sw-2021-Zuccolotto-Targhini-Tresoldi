package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.model.enums.Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class DecoratedChangePlayerBoard extends DecoratedPlayerBoard implements Serializable {
    private ArrayList<Boolean> substitutes;

    public DecoratedChangePlayerBoard(PlayerBoard subBoard){
        super(subBoard);
        substitutes = new ArrayList<Boolean>(4);
        substitutes.add(false);
        substitutes.add(false);
        substitutes.add(false);
        substitutes.add(false);
    }

    @Override
    public void addSubstitute(Resources resource){
        try{
            substitutes.set(resource.ordinal(), true);
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }

    @Override
    public ArrayList<Boolean> getSubstitutes(){
        return new ArrayList<Boolean>(Arrays.asList(substitutes.get(0), substitutes.get(1), substitutes.get(2), substitutes.get(3)));
    }

    @Override
    public ArrayList<Resources> getSubstitutableResources(){
        ArrayList<Resources> res=new ArrayList<>();
        for(int i=0; i<4; i++){
            if(substitutes.get(i)){
                res.add(Resources.transform(i));
            }
        }
        return res;
    }

    @Override
    public boolean isResourceSubstitutable(Resources resource){
        try {
            return substitutes.get(resource.ordinal());
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalResourceException();
        }
    }
}
