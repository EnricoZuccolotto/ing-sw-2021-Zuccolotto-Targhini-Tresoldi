package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.NetworkLayerView;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerTest {
 @Test
 public void OnMessageTest() {

     GameController gameController = new GameController(false) {
         @Override
         public void StartGame() {
             getRoundController().init();
             getInstance().setPublicCommunication("The game is starting", CommunicationMessage.STARTING_GAME);
             setGameState(GameState.GAME_STARTED);
             getRoundController().handle_firstTurn();
         }
     };
     GameBoard gb = gameController.getInstance();
     gb.addPlayer(new HumanPlayer("Harry", true));
     gb.addPlayer(new HumanPlayer("Voldemort", true));
     gb.addPlayer(new HumanPlayer("Ron", true));
     gb.addPlayer(new HumanPlayer("Hermione", true));
     gb.init(gb);
     gameController.onMessage(new FirstActionMessage("Hermione", 1, 0));
     assertEquals(gb.getPlayer("Hermione").getPrivateCommunication().getMessage(), "The game is not started yet.");
     gameController.onMessage(new FirstActionMessage("Harry", 1, 0));
     assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "The game is not started yet.");
  gameController.onMessage(new FirstActionMessage("Ron", 1, 0));
  assertEquals(gb.getPlayer("Ron").getPrivateCommunication().getMessage(), "The game is not started yet.");
  gameController.onMessage(new FirstActionMessage("Voldemort", 1, 0));
  assertEquals(gb.getPlayer("Voldemort").getPrivateCommunication().getMessage(), "The game is not started yet.");
  gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
  assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "The game is not started yet.");
  gameController.StartGame();
  gameController.onMessage(new LobbyJoinMessage("Ron"));
  assertEquals(gb.getPlayer("Ron").getPrivateCommunication().getMessage(), "You cannot do this action in this state FIRST_TURN");
  gameController.onMessage(new LobbySetMessage("Ron", 1));
  assertEquals(gb.getPlayer("Ron").getPrivateCommunication().getMessage(), "You cannot do this action in this state FIRST_TURN");

  // first turn
  gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
  assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot do this action in this state FIRST_TURN");
  gameController.onMessage(new GetProductionCardMessage("Hermione", null, Colors.YELLOW, 0, 0));
  assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot do this action in this state FIRST_TURN");
  gameController.onMessage(new FirstActionMessage("Hermione", 1, 0));
  gameController.onMessage(new FirstActionMessage("Harry", 2, 3));
  gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
  assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot do this action in this state FIRST_TURN");
  gameController.onMessage(new FirstActionMessage("Voldemort", 1, 0));
  gameController.onMessage(new FirstActionMessage("Ron", 2, 3));

  // second action
  ArrayList<Resources> r = new ArrayList<>();
  r.add(Resources.STONE);
  gameController.onMessage(new SecondActionMessage("Hermione", r));
  assertEquals(gb.getPlayer("Hermione").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
  gameController.onMessage(new MarketRequestMessage("Harry", 2, 0));
  assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot do this action in this state SECOND_TURN");
  gameController.onMessage(new SecondActionMessage("Voldemort", r));
  assertTrue(gb.getPlayers().get(1).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 1, 0}));
  r.add(Resources.SHIELD);
  gameController.onMessage(new SecondActionMessage("Hermione", r));
  r.remove(0);
  gameController.onMessage(new SecondActionMessage("Ron", r));

  assertEquals(1, gb.getPlayerFaithPathPosition(2));
  assertEquals(1, gb.getPlayerFaithPathPosition(3));

  //Game_turn
  for (int i = 1; i < 4; i++) {
   gameController.onMessage(new MarketRequestMessage(gb.getPlayers().get(i).getName(), 0, 0));
   assertEquals(gb.getPlayers().get(i).getPrivateCommunication().getMessage(), "This is not your turn");
  }
  assertTrue(true);
 }

 @Test
 public void addViewTest(){
     NetworkLayerView view = new NetworkLayerView(null);
     GameController gc = new GameController(false);
     gc.addView("prova", view);
     assertEquals(gc.getViewFromMap("prova"), view);
 }

 @Test
 public void localViewTest(){

 }

}