package it.polimi.ingsw.model.board;

import com.sun.org.apache.xpath.internal.operations.Bool;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public interface PlayerBoard {

    // General methods
    public boolean getInkwell();
    public void addLeaderCard(LeaderCard leaderCard);
    public int getVictoryPoints();

    // Decorated warehouse methods
    public void addWarehouseSpace(Resources resource);
    public void addExtraResources(Resources resources, int quantity);
    public void takeExtraResources(Resources resource, int quantity);
    public ArrayList<Integer> getExtraResources();

    // Decorated cost methods
    public void addDiscount(Resources resource, int amount);
    public boolean isResourceDiscounted(Resources resource);
    public int getResourceDiscount(Resources resource);

    // Decorated white marble
    public void addSubstitute(Resources resource);
    public ArrayList<Boolean> getSubstitutes();
    public boolean isResourceSubstitutable(Resources resource);

    // Decorated production
    public void addProduction(Resources resource);
    public ArrayList<Resources> getProductions(Resources resource);
}
