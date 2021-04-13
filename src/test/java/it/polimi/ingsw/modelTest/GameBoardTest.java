package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameBoardTest {
    @Test
    public void GameBoardTest(){
        GameBoard g= new GameBoard();
        HumanPlayer h= new HumanPlayer("Giorgio", true);
        g.addPlayer(h);
        HumanPlayer f= new HumanPlayer("Luca", true);
        g.addPlayer(f);
        assertEquals(2, g.getPlayers().size());
        HumanPlayer k= new HumanPlayer("Gianmarco", true);
        g.addPlayer(k);
        HumanPlayer l= new HumanPlayer("Enri", true);
        g.addPlayer(l);
        assertEquals(4, g.getPlayers().size());
        assertEquals(4,g.getDeck(0,0).DeckLength());
        assertEquals(4,g.getDeck(0,1).DeckLength());
        assertEquals(4,g.getDeck(0,2).DeckLength());
        assertEquals(4,g.getDeck(1,0).DeckLength());
        assertEquals(4,g.getDeck(1,1).DeckLength());
        assertEquals(4,g.getDeck(1,2).DeckLength());
        assertEquals(4,g.getDeck(2,0).DeckLength());
        assertEquals(4,g.getDeck(2,1).DeckLength());
        assertEquals(4,g.getDeck(2,2).DeckLength());
        assertEquals(4,g.getDeck(3,2).DeckLength());
        assertEquals(4,g.getDeck(3,2).DeckLength());
        assertEquals(4,g.getDeck(3,2).DeckLength());
        g.init(g);
        assertEquals(3, g.pushColumnMarket(0).size());
        assertEquals(4, g.pushRowMarket(1).size());
        g.movePlayerFaithPath(0,2);
        g.movePlayerFaithPath(1,5);
        assertEquals(2, g.getPlayerFaithPathPosition(0));
        assertEquals(5, g.getPlayerFaithPathPosition(1));
        assertEquals(0, g.get_PV(0));
        assertEquals(1, g.get_PV(1));
    }
    @Test
    public void GameBoardBotTest(){
        GameBoard g= new GameBoard();
        HumanPlayer l= new HumanPlayer("Valerio", true);
        g.addPlayer(l);
        g.init(g);
        assertNotEquals(null, g.getBot());
        assertEquals(1, g.getPlayers().size());
        assertEquals(4,g.getDeck(0,0).DeckLength());
        assertEquals(4,g.getDeck(0,1).DeckLength());
        assertEquals(4,g.getDeck(0,2).DeckLength());
        assertEquals(4,g.getDeck(1,0).DeckLength());
        assertEquals(4,g.getDeck(1,1).DeckLength());
        assertEquals(4,g.getDeck(1,2).DeckLength());
        assertEquals(4,g.getDeck(2,0).DeckLength());
        assertEquals(4,g.getDeck(2,1).DeckLength());
        assertEquals(4,g.getDeck(2,2).DeckLength());
        assertEquals(4,g.getDeck(3,2).DeckLength());
        assertEquals(4,g.getDeck(3,2).DeckLength());
        assertEquals(4,g.getDeck(3,2).DeckLength());
        assertEquals(3, g.pushColumnMarket(0).size());
        assertEquals(4, g.pushRowMarket(1).size());
        g.movePlayerFaithPath(0,2);
        g.movePlayerFaithPath(1,5);
        assertEquals(2, g.getPlayerFaithPathPosition(0));
        assertEquals(5, g.getPlayerFaithPathPosition(1));
        assertEquals(0, g.get_PV(0));
        assertEquals(1, g.get_PV(1));
    }
}
