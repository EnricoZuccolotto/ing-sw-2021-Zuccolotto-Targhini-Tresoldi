package it.polimi.ingsw.test.modelTest;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.tools.CardParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * This class tests the Leader cards.
 */
public class LeaderCardTest {
    /**
     * Generates LeaderCards and checks if all effects/advantages are correct.
      */
    @Test
    public void LeaderCards(){
        LeaderCard c ;
        int cont=0;


        ArrayList<Integer> expected=new ArrayList<>();
        for(int i=49;i<65;i++)
            expected.add(i);
        ArrayList<Advantages> expect=new ArrayList<>();
        for(int i=0;i<4;i++)
            Collections.addAll(expect, Advantages.values());

        ArrayList<LeaderCard> deck= CardParser.parseLeadCards();
                assertNotNull(deck);
                assertEquals(16, deck.size());
                for (int i = 0; i < 16; i++) {
                    cont++;
                    c = deck.get(i);
                    assertNotNull(c);
                    assertEquals(c.getCostColor().length,4);
                    assertEquals(c.getCostResources().length,4);
                    expect.remove(c.getAdvantage());
                    assertEquals(c.getEffect().size(), 4);
                    expected.remove((Integer) c.getID());
                }


        assertTrue(expect.isEmpty());
        assertTrue(expected.isEmpty());
        assertEquals(4*4,cont);
    }

}
