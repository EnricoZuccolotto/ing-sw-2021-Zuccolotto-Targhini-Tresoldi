package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.exceptions.playerboard.InsufficientLevelException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.board.DecoratedWarehousePlayerBoard;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SimplePlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerBoardTest {
    @Test
    public void initTest(){
        SimplePlayerBoard s= new SimplePlayerBoard(true);
        assertTrue(s.getInkwell());
        assertEquals(0, s.getLeaderCardsNumber());
        SimplePlayerBoard g= new SimplePlayerBoard(false);
        assertFalse(g.getInkwell());
    }
    @Test
    public void LeadCardTest(){
        SimplePlayerBoard s = new SimplePlayerBoard(true);
        assertEquals(0, s.getLeaderCardsNumber());
        int[] a = {0, 0};
        LeaderCard l = new LeaderCard(5, 1, a, a, Advantages.CHANGE, a);
        s.addLeaderCard(l);
        assertEquals(l, s.getLeaderCard(0));
        assertEquals(1, s.getLeaderCardsNumber());
        LeaderCard g = new LeaderCard(3, 1, a, a, Advantages.CHANGE, a);
        s.addLeaderCard(g);
        assertEquals(g, s.getLeaderCard(1));
        assertEquals(2, s.getLeaderCardsNumber());
        s.removeLeaderCard(l);
        assertEquals(1, s.getLeaderCardsNumber());
        s.removeLeaderCard(g);
        assertEquals(0, s.getLeaderCardsNumber());
        s.addLeaderCard(g);
        s.addLeaderCard(l);
        g.flipCard();
        l.flipCard();
        assertEquals(8, s.getVictoryPointsCards());
    }
    @Test
    public void ExtraBoardTest(){
        SimplePlayerBoard s= new SimplePlayerBoard(true);
        int[] a={0,0,0,0};
        try {
            s.addWarehouseSpace(Resources.COIN,2);
            fail();
        }
        catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertEquals(4, s.getExtraResources().size());
        try {
            s.addExtraResources(Resources.COIN,2);
            fail();
        }
        catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            s.takeExtraResources(Resources.COIN,2);
            fail();
        }
        catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            s.addDiscount(Resources.COIN,2);
            fail();
        }
        catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertFalse(s.isResourceDiscounted(Resources.COIN));
        assertEquals(0, s.getResourceDiscount(Resources.COIN));
        try {
            s.addSubstitute(Resources.COIN);
            fail();
        }
        catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        try {
            s.addProduction(Resources.COIN);
            fail();
        }
        catch (IllegalDecoratorException e) {
            assertTrue(true);
        }
        assertEquals(4, s.getSubstitutes().size());
        for (int i = 0; i < 4; i++) {
            assertFalse(s.getSubstitutes().get(i));
        }
        assertFalse(s.isResourceSubstitutable(Resources.COIN));
        assertNull(s.getProductions(Resources.COIN));
        assertTrue(s.payResourcesSpecialWarehouse(a));
        assertTrue(s.checkResourcesSpecialWarehouse(a));
    }

    @Test
    public void WareTest() {
        SimplePlayerBoard s = new SimplePlayerBoard(true);
        int[] a = {0, 2, 0, 0};
        int[] b = {0, 1, 0, 0};
        assertTrue(s.addWarehouseResource(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(s.addWarehouseResource(Resources.COIN, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertFalse(s.addWarehouseResource(Resources.COIN, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertTrue(s.shiftWarehouseRows(WarehousePositions.WAREHOUSE_SECOND_ROW, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(s.checkResources(a));
        assertTrue(s.checkResources(b));
        assertTrue(s.checkResourcesWarehouse(a));
        assertTrue(s.checkResourcesWarehouse(b));
        assertTrue(s.payResourcesWarehouse(a));
        assertFalse(s.checkResources(a));
        assertFalse(s.checkResources(b));
        assertFalse(s.checkResourcesWarehouse(a));
        assertFalse(s.checkResourcesWarehouse(b));
        assertTrue(s.addWarehouseResource(Resources.SERVANT, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(s.addWarehouseResource(Resources.SERVANT, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(s.addWarehouseResource(Resources.SERVANT, WarehousePositions.WAREHOUSE_THIRD_ROW));
        assertTrue(s.addWarehouseResource(Resources.COIN, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertTrue(s.addWarehouseResource(Resources.COIN, WarehousePositions.WAREHOUSE_SECOND_ROW));
        assertEquals(1, Math.floorDiv(s.getNumberResources(), 5));
    }
    @Test
    public void ProdcardTest(){
        SimplePlayerBoard s= new SimplePlayerBoard(true);
        int[] a={0,0,0,0};
        int[] b={1,1,1,1};
        int[] col1={2,0,0,0};
        int[] col2={2,1,0,0};
        int[] col3={2,1,1,3};
        DevelopmentCard d= new DevelopmentCard(0,0,a,b,a, Colors.BLUE,1);
        DevelopmentCard e= new DevelopmentCard(1,2,a,b,a, Colors.BLUE,1);
        assertTrue(s.addProductionCard(d, 0));
        try {
            s.addProductionCard(e,0);
            fail();
        }
        catch (InsufficientLevelException w) {
            assertTrue(true);
        }
        assertTrue(s.addProductionCard(e, 2));
        assertTrue(s.checkColors(col1));
        assertFalse(s.checkColors(col2));
        assertEquals(b,s.getProductionCost(0));
        assertEquals(a, s.getProductionResult(0));
        DevelopmentCard g= new DevelopmentCard(1,2,a,a,b, Colors.YELLOW,2);
        assertTrue(s.addProductionCard(g, 0));
        assertEquals(a, s.getProductionCost(0));
        assertEquals(b, s.getProductionResult(0));
        assertTrue(s.checkColors(col1));
        assertTrue(s.checkColorsAndLevel(col1, 1));
        assertTrue(s.checkColors(col2));
        DevelopmentCard h= new DevelopmentCard(1,2,a,a,a, Colors.PURPLE,3);
        try {
            s.addProductionCard(h,1);
            fail();
        }
        catch (InsufficientLevelException w) {
            assertTrue(true);
        }
        try {
            s.addProductionCard(g,0);
            fail();
        }
        catch (InsufficientLevelException w) {
            assertTrue(true);
        }
        assertTrue(s.addProductionCard(h,0));
        DevelopmentCard i= new DevelopmentCard(2,2,a,a,a, Colors.GREEN,1);
        DevelopmentCard l= new DevelopmentCard(3,2,a,a,a, Colors.GREEN,2);
        DevelopmentCard m= new DevelopmentCard(4,2,a,a,a, Colors.GREEN,3);
        try {
            s.addProductionCard(i,0);
            fail();
        }
        catch (InsufficientLevelException w) {
            assertTrue(true);
        }
        assertTrue(s.addProductionCard(i, 1));
        assertTrue(s.checkColors(col1));
        assertTrue(s.checkColors(col2));
        assertTrue(s.addProductionCard(l, 1));
        try {
            s.addProductionCard(m, 1);
            fail();
        }
        catch (WinnerException w) {
            assertTrue(true);
        }
        assertTrue(s.checkColors(col3));
        assertEquals(12,s.getVictoryPointsCards());
    }
    @Test
    public void StrongTest(){
        SimplePlayerBoard s= new SimplePlayerBoard(true);
        int[] a={0,3,0,5};
        int[] b={0,1,1,1};
        int[] c={0,1,0,1};
        int[] d={1,3,3,5};
        s.addStrongboxResource(Resources.COIN,3);
        s.addStrongboxResource(Resources.SHIELD,5);
        assertTrue(s.checkResourcesStrongbox(a));
        assertFalse(s.checkResourcesStrongbox(b));
        s.addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_FIRST_ROW);
        assertFalse(s.checkResourcesStrongbox(b));
        s.addStrongboxResource(Resources.STONE, 3);
        assertTrue(s.payResourcesStrongbox(b));
        assertTrue(s.checkResourcesStrongbox(b));
        assertTrue(s.payResourcesStrongbox(c));
        assertTrue(s.checkResourcesStrongbox(c));
        s.addWarehouseResource(Resources.SERVANT, WarehousePositions.WAREHOUSE_SECOND_ROW);
        s.addStrongboxResource(Resources.SHIELD, 2);
        s.addStrongboxResource(Resources.COIN, 2);
        assertTrue(s.checkResources(d));
        assertTrue(s.checkResources(a));
        assertTrue(s.checkResourcesStrongbox(a));
        assertFalse(s.checkResourcesStrongbox(d));
        assertTrue(s.payResourcesStrongbox(c));
        assertEquals(2, Math.floorDiv(s.getNumberResources(), 5));
    }

    @Test
    public void VictoryPointsTest() {
        HumanPlayer player = new HumanPlayer("test", true);
        PlayerBoard board = new DecoratedWarehousePlayerBoard(player.getPlayerBoard());
        board.addWarehouseSpace(Resources.COIN, 2);
        board.addExtraResources(Resources.COIN, 2);
        board.addStrongboxResource(Resources.STONE, 15);
        board.addWarehouseResource(Resources.SHIELD, WarehousePositions.WAREHOUSE_THIRD_ROW);
        board.addWarehouseResource(Resources.SHIELD, WarehousePositions.WAREHOUSE_THIRD_ROW);
        board.addWarehouseResource(Resources.SHIELD, WarehousePositions.WAREHOUSE_THIRD_ROW);
        assertEquals(board.getVictoryPointsCards(), 0);
        assertEquals(board.getNumberResources(), 20);
        board.addLeaderCard(new LeaderCard(10, 2, null, null, null, new int[]{0, 0, 0, 3}));
        assertEquals(0, board.getVictoryPointsCards());
        board.getLeaderCard(0).flipCard();
        assertEquals(10, board.getVictoryPointsCards());
        board.addProductionCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, new int[]{0, 1, 2, 0}, new int[]{7, 5, 2, 0, 0, 2}, Colors.BLUE, 1), 0);
        assertEquals(14, board.getVictoryPointsCards());
    }
}
