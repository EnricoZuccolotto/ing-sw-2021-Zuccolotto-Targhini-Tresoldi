package it.polimi.ingsw.test.modelTest;

import it.polimi.ingsw.exceptions.WinnerException;
import it.polimi.ingsw.model.FaithPath;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the Faith Path
 */
public class FaithPathTest {
    /**
     * Checks if the faith symbol correctly moves and stops moving if the game is won.
     */
    @Test
        public void FaithTest(){
        FaithPath f = new FaithPath();
        f.init(3, false);
        f.movePlayer(0, 3);
        f.movePlayer(1, 5);
        f.movePlayer(2, 8);
        f.movePlayer(0, 5);
        //8 5 8
        assertFalse(f.getCardsState(0, 0));
        assertTrue(f.getCardsState(0, 1));
        assertTrue(f.getCardsState(0, 2));
        f.movePlayer(0, 4);
        //12 5 8
        assertFalse(f.getCardsState(1, 0));
        f.movePlayer(1, 5);
        f.movePlayer(2, 8);
        // 12 10 16
        assertTrue(f.getCardsState(1, 0));
        assertFalse(f.getCardsState(1, 1));
        assertTrue(f.getCardsState(1, 2));
        f.movePlayer(1, 10);
        f.movePlayer(2, 2);
        f.movePlayer(0, 12);
        // 24 20 17
        assertTrue(f.getCardsState(2, 0));
        assertTrue(f.getCardsState(2, 1));
        assertFalse(f.getCardsState(2, 2));
        assertEquals(27, f.get_PV(0));
        assertEquals(18, f.get_PV(1));
        assertEquals(17, f.get_PV(2));
        try {
            f.movePlayer(0,2);
            fail();
        }
        catch (WinnerException e) {
            assertTrue(true);
        }
    }

    /**
     * Try to move the player all the way to the end and check if the game finishes.
     */
    @Test
    public void InstaWin() {
        FaithPath f = new FaithPath();
        f.init(4, false);
        try {
            f.movePlayer(0, 25);
            fail();
        } catch (WinnerException e) {
            for (int i = 1; i < 4; i++)
                for (int j = 0; j < 3; j++)
                    assertFalse(f.getCardsState(j, i));

            assertTrue(f.getCardsState(0, 0));
            assertTrue(f.getCardsState(1, 0));
            assertTrue(f.getCardsState(2, 0));
            assertEquals(29, f.get_PV(0));
            assertEquals(0, f.get_PV(1));
            assertEquals(0, f.get_PV(2));
            assertEquals(0, f.get_PV(3));
        }



    }

    /**
     * Test the faith path report cards.
     */
    @Test
    public void MultipleReportSpaces() {
        FaithPath f = new FaithPath();
        f.init(4, false);
        f.movePlayer(0, 5);
        assertFalse(f.getCardsState(0, 0));
        assertFalse(f.getCardsState(0, 1));
        assertFalse(f.getCardsState(0, 2));
        assertFalse(f.getCardsState(0, 3));
        f.movePlayer(1, 16);
        assertTrue(f.getCardsState(0, 0));
        assertTrue(f.getCardsState(0, 1));
        assertFalse(f.getCardsState(0, 2));
        assertFalse(f.getCardsState(0, 3));
        assertTrue(f.getCardsState(1, 1));
        f.movePlayer(2, 24);
        assertTrue(f.getCardsState(0, 0));
        assertTrue(f.getCardsState(0, 1));
        assertTrue(f.getCardsState(2, 2));
        assertFalse(f.getCardsState(0, 3));

        f.movePlayer(3, 8);

        assertTrue(f.getCardsState(0, 0));
        assertTrue(f.getCardsState(0, 1));
        assertFalse(f.getCardsState(0, 2));
        assertFalse(f.getCardsState(0, 3));

        assertFalse(f.getCardsState(1, 0));
        assertTrue(f.getCardsState(1, 1));
        assertFalse(f.getCardsState(1, 2));
        assertFalse(f.getCardsState(1, 3));

        assertFalse(f.getCardsState(2, 0));
        assertFalse(f.getCardsState(2, 1));
        assertTrue(f.getCardsState(2, 2));
        assertFalse(f.getCardsState(2, 0));

        assertEquals(3, f.get_PV(0));
        assertEquals(14, f.get_PV(1));
        assertEquals(24, f.get_PV(2));
        assertEquals(2, f.get_PV(3));

    }
}
