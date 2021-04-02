package it.polimi.ingsw;

import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;

public class DeckTest {

    public static void main(String[] args){
    DevelopmentCard c = null;
    int g;
       Deck [][]decks=null;
    Deck deck=null;
    decks= CardParser.parseDevCards();
        for (int k = 0; k < 4; k++)
            for (int j = 0; j < 3; j++) {
        deck = decks[k][j];
        g=deck.DeckLength();
        assert( deck.DeckLength()==4);
        System.out.println("Dimensione deck: " + g + " color:" + k + " level"+j);
        for (int i = 0; i < g; i++) {
            c = deck.getFirstCard();
            assert(c!=null);
            System.out.println(c);
            deck.popFirstCard();
            assert(deck.DeckLength()==g-1-i);
            System.out.println("\n");
        }

    }
}

}
