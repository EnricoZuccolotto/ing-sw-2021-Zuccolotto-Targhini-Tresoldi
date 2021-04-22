package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.MarketRequestMessage;
import org.junit.Test;

public class GameControllerTest {
    @Test
    public void OnMessageTest() {
        GameController gameController = new GameController();
        gameController.addPlayer("Harry", null);
        gameController.addPlayer("Hermione", null);
        gameController.addPlayer("Ron", null);
        gameController.addPlayer("Voldemort", null);
        gameController.StartGame();
        gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
    }
}
