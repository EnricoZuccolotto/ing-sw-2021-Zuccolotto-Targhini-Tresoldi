package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.FirstActionMessage;
import it.polimi.ingsw.network.messages.MarketRequestMessage;
import it.polimi.ingsw.network.messages.SecondActionMessage;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {
    @Test
    public void OnMessageTest() {

     GameController gameController = new GameController();
     GameBoard gb = gameController.getInstance();
     gb.addPlayer(new HumanPlayer("Harry", true));
     gb.addPlayer(new HumanPlayer("Voldemort", true));
     gb.addPlayer(new HumanPlayer("Ron", true));
     gb.addPlayer(new HumanPlayer("Hermione", true));
     gameController.StartGame();

     // first turn
     gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
     assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot do this action in this state FIRST_TURN");
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
     gameController.onMessage(new SecondActionMessage("Hermione", r));
     r.remove(0);
     gameController.onMessage(new SecondActionMessage("Ron", r));

     assertEquals(1, gb.getPlayerFaithPathPosition(2));
     assertEquals(1, gb.getPlayerFaithPathPosition(3));

    }
}
