package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;

import java.util.ArrayList;

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

    public int[] getEffect1() {
        return effect1;
    }

    public int[] getEffect2() {
        return effect2;
    }

    public void print(){

        System.out.println("ID:"+this.getID());
        System.out.println("VP:"+this.getVP());
        System.out.println("Advantages:"+this.advantage);
        System.out.println("costResources:");
        for (int i=0;i<4;i++ )
            System.out.print(this.costResources[i]);
        System.out.println("\ncostColor:");
        for (int i=0;i<4;i++ )
            System.out.print(this.costColor[i]);
        System.out.println("\neffect1:");
        for (int i=0;i<4;i++ )
            System.out.print(this.effect1[i]);
        if (effect2.length>0) {
            System.out.println("\neffect2:");
            for (int i = 0; i < 4; i++)
                System.out.print(this.effect2[i]);
        }
    }
}
