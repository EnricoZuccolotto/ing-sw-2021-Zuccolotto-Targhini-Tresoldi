package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;

import java.util.ArrayList;

public class LeaderCard extends Card {
    private int[] costResources;
    private int[] costColor;
    private Advantages advantage;
    private int[] effect1;
    private int[] effect2;



    LeaderCard(int VP){
        super(VP);
        costResources = new int[4];
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
}
