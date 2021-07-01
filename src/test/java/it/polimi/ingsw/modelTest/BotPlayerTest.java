package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.WinnerException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.BotActions;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.player.BotPlayer;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Bot Player test.
 */
public class BotPlayerTest {
    /**
     * Tests if the bot player correctly discarded 2 Development cards from the deck of the right color.
     */
    @Test
    public void discardTest() {
        GameBoard g = new GameBoard();
        HumanPlayer h = new HumanPlayer("Luca", true);
        g.addPlayer(h);
        g.init(g);
        BotPlayer b = g.getBot();
        assertEquals(4, g.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.PURPLE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.GREEN, 1).DeckLength());
        b.discard(Colors.BLUE);
        assertEquals(2, g.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(4, g.getDeck(Colors.YELLOW, 1).DeckLength());
        b.discard(Colors.YELLOW);
        assertEquals(2, g.getDeck(Colors.YELLOW, 1).DeckLength());
        b.discard(Colors.PURPLE);
        assertEquals(2, g.getDeck(Colors.PURPLE, 1).DeckLength());
        b.discard(Colors.GREEN);
        assertEquals(2, g.getDeck(Colors.GREEN, 1).DeckLength());
        b.discard(Colors.BLUE);
        assertEquals(0, g.getDeck(Colors.BLUE, 1).DeckLength());
        b.discard(Colors.BLUE);
        assertEquals(0, g.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(2, g.getDeck(Colors.BLUE, 2).DeckLength());
        b.discard(Colors.BLUE);
        assertEquals(0, g.getDeck(Colors.BLUE, 2).DeckLength());
        b.discard(Colors.BLUE);
        assertEquals(2, g.getDeck(Colors.BLUE, 3).DeckLength());
        try {
            b.discard(Colors.BLUE);
            fail();
        } catch (WinnerException e) {
            assertTrue(true);
        }
        assertEquals(0, g.getDeck(Colors.BLUE, 3).DeckLength());
    }

    /**
     * Tests if the bot player moves forward of 2 step towards the Pope.
     */
    @Test
    public void BlackCross2Test() {
        GameBoard g = new GameBoard();
        HumanPlayer h = new HumanPlayer("Luca", true);
        g.addPlayer(h);
        g.init(g);
        BotPlayer b = g.getBot();
        int i = 0;
        while (!(BotActions.BlackCross2.equals(b.getCurrentAction()))) {
            if (BotActions.BlackCross1Shuffle.equals(b.getCurrentAction())) {
                i++;
            }
            b.doAction();
        }
        if (BotActions.BlackCross2.equals(b.getCurrentAction())) {
            assertEquals(i, g.getPlayerFaithPathPosition(1));
            b.doAction();
            assertEquals(i + 2, g.getPlayerFaithPathPosition(1));
        }
    }

    /**
     * Tests if the bot player moves forward of 1 step towards the Pope and tests if the card are shuffled.
     */
    @Test
    public void BlackCross1ShuffleTest() {
        GameBoard g = new GameBoard();
        HumanPlayer h = new HumanPlayer("Luca", true);
        g.addPlayer(h);
        g.init(g);
        BotPlayer b = g.getBot();
        int i = 0;
        while (!(BotActions.BlackCross1Shuffle.equals(b.getCurrentAction()))) {
            if (BotActions.BlackCross2.equals(b.getCurrentAction())) {
                i = i + 2;
            }
            b.doAction();
        }
        if (BotActions.BlackCross1Shuffle.equals(b.getCurrentAction())) {
            assertEquals(i, g.getPlayerFaithPathPosition(1));
            b.doAction();
            assertEquals(i + 1, g.getPlayerFaithPathPosition(1));
        }
    }
}
