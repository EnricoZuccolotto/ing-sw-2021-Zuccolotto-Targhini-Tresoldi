package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.model.board.DecoratedWarehousePlayerBoard;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;

import static org.junit.Assert.*;

public class DecoratorsTest{
    @Test
    public void UndecoratedPlayerBoard(){
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = player.getPlayerBoard();
        try {
            board.addSubstitute(Resources.COIN);
            fail();
        } catch(IllegalDecoratorException e){
            assertTrue(true);
        }
    }

    @Test
    public void TestingWarehouseDecorator() {
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = new DecoratedWarehousePlayerBoard(player.getPlayerBoard());
        board.addWarehouseSpace(Resources.COIN, 2);

        assertTrue(board.addExtraResources(Resources.COIN, 1));
        assertTrue(board.addExtraResources(Resources.COIN, 1));
        assertFalse(board.addExtraResources(Resources.COIN, 12));
        assertFalse(board.addExtraResources(Resources.SERVANT, 1));
        assertEquals(board.getExtraResources().get(0).intValue(), 0);
        assertEquals(board.getExtraResources().get(1).intValue(), 2);
        assertEquals(board.getExtraResources().get(2).intValue(), 0);
        assertEquals(board.getExtraResources().get(3).intValue(), 0);
        assertTrue(board.takeExtraResources(Resources.COIN, 1));
        assertTrue(board.takeExtraResources(Resources.COIN, 1));
        assertFalse(board.takeExtraResources(Resources.COIN, 1));
        assertFalse(board.takeExtraResources(Resources.STONE, 1));
        assertEquals(board.getExtraResources().get(0).intValue(), 0);
        assertEquals(board.getExtraResources().get(1).intValue(), 0);
        assertEquals(board.getExtraResources().get(2).intValue(), 0);
        assertEquals(board.getExtraResources().get(3).intValue(), 0);

        // Checking that other decorators fail as expected
        // Discount
        try {
            board.addDiscount(Resources.COIN, 1);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertFalse(board.isResourceDiscounted(Resources.COIN));
        assertFalse(board.isResourceDiscounted(Resources.SERVANT));
        assertFalse(board.isResourceDiscounted(Resources.SHIELD));
        assertFalse(board.isResourceDiscounted(Resources.STONE));
        assertEquals(board.getResourceDiscount(Resources.COIN), 0);
        assertEquals(board.getResourceDiscount(Resources.SERVANT), 0);
        assertEquals(board.getResourceDiscount(Resources.SHIELD), 0);
        assertEquals(board.getResourceDiscount(Resources.STONE), 0);
        // White marble substitution
        try {
            board.addSubstitute(Resources.COIN);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertFalse(board.getSubstitutes().get(0));
        assertFalse(board.getSubstitutes().get(1));
        assertFalse(board.getSubstitutes().get(2));
        assertFalse(board.getSubstitutes().get(3));
        assertFalse(board.isResourceSubstitutable(Resources.COIN));
        assertFalse(board.isResourceSubstitutable(Resources.SERVANT));
        assertFalse(board.isResourceSubstitutable(Resources.SHIELD));
        assertFalse(board.isResourceSubstitutable(Resources.STONE));
        // Extra production
        try {
            board.addProduction(Resources.COIN);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertNull(board.getProductions(Resources.COIN));
        assertNull(board.getProductions(Resources.SERVANT));
        assertNull(board.getProductions(Resources.SHIELD));
        assertNull(board.getProductions(Resources.STONE));
    }

}
