package it.polimi.ingsw.modelTest;


import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeckTest {
    @Test
    public void DecksCards(){
    DevelopmentCard c ;
    int cont=0;
    Deck [][]decks;
    Deck deck;
    ArrayList<Integer> expected=new ArrayList<>();
    for(int i=1;i<49;i++)
        expected.add(i);

    decks= CardParser.parseDevCards();

        for (int k = 0; k < 4; k++)
            for (int j = 0; j < 3; j++) {

                assertNotNull(decks);
                deck = decks[k][j];
                assertNotNull(decks[k][j]);
                assertEquals(4, deck.DeckLength());
                for (int i = 0; i < 4; i++) {
                    cont++;
                    c = deck.getFirstCard();
                    assertNotNull(c);
                    assertEquals(c.getColor().ordinal(),k);
                    assertEquals(c.getLevel(),j+1);
                    assertEquals(4, c.getCostCard().length);
                    assertEquals(5, c.getCostProduction().length);
                    assertEquals(6, c.getProductionResult().length);
                    expected.remove((Integer) c.getID());
                    deck.popFirstCard();
                    assertEquals(deck.DeckLength(), 3 - i);
        }

    }
        assertTrue(expected.isEmpty());
        assertEquals(4*3*4,cont);
}

}
