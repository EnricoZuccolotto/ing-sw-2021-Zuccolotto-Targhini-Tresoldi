package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.model.board.*;
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

    @Test
    public void TestingDiscountDecorator() {
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = new DecoratedCostPlayerBoard(player.getPlayerBoard());
        board.addDiscount(Resources.COIN, 2);

        assertTrue(board.isResourceDiscounted(Resources.COIN));
        assertFalse(board.isResourceDiscounted(Resources.SHIELD));
        assertFalse(board.isResourceDiscounted(Resources.SERVANT));
        assertFalse(board.isResourceDiscounted(Resources.STONE));

        assertEquals(board.getResourceDiscount(Resources.COIN), 2);
        assertEquals(board.getResourceDiscount(Resources.SHIELD), 0);
        assertEquals(board.getResourceDiscount(Resources.SERVANT), 0);
        assertEquals(board.getResourceDiscount(Resources.STONE), 0);


        // Checking that other decorators fail as expected
        // Warehouse
        try {
            board.addWarehouseSpace(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            board.addExtraResources(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            board.takeExtraResources(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertEquals(board.getExtraResources().get(0).intValue(), 0);
        assertEquals(board.getExtraResources().get(1).intValue(), 0);
        assertEquals(board.getExtraResources().get(2).intValue(), 0);
        assertEquals(board.getExtraResources().get(3).intValue(), 0);
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

    @Test
    public void TestingWhiteMarbleDecorator() {
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = new DecoratedChangePlayerBoard(player.getPlayerBoard());
        board.addSubstitute(Resources.COIN);

        assertTrue(board.isResourceSubstitutable(Resources.COIN));
        assertFalse(board.isResourceSubstitutable(Resources.SERVANT));
        assertFalse(board.isResourceSubstitutable(Resources.SHIELD));
        assertFalse(board.isResourceSubstitutable(Resources.STONE));
        assertFalse(board.getSubstitutes().get(0));
        assertTrue(board.getSubstitutes().get(1));
        assertFalse(board.getSubstitutes().get(2));
        assertFalse(board.getSubstitutes().get(3));

        // Checking that other decorators fail as expected
        // Warehouse
        try {
            board.addWarehouseSpace(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            board.addExtraResources(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            board.takeExtraResources(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertEquals(board.getExtraResources().get(0).intValue(), 0);
        assertEquals(board.getExtraResources().get(1).intValue(), 0);
        assertEquals(board.getExtraResources().get(2).intValue(), 0);
        assertEquals(board.getExtraResources().get(3).intValue(), 0);
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

    @Test
    public void TestingProductionDecorator() {
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = new DecoratedProductionPlayerBoard(player.getPlayerBoard());
        board.addProduction(Resources.SERVANT);

        assertEquals(board.getProductions(Resources.SERVANT).get(0).compareTo(Resources.WHATEVER), 0);
        assertEquals(board.getProductions(Resources.SERVANT).get(1).compareTo(Resources.FAITH), 0);
        assertNull(board.getProductions(Resources.COIN));
        assertNull(board.getProductions(Resources.SHIELD));
        assertNull(board.getProductions(Resources.STONE));

        // Checking that other decorators fail as expected
        // Warehouse
        try {
            board.addWarehouseSpace(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            board.addExtraResources(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            board.takeExtraResources(Resources.COIN, 2);
            fail();
        } catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertEquals(board.getExtraResources().get(0).intValue(), 0);
        assertEquals(board.getExtraResources().get(1).intValue(), 0);
        assertEquals(board.getExtraResources().get(2).intValue(), 0);
        assertEquals(board.getExtraResources().get(3).intValue(), 0);
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
    }

    @Test
    public void TestingADoubleDecorator(){
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = new DecoratedChangePlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        board.addWarehouseSpace(Resources.COIN, 2);
        board.addSubstitute(Resources.SHIELD);

        assertTrue(board.getSubstitutes().get(3));
        assertTrue(board.addExtraResources(Resources.COIN, 2));
        try{
            board.addProduction(Resources.COIN);
            fail();
        } catch(IllegalDecoratorException ex){
            assertTrue(true);
        }
    }
}
