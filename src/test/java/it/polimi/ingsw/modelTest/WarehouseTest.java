package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.enums.Resources;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

public class WarehouseTest {
    @Test
        public void getResourcesTest(){
        Warehouse w=new Warehouse();
        assertEquals(0,w.getResource(Resources.COIN));
        w.AddResources(Resources.COIN,3);
        w.AddResources(Resources.COIN,3);
        w.AddResources(Resources.COIN, 3);
        w.AddResources(Resources.SERVANT, 2);
        w.AddResources(Resources.SERVANT, 2);
        w.AddResources(Resources.STONE,1);
        assertEquals(3,w.getResource(Resources.COIN));
        assertEquals(2,w.getResource(Resources.SERVANT));
        assertEquals(1,w.getResource(Resources.STONE));
        w.popResources(Resources.COIN);
        assertEquals(2, w.getResource(Resources.COIN));
        assertEquals(2,w.getResource(Resources.SERVANT));
        w.popResources(Resources.COIN);
        w.popResources(Resources.SERVANT);
        assertEquals(1,w.getResource(Resources.COIN));
        assertEquals(1,w.getResource(Resources.STONE));
        assertEquals(0,w.getResource(Resources.SHIELD));
    }
    @Test
        public void AddResourcesTest(){
        Warehouse w=new Warehouse();
        assertTrue(w.AddResources(Resources.COIN,3));
        assertTrue(w.AddResources(Resources.COIN,3));
        assertEquals(2, w.Resourcesnumb());
        assertEquals(Resources.COIN, w.getR(4));
        assertEquals(Resources.WHITE, w.getR(5));
        assertTrue(w.AddResources(Resources.COIN, 3));
        assertFalse(w.AddResources(Resources.COIN, 3));
        assertTrue(w.AddResources(Resources.SERVANT, 2));
        assertFalse(w.AddResources(Resources.COIN, 2));
        assertEquals(Resources.COIN, w.getR(5));
        assertEquals(4, w.Resourcesnumb());
        assertTrue(w.AddResources(Resources.SERVANT, 2));
        assertFalse( w.AddResources(Resources.COIN, 3));
        assertEquals(5, w.Resourcesnumb());
        assertFalse( w.AddResources(Resources.SERVANT, 1));
        assertFalse( w.AddResources(Resources.COIN, 1));
        assertEquals(5, w.Resourcesnumb());
        assertEquals(Resources.SERVANT, w.getR(1));
        assertEquals(Resources.SERVANT, w.getR(2));
        assertTrue(w.AddResources(Resources.STONE,1));
        assertFalse( w.AddResources(Resources.STONE, 1));
        assertFalse(w.AddResources(Resources.SHIELD,1));
        assertFalse(w.AddResources(Resources.SHIELD,2));
        assertFalse(w.AddResources(Resources.SHIELD,3));
    }
    @Test
        public void MoveRowTest(){
        Warehouse w=new Warehouse();
        w.AddResources(Resources.COIN,3);
        w.AddResources(Resources.COIN,3);
        assertTrue(w.MoveRow(2,3));
        assertFalse(w.MoveRow(1,2));
        assertEquals(Resources.COIN, w.getR(2));
        assertEquals(Resources.WHITE, w.getR(5));
        assertFalse(w.AddResources(Resources.COIN, 3));
        w.MoveRow(2,3);
        w.AddResources(Resources.COIN, 3);
        assertFalse(w.MoveRow(2,3));
        w.AddResources(Resources.SERVANT, 2);
        assertFalse(w.MoveRow(1,3));
        assertTrue(w.MoveRow(1,2));
        w.MoveRow(1,2);
        w.AddResources(Resources.SERVANT, 2);
        assertFalse(w.MoveRow(1,2));
        w.AddResources(Resources.STONE,1);
        w.popResources(Resources.SERVANT);
        assertTrue(w.MoveRow(1,2));
        w.popResources(Resources.COIN);
        w.popResources(Resources.COIN);
        assertTrue(w.MoveRow(2,3));
        assertTrue(w.MoveRow(1,3));
    }
    @Test
        public void popResourcesTest(){
        Warehouse w= new Warehouse();
        w.AddResources(Resources.COIN,3);
        w.AddResources(Resources.COIN,3);
        w.AddResources(Resources.COIN, 3);
        w.AddResources(Resources.SERVANT, 2);
        w.AddResources(Resources.SERVANT, 2);
        w.AddResources(Resources.STONE,1);
        assertTrue(w.popResources(Resources.COIN));
        assertEquals(Resources.WHITE, w.getR(5));
        assertFalse(w.popResources(Resources.SHIELD));
        assertTrue(w.popResources(Resources.STONE));
        assertFalse(w.popResources(Resources.STONE));
        assertEquals(Resources.WHITE, w.getR(0));
        assertTrue(w.popResources(Resources.SERVANT));
        assertEquals(Resources.WHITE, w.getR(2));
        assertEquals(3, w.Resourcesnumb());
        assertTrue(w.popResources(Resources.SERVANT));
        assertEquals(2, w.Resourcesnumb());
        assertEquals(Resources.WHITE, w.getR(1));
        assertEquals(Resources.COIN, w.getR(3));
        assertTrue(w.popResources(Resources.COIN));
        assertEquals(1, w.Resourcesnumb());
        assertTrue(w.popResources(Resources.COIN));
        assertEquals(0, w.Resourcesnumb());
        assertEquals(Resources.WHITE, w.getR(0));
        assertEquals(Resources.WHITE, w.getR(3));
    }
}
