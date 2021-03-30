package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Colors;

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
    public void print(){
       System.out.println("level:"+this.level);
        System.out.println("color:"+this.color);
        System.out.println("ID:"+this.getID());
        System.out.println("VP:"+this.getVP());
        System.out.println("CostCard:");
        for (int i=0;i<4;i++ )
        System.out.print(this.costCard[i]);
        System.out.println("\nCostProd:");
        for (int i=0;i<5;i++ )
            System.out.print(this.costProduction[i]);
        System.out.println("\nProdResult:");
        for (int i=0;i<6;i++ )
            System.out.print(this.productionResult[i]);

    }
}
