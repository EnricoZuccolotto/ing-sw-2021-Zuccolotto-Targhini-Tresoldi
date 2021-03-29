package it.polimi.ingsw.model.board;

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
}
