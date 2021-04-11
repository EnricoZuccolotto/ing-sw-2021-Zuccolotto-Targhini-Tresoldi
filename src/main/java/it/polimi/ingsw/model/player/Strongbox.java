package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.enums.Resources;

import java.util.Arrays;

public class Strongbox {
    private int[] strongBox;

    public Strongbox() {
        this.strongBox=new int[4];
        for(int i=0;i<4;i++)
            strongBox[i]=0;
    }

    public int getResources(Resources resource) {
        return this.strongBox[resource.ordinal()];
    }
    public void setResources(Resources resource, int number) {
        this.strongBox[resource.ordinal()]=number;
    }
    public void addResources(Resources resource, int number){ this.strongBox[resource.ordinal()]+=number; }
    public void removeResources(Resources resource, int number){ this.strongBox[resource.ordinal()]-=number; }

    @Override
    public String toString() {
        return "Strongbox" +
                Arrays.toString(strongBox)
              ;
    }
    public int getNumResources(){
        return strongBox[0]+strongBox[1]+strongBox[2]+strongBox[3];
    }
}
