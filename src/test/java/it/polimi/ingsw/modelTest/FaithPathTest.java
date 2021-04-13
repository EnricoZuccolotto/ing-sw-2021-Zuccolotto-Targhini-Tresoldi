package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.FaithPath;


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
        //8 5 8
        assertFalse(f.getCardsState(0,0));
        assertTrue(f.getCardsState(0,1));
        assertTrue(f.getCardsState(0,2));
        f.movePlayer(0,4);
        //12 5 8
        assertFalse(f.getCardsState(1,0));
        f.movePlayer(1,5);
        f.movePlayer(2, 8);
        // 12 10 16
        assertTrue(f.getCardsState(1,0));
        assertFalse(f.getCardsState(1,1));
        assertTrue(f.getCardsState(1,2));
        f.movePlayer(1, 10);
        f.movePlayer(2, 2);
        f.movePlayer(0, 12);
        // 24 20 17
        assertTrue(f.getCardsState(2,0));
        assertTrue(f.getCardsState(2,1));
        assertFalse(f.getCardsState(2,2));
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
    @Test
    public void InstaWin() {
        FaithPath f = new FaithPath(4);
        try {
            f.movePlayer(0, 25);
            fail();
        }
        catch (WinnerException e){
            for(int i=0;i<4;i++)
                for (int j=0;j<3;j++)
                    assertFalse(f.getCardsState(j, i));

            assertEquals(20, f.get_PV(0));
            assertEquals(0, f.get_PV(1));
            assertEquals(0, f.get_PV(2));
            assertEquals(0, f.get_PV(3));

            assertTrue(true);
        }



    }
    @Test
    public void MultipleReportSpaces() {
        FaithPath f = new FaithPath(4);
        f.movePlayer(0, 5);
        assertFalse(f.getCardsState(0,0));
        assertFalse(f.getCardsState(0,1));
        assertFalse(f.getCardsState(0,2));
        assertFalse(f.getCardsState(0,3));
        f.movePlayer(1, 16);
        assertFalse(f.getCardsState(0,0));
        assertFalse(f.getCardsState(0,1));
        assertFalse(f.getCardsState(0,2));
        assertFalse(f.getCardsState(0,3));
        f.movePlayer(2, 24);
        assertFalse(f.getCardsState(0,0));
        assertFalse(f.getCardsState(0,1));
        assertFalse(f.getCardsState(0,2));
        assertFalse(f.getCardsState(0,3));
        f.movePlayer(3, 8);

        assertTrue(f.getCardsState(0,0));
        assertTrue(f.getCardsState(0,1));
        assertTrue(f.getCardsState(0,2));
        assertTrue(f.getCardsState(0,3));

        assertFalse(f.getCardsState(1,0));
        assertTrue(f.getCardsState(1,1));
        assertTrue(f.getCardsState(1,2));
        assertFalse(f.getCardsState(1,3));

        assertFalse(f.getCardsState(2,0));
        assertFalse(f.getCardsState(2,1));
        assertTrue(f.getCardsState(2,2));
        assertFalse(f.getCardsState(2,0));

        assertEquals(3, f.get_PV(0));
        assertEquals(14, f.get_PV(1));
        assertEquals(29, f.get_PV(2));
        assertEquals(4, f.get_PV(3));

    }
}
