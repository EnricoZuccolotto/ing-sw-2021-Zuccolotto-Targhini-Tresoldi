package it.polimi.ingsw.test.modelTest;

import it.polimi.ingsw.model.board.SimplePlayerBoard;

import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests the human player.
 */
public class HumanPlayerTest {
    /**
     * Create a player, inserts into the board and checks nothing fails and everything is correctly added.
     */
    @Test
    public void humanPlayerTest(){
        HumanPlayer h= new HumanPlayer("Giorgio", true );
        assertTrue(h.getPlayerBoard().getInkwell());
        SimplePlayerBoard s= new SimplePlayerBoard(false);
        h.setPlayerBoard(s);
        assertFalse(h.getPlayerBoard().getInkwell());
    }
}
