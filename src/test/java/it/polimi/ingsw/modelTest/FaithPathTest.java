package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.cards.Card;

import org.junit.Test;
import static org.junit.Assert.*;

public class FaithPathTest {
    @Test
        public void FaithTest(){
        FaithPath f= new FaithPath(3);
        f.movePlayer(0, 3);
        f.movePlayer(1,5);
        f.movePlayer(2,8);
        f.movePlayer(0,5);
        assertFalse(f.getCardsState(0,0));
        assertTrue(f.getCardsState(0,1));
        assertTrue(f.getCardsState(0,2));
        f.movePlayer(0,4);
        assertFalse(f.getCardsState(1,0));
        f.movePlayer(1,5);
        f.movePlayer(2, 8);
        assertTrue(f.getCardsState(1,0));
        assertFalse(f.getCardsState(1,1));
        assertTrue(f.getCardsState(1,2));
        f.movePlayer(1, 10);
        f.movePlayer(2, 2);
        f.movePlayer(0, 13);
        assertTrue(f.getCardsState(2,0));
        assertTrue(f.getCardsState(2,1));
        assertFalse(f.getCardsState(2,2));
        assertEquals(27, f.get_PV(0));
        assertEquals(18, f.get_PV(1));
        assertEquals(17, f.get_PV(2));
    }
}
