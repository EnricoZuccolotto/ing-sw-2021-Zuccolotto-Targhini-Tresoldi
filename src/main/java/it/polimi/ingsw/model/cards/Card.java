package it.polimi.ingsw.model.cards;

public class Card {
    private int VP;
    private boolean uncovered;

    public Card(int VP){
        this.VP = VP;
        this.uncovered = false;
    }

    public void flipCard() {
        this.uncovered = true;
    }

    public boolean getUncovered() { return uncovered; }
    public int getVP() { return VP; }
}
