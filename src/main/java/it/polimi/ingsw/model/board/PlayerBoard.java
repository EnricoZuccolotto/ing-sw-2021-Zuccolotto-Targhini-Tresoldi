package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

public interface PlayerBoard {

    // General methods
    boolean getInkwell();
    void addLeaderCard(LeaderCard leaderCard);
    int getVictoryPoints();
    boolean addWarehouseResource(Resources r, int row);
    boolean addProductionCard(DevelopmentCard c);


    // Decorated warehouse methods
    void addWarehouseSpace(Resources resource, int maxQuantity) throws IllegalDecoratorException;
    boolean addExtraResources(Resources resources, int quantity) throws IllegalDecoratorException;
    boolean takeExtraResources(Resources resource, int quantity) throws IllegalDecoratorException;
    ArrayList<Integer> getExtraResources();

    // Decorated cost methods
    void addDiscount(Resources resource, int amount) throws IllegalDecoratorException;
    boolean isResourceDiscounted(Resources resource);
    int getResourceDiscount(Resources resource);

    // Decorated white marble
    void addSubstitute(Resources resource) throws IllegalDecoratorException;
    ArrayList<Boolean> getSubstitutes();
    boolean isResourceSubstitutable(Resources resource);

    // Decorated production
    void addProduction(Resources resource) throws IllegalDecoratorException;
    ArrayList<Resources> getProductions(Resources resource);
}
