package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.enums.Resources;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;

public class MarketTest {

@Test
    public void pushColumnTest(){
        Market m=new Market();
        ArrayList<Resources> r=init();
    r.remove(m.getSlide());

        for (int i=0;i<4;i++) {

            r.removeAll(m.pushColumn(i));

        }
    assertEquals(0, r.size());

    }
    @Test
    public void pushRowTest() {
        Market m = new Market();
        ArrayList<Resources> r = init();
        r.remove(m.getSlide());

        for (int i=0;i<3;i++) {
            r.removeAll(m.pushRow(i));

        }

        assertEquals(0, r.size());
    }

public ArrayList<Resources> init(){
    ArrayList<Resources> r=new ArrayList<>();
    r.add(Resources.STONE);
    r.add(Resources.STONE);

    r.add(Resources.COIN);
    r.add(Resources.COIN);

    r.add(Resources.FAITH);

    r.add(Resources.SERVANT);
    r.add(Resources.SERVANT);

    r.add(Resources.SHIELD);
    r.add(Resources.SHIELD);

    r.add(Resources.WHITE);
    r.add(Resources.WHITE);
    r.add(Resources.WHITE);
    r.add(Resources.WHITE);
    return r;
}

}
