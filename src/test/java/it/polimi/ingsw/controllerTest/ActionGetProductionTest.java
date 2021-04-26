package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.DecoratedWarehousePlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.ExchangeResources;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionGetProductionTest {
    @Test
    public void CorrectNumberResources(){
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);


        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));

        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        assertEquals(player.getPlayerBoard().getExtraResources().get(3), (Integer) 0);
    }

    @Test
    public void WrongNumberResourcesSent() {
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

        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 1, 0, 3}, new int[]{0, 0, 0, 0}));
        assertEquals(player.getPrivateCommunication().getMessage(), "The resources sent are different from the cost of the card");
        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());

        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 1, 2, 3}, new int[]{0, 0, 0, 0}));
        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, null, null, Colors.BLUE, 2));
        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}));
        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
    }

    @Test
    public void WrongNumberResources() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);

        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.INSUFFICIENT_RESOURCES);
        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
    }



    @Test
    public void WrongLevel() {
        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, null, null, Colors.BLUE, 1));

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 1, 2, 3}, null, null, Colors.BLUE, 3));

        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN, 1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 3});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }

        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);

        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "You don't have a space available");


        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
    }

    @Test
    public void winnerException() {
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
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);

        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}));
        assertEquals(6, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}));
        assertEquals(5, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}));
        assertEquals(4, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());

        actionController.getProduction(Colors.BLUE, 1, gameBoard, 0, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}));
        assertEquals(3, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        actionController.getProduction(Colors.BLUE, 1, gameBoard, 1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}));
        assertEquals(2, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        actionController.getProduction(Colors.BLUE, 1, gameBoard, 2, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}));
        assertEquals(1, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());
        try {
            actionController.getProduction(Colors.BLUE, 1, gameBoard, 0, player, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}));
            fail();
        } catch (WinnerException e) {
            assertTrue(true);
        }
        assertEquals(0, gameBoard.getDeck(Colors.BLUE, 1).DeckLength());


    }

}
