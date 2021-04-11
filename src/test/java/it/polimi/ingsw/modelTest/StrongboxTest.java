package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.enums.Resources;

import org.junit.Test;
import static org.junit.Assert.*;

public class StrongboxTest {
    @Test
        public void strongboxTest(){
        Strongbox s= new Strongbox();
        s.setResources(Resources.SHIELD, 4);
        s.setResources(Resources.STONE, 1);
        assertEquals(5, s.getNumResources());
        s.addResources(Resources.SHIELD, 2);
        assertEquals(7, s.getNumResources());
        s.setResources(Resources.SHIELD, 1);
        assertEquals(2, s.getNumResources());
        s.setResources(Resources.SERVANT, 10);
        s.setResources(Resources.COIN, 5);
        assertEquals(17, s.getNumResources());
        s.removeResources(Resources.SERVANT, 5);
        s.removeResources(Resources.COIN, 2);
        assertEquals(10, s.getNumResources());
    }
}
