package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LeaderCard extends Card {
    private final int[] costResources;
    private final int[] costColor;
    private final Advantages advantage;
    private final int[] effect1;
    private final int[] effect2;



    public LeaderCard(int VP, int ID,int [] costResources,int[] costColor,Advantages advantage,int[] effect1,int[] effect2){
        super(VP,ID);
        this.costResources=costResources;
        this.costColor=costColor;
        this.advantage=advantage;
        this.effect1=effect1;
        this.effect2=effect2;
    }


    public int[] getCostResources() {
        return costResources;
    }

    public int[] getCostColor() {
        return costColor;
    }

    public Advantages getAdvantage() {
        return advantage;
    }

    public ArrayList<Integer> getEffect1() {
        ArrayList<Integer> result = new ArrayList<Integer>(4);
        for(int effect : effect1){
            result.add(effect);
        }
        return result;
    }

    public ArrayList<Integer> getEffect2() {
        ArrayList<Integer> result = new ArrayList<Integer>(4);
        for(int effect : effect1){
            result.add(effect);
        }
        return result;
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                super.toString()+
                "costResources=" + Arrays.toString(costResources) +
                ", costColor=" + Arrays.toString(costColor) +
                ", advantage=" + advantage +
                ", effect1=" + Arrays.toString(effect1) +
                ", effect2=" + Arrays.toString(effect2) +
                '}';
    }
}
