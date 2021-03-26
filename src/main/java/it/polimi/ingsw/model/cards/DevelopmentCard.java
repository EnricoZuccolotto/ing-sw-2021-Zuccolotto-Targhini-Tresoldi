package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Colors;

public class DevelopmentCard extends Card {
    private int[] costCard;
    private int[] costProduction;
    private int[] productionResult;
    private Colors color;
    private int level;

    DevelopmentCard(int VP){
        super(VP);
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
}
