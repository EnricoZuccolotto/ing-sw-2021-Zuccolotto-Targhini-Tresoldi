package it.polimi.ingsw.test.modelTest;

import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;
import it.polimi.ingsw.model.player.Warehouse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the warehouse.
 */
public class WarehouseTest {
    /**
     * Adds some resources and checks they are correctly set.
     */
    @Test
    public void getResourcesTest() {
        Warehouse w = new Warehouse();
        assertEquals(0, w.getNumberResource(Resources.COIN));
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        w.AddResources(Resources.STONE, WarehousePositions.WAREHOUSE_FIRST_ROW);
        assertEquals(3, w.getNumberResource(Resources.COIN));
        assertEquals(2, w.getNumberResource(Resources.SERVANT));
        assertEquals(1, w.getNumberResource(Resources.STONE));
        w.popResources(Resources.COIN);
        assertEquals(2, w.getNumberResource(Resources.COIN));
        assertEquals(2, w.getNumberResource(Resources.SERVANT));
        w.popResources(Resources.COIN);
        w.popResources(Resources.SERVANT);
        assertEquals(1, w.getNumberResource(Resources.COIN));
        assertEquals(1, w.getNumberResource(Resources.STONE));
        assertEquals(0, w.getNumberResource(Resources.SHIELD));
    }

    /**
     * Checks if resource additions works correctly and fails if warehouse rules aren't followed.
     */
    @Test
    public void AddResourcesTest() {
        Warehouse w = new Warehouse();
        assertTrue(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertEquals(2, w.ResourceNumber());
        assertEquals(Resources.COIN, w.getR(4));
        assertEquals(Resources.WHITE, w.getR(5));
        assertTrue(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertFalse(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertFalse(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertEquals(Resources.COIN, w.getR(5));
        assertEquals(4, w.ResourceNumber());
        assertTrue(w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertFalse(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertEquals(5, w.ResourceNumber());
        assertFalse(w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_FIRST_ROW));
        assertFalse(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_FIRST_ROW));
        assertEquals(5, w.ResourceNumber());
        assertEquals(Resources.SERVANT, w.getR(1));
        assertEquals(Resources.SERVANT, w.getR(2));
        assertTrue(w.AddResources(Resources.STONE, WarehousePositions.WAREHOUSE_FIRST_ROW));
        assertFalse(w.AddResources(Resources.STONE, WarehousePositions.WAREHOUSE_FIRST_ROW));
        assertFalse(w.AddResources(Resources.SHIELD, WarehousePositions.WAREHOUSE_FIRST_ROW));
        assertFalse(w.AddResources(Resources.SHIELD, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertFalse(w.AddResources(Resources.SHIELD, WarehousePositions.WAREHOUSE_THIRD_ROW));
    }

    /**
     * Checks the row switching functionality.
     */
    @Test
    public void MoveRowTest() {
        Warehouse w = new Warehouse();
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        assertTrue(w.MoveRow(WarehousePositions.WAREHOUSE_SECOND_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertFalse(w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertEquals(Resources.COIN, w.getR(2));
        assertEquals(Resources.WHITE, w.getR(5));
        assertFalse(w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        w.MoveRow(WarehousePositions.WAREHOUSE_SECOND_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        assertFalse(w.MoveRow(WarehousePositions.WAREHOUSE_SECOND_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW));
        w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        assertFalse(w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_SECOND_ROW));
        w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_SECOND_ROW);
        w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        assertFalse(w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_SECOND_ROW));
        w.AddResources(Resources.STONE, WarehousePositions.WAREHOUSE_FIRST_ROW);
        w.popResources(Resources.SERVANT);
        assertTrue(w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_SECOND_ROW));
        w.popResources(Resources.COIN);
        w.popResources(Resources.COIN);
        assertTrue(w.MoveRow(WarehousePositions.WAREHOUSE_SECOND_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(w.MoveRow(WarehousePositions.WAREHOUSE_FIRST_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW));
    }

    /**
     * Checks that you can correctly remove resources from the market.
     */
    @Test
    public void popResourcesTest() {
        Warehouse w = new Warehouse();
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW);
        w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        w.AddResources(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        w.AddResources(Resources.STONE, WarehousePositions.WAREHOUSE_FIRST_ROW);
        assertTrue(w.popResources(Resources.COIN));
        assertEquals(Resources.WHITE, w.getR(5));
        assertFalse(w.popResources(Resources.SHIELD));
        assertTrue(w.popResources(Resources.STONE));
        assertFalse(w.popResources(Resources.STONE));
        assertEquals(Resources.WHITE, w.getR(0));
        assertTrue(w.popResources(Resources.SERVANT));
        assertEquals(Resources.WHITE, w.getR(2));
        assertEquals(3, w.ResourceNumber());
        assertTrue(w.popResources(Resources.SERVANT));
        assertEquals(2, w.ResourceNumber());
        assertEquals(Resources.WHITE, w.getR(1));
        assertEquals(Resources.COIN, w.getR(3));
        assertTrue(w.popResources(Resources.COIN));
        assertEquals(1, w.ResourceNumber());
        assertTrue(w.popResources(Resources.COIN));
        assertEquals(0, w.ResourceNumber());
        assertEquals(Resources.WHITE, w.getR(0));
        assertEquals(Resources.WHITE, w.getR(3));
    }
}
