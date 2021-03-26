package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.LeaderCard;

public abstract class PlayerBoard {
    private int VP;
    private int faithPoints;
    private int[] resourcesToPlace;
    private Strongbox strongbox;
    private boolean inkWell;
    private LeaderCard[] leaderCards;
    private SpaceProd[] spacesOfProduction;
    private Warehouse warehouse;

    public PlayerBoard(){}

    abstract void payResource();
    abstract void putExtraResource();
    abstract void changeResource();
    abstract void getSpecialProduction();
    abstract void useSpecialProduction();

    public boolean getInkwell() { return inkWell; }
    public void setInkwell(boolean inkWell) { this.inkWell = inkWell; }
    public void baseProduction() {}
    public void addLeaderCard(LeaderCard leaderCard) {}
    public void removeLeaderCard(LeaderCard leaderCard) {}
    public void addVP() {}
    public int getVP() { return VP; }


    public int getFaithPoints() {
        return faithPoints;
    }

    public void addFaithPoints(int faithPoints) {

    }
}
