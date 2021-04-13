package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;

import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;
import static org.junit.Assert.*;

public class HumanPlayerTest {
    @Test
    public void HumanPlayerTest(){
        HumanPlayer h= new HumanPlayer("Giorgio", true );
        assertTrue(h.getPlayerBoard().getInkwell());
        SimplePlayerBoard s= new SimplePlayerBoard(false);
        h.setPlayerBoard(s);
        assertFalse(h.getPlayerBoard().getInkwell());
    }
}
