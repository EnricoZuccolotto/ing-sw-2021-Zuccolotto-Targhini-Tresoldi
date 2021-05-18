package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.DecoratedWarehousePlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.tools.ExchangeResources;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ActionControllerTest {
    @Test
    public void GetProduction_CorrectNumberResources() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);


        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));

        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(player.getPlayerBoard().getExtraResources().get(3), (Integer) 0);
    }

    @Test
    public void GetProduction_WrongNumberResourcesSent() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addStrongboxResource(Resources.SHIELD, 3);
        player.getPlayerBoard().addStrongboxResource(Resources.STONE, 2);

        assertFalse(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 1, 0, 3}, new int[]{0, 0, 0, 0})));
        assertEquals(player.getPrivateCommunication().getMessage(), "The resources sent are different from the cost of the card");
        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());

        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 1, 2, 3}, new int[]{0, 0, 0, 0})));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, null, null, Colors.BLUE, 2));
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0})));
        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
    }

    @Test
    public void GetProduction_WrongNumberResources() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);

        assertFalse(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
    }


    @Test
    public void GetProduction_WrongLevel() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, null, null, Colors.BLUE, 1));

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 3));

        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0})));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);
        //wrong level
        assertFalse(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "You don't have a space available");


        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
    }

    @Test
    public void GetProduction_winnerException() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 1}, null, null, Colors.BLUE, 1));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 1}, null, null, Colors.BLUE, 1));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 1}, null, null, Colors.BLUE, 1));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 1}, null, null, Colors.BLUE, 2));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 1}, null, null, Colors.BLUE, 2));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 1}, null, null, Colors.BLUE, 2));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 2, 0}, null, null, Colors.BLUE, 3));

        player.getPlayerBoard().addStrongboxResource(Resources.SHIELD, 10);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);

        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0})));
        assertEquals(6, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0})));
        assertEquals(5, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0})));
        assertEquals(4, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());

        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, 0, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0})));
        assertEquals(3, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, 1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0})));
        assertEquals(2, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, 2, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0})));
        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        try {
            actionController.getProduction(Colors.BLUE, 1, gameBoard, 0, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}));
            fail();
        } catch (WinnerException e) {
            assertTrue(true);
        }
        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());


    }

    @Test
    public void useProduction_BaseProduction() {

        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);
        //wrong number of resources given

        assertFalse(actionController.useBaseProduction(player, 2, Resources.COIN, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "You sent the wrong number of resources");
        //wrong resource given

        assertFalse(actionController.useBaseProduction(player, 2, Resources.FAITH, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "The resource you sent is not SHIELD,STONE,SERVANT,COIN.");

        //normal functioning
        assertTrue(actionController.useBaseProduction(player, 2, Resources.SHIELD, new ExchangeResources(new int[]{0, 0, 1, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1})));

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 0, 1}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(3), (Integer) 2);

    }

    @Test
    public void useProduction_SpecialProduction() {

        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }
        player.getPlayerBoard().addLeaderCard(new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 1}));
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);

        // card not active

        assertFalse(actionController.useSpecialProduction(player, Resources.COIN, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "You need to activate this card to use it");

        player.getPlayerBoard().getLeaderCard(0).flipCard();
        //wrong number of resources given

        assertFalse(actionController.useSpecialProduction(player, Resources.COIN, 0, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "You sent the wrong number of resources");


        //wrong resource given

        assertFalse(actionController.useSpecialProduction(player, Resources.FAITH, 0, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1})));
        assertEquals(player.getPrivateCommunication().getMessage(), "The resource you sent is not SHIELD,STONE,SERVANT,COIN.");

        //normal functioning
        assertTrue(actionController.useSpecialProduction(player, Resources.COIN, 0, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1})));

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 0, 0}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(3), (Integer) 2);

    }

    @Test
    public void useProduction_NormalProduction() {

        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, new int[]{0, 1, 2, 0}, new int[]{7, 5, 2, 0, 2}, Colors.BLUE, 1));
        assertTrue(actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0, 25})));
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 5, 0, 0});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }


        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        player.getPlayerBoard().addExtraResources(Resources.COIN, 4);
        //wrong index given

        assertFalse(actionController.useNormalProduction(player, 2, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "You don't have a card in this position");
        //wrong number of resources given

        assertFalse(actionController.useNormalProduction(player, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3})));
        assertEquals(player.getPrivateCommunication().getMessage(), "That's not the cost of the card, you have sent the wrong number of resources");

        //correct resources but not enough int the warehouse

        assertFalse(actionController.useNormalProduction(player, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 1, 0, 0})));
        assertEquals(player.getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);

        player.getPlayerBoard().addWarehouseResource(Resources.STONE, WarehousePositions.WAREHOUSE_SECOND_ROW);
        //normal functioning
        assertTrue(actionController.useNormalProduction(player, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 1, 0, 0})));

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{7, 5, 2, 0}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(1), (Integer) 3);

    }

    @Test
    public void firstActionTest() {
        ActionController actionController = new ActionController();
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.addPlayer(new HumanPlayer("Ron", false));
        gb.addPlayer(new HumanPlayer("Hermione", false));
        gb.init(gb);
        // dealing leader cards
        ArrayList<LeaderCard> leaderCards = CardParser.parseLeadCards();
        if (leaderCards == null)
            throw new IllegalActionException();
        else
            for (HumanPlayer player : gb.getPlayers()) {
                for (int i = 0; i < 4; i++) {
                    player.getPlayerBoard().addLeaderCard(leaderCards.get(0));
                    leaderCards.remove(0);
                }
            }
        //first player
        ArrayList<LeaderCard> expected = new ArrayList<>();
        expected.add(gb.getPlayer("Harry").getPlayerBoard().getLeaderCard(0));
        expected.add(gb.getPlayer("Harry").getPlayerBoard().getLeaderCard(1));
        //wrong index sent
        assertFalse(actionController.firstAction(2, 4, gb.getPlayer("Harry")));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You don't have this card/s");
        //correct index sent
        assertTrue(actionController.firstAction(2, 3, gb.getPlayer("Harry")));
        assertEquals(gb.getPlayer("Harry").getPlayerBoard().getLeaderCard(0), expected.get(0));
        assertEquals(gb.getPlayer("Harry").getPlayerBoard().getLeaderCard(1), expected.get(1));
        expected.clear();

        //second player
        expected.add(gb.getPlayer("Enry").getPlayerBoard().getLeaderCard(2));
        expected.add(gb.getPlayer("Enry").getPlayerBoard().getLeaderCard(3));
        //correct index sent
        assertTrue(actionController.firstAction(1, 0, gb.getPlayer("Enry")));
        assertEquals(gb.getPlayer("Enry").getPlayerBoard().getLeaderCard(0), expected.get(0));
        assertEquals(gb.getPlayer("Enry").getPlayerBoard().getLeaderCard(1), expected.get(1));
        expected.clear();

        //third  player
        expected.add(gb.getPlayer("Ron").getPlayerBoard().getLeaderCard(1));
        expected.add(gb.getPlayer("Ron").getPlayerBoard().getLeaderCard(2));
        //correct index sent
        assertTrue(actionController.firstAction(3, 0, gb.getPlayer("Ron")));
        assertEquals(gb.getPlayer("Ron").getPlayerBoard().getLeaderCard(0), expected.get(0));
        assertEquals(gb.getPlayer("Ron").getPlayerBoard().getLeaderCard(1), expected.get(1));
        expected.clear();

        //fourth player
        expected.add(gb.getPlayer("Hermione").getPlayerBoard().getLeaderCard(0));
        expected.add(gb.getPlayer("Hermione").getPlayerBoard().getLeaderCard(3));
        //correct index sent
        assertTrue(actionController.firstAction(2, 1, gb.getPlayer("Hermione")));
        assertEquals(gb.getPlayer("Hermione").getPlayerBoard().getLeaderCard(0), expected.get(0));
        assertEquals(gb.getPlayer("Hermione").getPlayerBoard().getLeaderCard(1), expected.get(1));
        expected.clear();


    }

    @Test
    public void secondActionTest() {
        ActionController actionController = new ActionController();
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.addPlayer(new HumanPlayer("Ron", false));
        gb.addPlayer(new HumanPlayer("Hermione", false));
        gb.init(gb);
        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        r.add(Resources.FAITH);
        // third player 2 resources
        // wrong resource sent
        assertFalse(actionController.secondAction(r, 3, gb.getPlayer("Hermione")));
        assertEquals(gb.getPlayer("Hermione").getPrivateCommunication().getMessage(), "The resource you sent is not SHIELD,STONE,SERVANT,COIN.");
        // wrong number of resources sent
        r.remove(1);
        assertFalse(actionController.secondAction(r, 3, gb.getPlayer("Hermione")));
        assertEquals(gb.getPlayer("Hermione").getPrivateCommunication().getMessage(), "You sent the wrong number of resources");
        //correct action
        r.add(Resources.COIN);
        assertTrue(actionController.secondAction(r, 3, gb.getPlayer("Hermione")));
        assertTrue(gb.getPlayer("Hermione").getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 1, 0}));
        // first player nothing
        assertFalse(actionController.secondAction(r, 0, gb.getPlayer("Harry")));
        assertTrue(gb.getPlayer("Harry").getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 0, 0}));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You can't play in this preparation turn");
        //second player 1 resource
        r.remove(0);
        assertTrue(actionController.secondAction(r, 1, gb.getPlayer("Enry")));
        assertTrue(gb.getPlayer("Enry").getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 0, 0}));
        //third player 1 resource
        assertTrue(actionController.secondAction(r, 2, gb.getPlayer("Ron")));
        assertTrue(gb.getPlayer("Ron").getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 0, 0}));
    }

    @Test
    public void foldLeaderTest() {
        ActionController actionController = new ActionController();
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        // dealing leader cards
        ArrayList<LeaderCard> leaderCards = CardParser.parseLeadCards();
        if (leaderCards == null)
            throw new IllegalActionException();
        else
            for (HumanPlayer player : gb.getPlayers()) {
                for (int i = 0; i < 4; i++) {
                    player.getPlayerBoard().addLeaderCard(leaderCards.get(0));
                    leaderCards.remove(0);
                }
            }
        //wrong index
        assertFalse(actionController.foldLeader(6, gb.getPlayer("Harry")));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "Index 6 out of bounds for length 4");
        //leader card active
        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
        assertFalse(actionController.foldLeader(0, gb.getPlayer("Harry")));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot discard this card.");
        //normal remove
        LeaderCard c = gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(1);
        assertTrue(actionController.foldLeader(1, gb.getPlayer("Harry")));
        assertNotEquals(c, gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(1));
        assertNotEquals(c, gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(2));
        assertNotEquals(c, gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0));

    }


}
