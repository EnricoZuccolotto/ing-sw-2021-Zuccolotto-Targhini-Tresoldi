package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.Stack;

public class SpaceProd {
    private Stack<DevelopmentCard> spaceProd;

    public SpaceProd(){

    }

    //public int getTotalVP() {}

    public void addCard(DevelopmentCard card){
        spaceProd.push(card);
    }

    public void useCard(){}
}
