package it.polimi.ingsw.model.cards;

import java.util.ArrayList;

public class Deck {
    private ArrayList<DevelopmentCard> deck;

    public Deck(){

    }

    public ArrayList<DevelopmentCard> getDeck(){
        return deck;
    }

    public void shuffle(){}
    //public DevelopmentCard popFirstCard() {}
}
