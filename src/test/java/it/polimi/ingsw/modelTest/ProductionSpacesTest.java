package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.player.SpaceProd;
import it.polimi.ingsw.model.tools.CardParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class ProductionSpacesTest {

@Test
    public void ProductionSpaces() {
        SpaceProd s = new SpaceProd(new DevelopmentCard(12, 1, new int[]{ 1, 2, 3, 4}, new int[]{ 4, 5, 6, 7}, new int[]{8, 9, 10, 11}, Colors.BLUE, 1));
        assertArrayEquals(new int[]{1, 2, 3, 4}, s.getTop().getCostCard());
        assertArrayEquals(new int[]{4, 5, 6, 7}, s.getTop().getCostProduction());
        assertArrayEquals(new int[]{8, 9, 10, 11}, s.getTop().getProductionResult());
        assertEquals(Colors.BLUE, s.getTop().getColor());
        assertEquals(1, s.getTop().getLevel());
        assertEquals(s.getVictoryPoints(), 12);
        assertEquals(s.checkColor(Colors.BLUE), 1);
        assertEquals(s.getNumbCard(), 1);
        s.addCard(new DevelopmentCard(9, 2, new int[]{1}, new int[]{ 2}, new int[]{3}, Colors.YELLOW, 2));
        assertArrayEquals(new int[]{1}, s.getTop().getCostCard());
        assertArrayEquals(new int[]{2}, s.getTop().getCostProduction());
        assertArrayEquals(new int[]{3}, s.getTop().getProductionResult());
        assertEquals(Colors.YELLOW, s.getTop().getColor());
        assertEquals(2, s.getTop().getLevel());
        assertEquals(s.getVictoryPoints(), 21);
        assertEquals(s.checkColor(Colors.BLUE), 1);
        assertEquals(s.checkColor(Colors.YELLOW), 1);
        assertEquals(s.getNumbCard(), 2);

    }


}
