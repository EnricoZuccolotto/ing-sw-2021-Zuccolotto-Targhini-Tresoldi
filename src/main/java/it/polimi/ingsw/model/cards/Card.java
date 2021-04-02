package it.polimi.ingsw.model.cards;

public class Card {
    private  final int  VP;
    private  final int  ID;
    private boolean uncovered;

    public Card(int VP,int ID){
        this.VP = VP;
        this.ID = ID;
        this.uncovered = false;
    }

    public void flipCard() {
        this.uncovered = true;
    }

    public boolean getUncovered() { return uncovered; }
    public int getVP() { return VP; }
    public int getID() { return ID; }

    @Override
    public String toString() {
        return "Card{" +
                "VP=" + VP +
                ", ID=" + ID +
                ", uncovered=" + uncovered +
                '}';
    }
}
