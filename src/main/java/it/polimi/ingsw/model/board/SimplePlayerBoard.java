package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.SpaceProd;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplePlayerBoard implements PlayerBoard {
    private Strongbox strongbox;
    private boolean inkWell;
    private ArrayList<LeaderCard> leaderCards;
    private ArrayList<SpaceProd> productionSpaces;
    private Warehouse warehouse;

    public SimplePlayerBoard(boolean inkWell){
        this.inkWell = inkWell;
        strongbox = new Strongbox();
        leaderCards = new ArrayList<LeaderCard>(2);
        productionSpaces = new ArrayList<SpaceProd>(3);
        warehouse = new Warehouse();
    }

    @Override
    public boolean getInkwell() {
        return inkWell;
    }

    @Override
    public void addLeaderCard(LeaderCard leaderCard) {
        leaderCards.add(leaderCard);
    }

    @Override
    public int getVictoryPoints() {
        int VP = 0;

        // Get leadercards victory points
        for(LeaderCard card : leaderCards){
            VP += card.getVP();
        }

        // Get production cards victory points
        for(SpaceProd sp : productionSpaces){
            // TODO: getVictoryPoints();
            // VP += sp.getVictoryPoints();
        }

        return VP;
    }

    @Override
    public void addWarehouseSpace(Resources resource) {
        // throw new IllegalDecoratorException()
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        return new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0));
    }

    @Override
    public void addExtraResources(Resources resources, int quantity) {
        // throw new IllegalDecoratorException()
    }

    @Override
    public void takeExtraResources(Resources resource, int quantity) {
        // throw new IllegalDecoratorException()
    }
}
