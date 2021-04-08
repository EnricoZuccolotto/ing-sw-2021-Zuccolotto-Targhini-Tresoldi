package it.polimi.ingsw;

import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeckTest {
    @Test
    public void shouldAnswerwithTrue(){
    DevelopmentCard c ;
    int g;
       Deck [][]decks;
    Deck deck;
    decks= CardParser.parseDevCards();
        for (int k = 0; k < 4; k++)
            for (int j = 0; j < 3; j++) {
                if(decks!=null){
        deck = decks[k][j];

        g=deck.DeckLength();
                assertEquals(4, deck.DeckLength());
        System.out.println("Dimensione deck: " + g + " color:" + k + " level"+j);
        for (int i = 0; i < g; i++) {
            c = deck.getFirstCard();
            assertNotNull(c);
            System.out.println(c);
            deck.popFirstCard();
            assertEquals(deck.DeckLength(), g - 1 - i);
            System.out.println("\n");
        }}

    }
}

}
