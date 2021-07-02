package it.polimi.ingsw.test.modelTest;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class tests the complete game instance.
 */
public class GameBoardTest {
    /**
     * Create a multiplayer game, set its players and checks that initialization correctly occurs.
     */
    @Test
    public void GameBoardTestMultiplayer() {
        GameBoard g = new GameBoard();
        HumanPlayer h = new HumanPlayer("Giorgio", true);
        g.addPlayer(h);
        HumanPlayer f = new HumanPlayer("Luca", true);
        g.addPlayer(f);
        assertEquals(2, g.getPlayers().size());
        HumanPlayer k = new HumanPlayer("Gianmarco", true);
        g.addPlayer(k);
        HumanPlayer l = new HumanPlayer("Enri", true);
        g.addPlayer(l);
        assertEquals(4, g.getPlayers().size());
        assertEquals(4, g.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.BLUE, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.BLUE, 3).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 3).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 3).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 3).DeckLength());
        g.init(g);
        assertEquals(3, g.pushColumnMarket(0).size());
        assertEquals(4, g.pushRowMarket(1).size());
        g.movePlayerFaithPath(0, 2);
        g.movePlayerFaithPath(1, 5);
        assertEquals(2, g.getPlayerFaithPathPosition(0));
        assertEquals(5, g.getPlayerFaithPathPosition(1));
        assertEquals(0, g.get_PV(0));
        assertEquals(1, g.get_PV(1));
    }

    /**
     * Try to create a single player game and check bot actions are correctly applied.
     */
    @Test
    public void GameBoardBotTest() {
        GameBoard g = new GameBoard();
        HumanPlayer l = new HumanPlayer("Valerio", true);
        g.addPlayer(l);
        g.init(g);
        assertNotEquals(null, g.getBot());
        assertEquals(1, g.getPlayers().size());
        assertEquals(4, g.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.BLUE, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.BLUE, 3).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 3).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 3).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 2).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 3).DeckLength());
        assertEquals(3, g.pushColumnMarket(0).size());
        assertEquals(4, g.pushRowMarket(1).size());
        g.movePlayerFaithPath(0, 2);
        g.movePlayerFaithPath(1, 5);
        assertEquals(2, g.getPlayerFaithPathPosition(0));
        assertEquals(5, g.getPlayerFaithPathPosition(1));
        assertEquals(0, g.get_PV(0));
        assertEquals(1, g.get_PV(1));
    }
}
