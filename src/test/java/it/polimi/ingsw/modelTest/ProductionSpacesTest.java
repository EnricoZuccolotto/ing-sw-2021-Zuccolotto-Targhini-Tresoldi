package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.player.SpaceProd;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ProductionSpacesTest {

@Test
    public void ProductionSpaces() {
    SpaceProd s = new SpaceProd();
    s.addCard(new DevelopmentCard(12, 1, new int[]{1, 2, 3, 4}, new int[]{4, 5, 6, 7}, new int[]{8, 9, 10, 11}, Colors.BLUE, 1));

    assertArrayEquals(new int[]{1, 2, 3, 4}, s.getTop().getCostCard());
    assertArrayEquals(new int[]{4, 5, 6, 7}, s.getTop().getCostProduction());
    assertArrayEquals(new int[]{8, 9, 10, 11}, s.getTop().getProductionResult());
    assertEquals(Colors.BLUE, s.getTop().getColor());
    assertEquals(1, s.getTop().getLevel());
    assertEquals(s.getVictoryPoints(), 12);
    assertEquals(s.checkColor(Colors.BLUE), 1);
    assertEquals(s.getNumbCard(), 1);
    s.addCard(new DevelopmentCard(9, 2, new int[]{1}, new int[]{2}, new int[]{3}, Colors.YELLOW, 2));
    assertArrayEquals(new int[]{1}, s.getTop().getCostCard());
    assertArrayEquals(new int[]{2}, s.getTop().getCostProduction());
    assertArrayEquals(new int[]{3}, s.getTop().getProductionResult());
    assertEquals(Colors.YELLOW, s.getTop().getColor());
    assertEquals(2, s.getTop().getLevel());
    assertEquals(s.checkColor(Colors.BLUE, 1), 1);
    assertEquals(s.checkColor(Colors.YELLOW, 2), 1);
    assertEquals(s.checkColor(Colors.YELLOW, 3), 0);
    assertEquals(s.getVictoryPoints(), 21);
    assertEquals(s.checkColor(Colors.BLUE), 1);
    assertEquals(s.checkColor(Colors.YELLOW), 1);
    assertEquals(s.getNumbCard(), 2);

}


}
