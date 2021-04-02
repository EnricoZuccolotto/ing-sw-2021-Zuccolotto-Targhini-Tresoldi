package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Colors;

import java.util.Arrays;

public class DevelopmentCard extends Card {
    private final int[] costCard;
    private final int[] costProduction;
    private final int[] productionResult;
    private final Colors color;
    private final int level;

   public DevelopmentCard(int VP,int ID,int[] costCard,int[] costProduction,int[] productionResult, Colors color,int level){
        super(VP,ID);
        this.color=color;
        this.costCard=costCard;
        this.level=level;
        this.costProduction=costProduction;
        this. productionResult=productionResult;
    }

    public int[] getCostCard() {
        return costCard;
    }

    public int[] getCostProduction() {
        return costProduction;
    }

    public int[] getProductionResult() {
        return productionResult;
    }

    public Colors getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "DevelopmentCard{" +
                super.toString()+
                "costCard=" + Arrays.toString(costCard) +
                ", costProduction=" + Arrays.toString(costProduction) +
                ", productionResult=" + Arrays.toString(productionResult) +
                ", color=" + color +
                ", level=" + level +
                '}';
    }
}
