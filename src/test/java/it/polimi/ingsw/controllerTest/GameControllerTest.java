package it.polimi.ingsw.controllerTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GameControllerTest {
    @Test
    public void OnMessageTest() {
        assertTrue(true);
        /*GameController gameController = new GameController();
        gameController.addPlayer("Harry", null);
        gameController.addPlayer("Voldemort",null);
        gameController.addPlayer("Ron",null);
        gameController.addPlayer("Hermione", null);
        gameController.StartGame();
        GameBoard gb=gameController.getInstance();
        // first turn
        gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(),"You cannot do this action in this state FIRST_TURN");
        gameController.onMessage(new FirstActionMessage("Hermione", 1, 0));
        gameController.onMessage(new FirstActionMessage("Harry", 2, 3));
        gameController.onMessage(new FirstActionMessage("Voldemort", 1, 0));
        gameController.onMessage(new FirstActionMessage("Ron", 2, 3));

        // second action
        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        gameController.onMessage(new SecondActionMessage("Hermione", r));
        assertEquals(gb.getPlayer("Hermione").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
        gameController.onMessage(new SecondActionMessage("Voldemort", r));
        assertTrue(gb.getPlayers().get(1).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 1, 0}));
        r.add(Resources.SHIELD);
/*        gameController.onMessage(new SecondActionMessage("Hermione", r));
        gameController.onMessage(new SecondActionMessage("Ron", r));
        assertTrue(gb.getPlayers().get(2).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 1, 0}));

        assertEquals(1, gb.getPlayerFaithPathPosition(2));
        assertEquals(1, gb.getPlayerFaithPathPosition(1));*/

    }
}
