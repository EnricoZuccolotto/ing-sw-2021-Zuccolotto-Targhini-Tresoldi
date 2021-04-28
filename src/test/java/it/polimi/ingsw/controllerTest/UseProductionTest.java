package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.ActionController;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UseProductionTest {
    @Test
    public void BaseProduction(){

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

        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);
        //wrong number of resources given

        actionController.useBaseProduction(player, 2, Resources.COIN, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "You sent the wrong number of resources");
        //wrong resource given

        actionController.useBaseProduction(player, 2, Resources.FAITH, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "The resource you sent is not SHIELD,STONE,SERVANT,COIN.");

        //normal functioning
        actionController.useBaseProduction(player, 2, Resources.SHIELD, new ExchangeResources(new int[]{0, 0, 1, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}));

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 0, 1}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(3), (Integer) 2);

    }

    @Test
    public void SpecialProduction() {

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
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addExtraResources(Resources.SHIELD, 3);
        // card not active

        actionController.useSpecialProduction(player, Resources.COIN, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "You need to activate this card to use it");

        player.getPlayerBoard().getLeaderCard(0).flipCard();
        //wrong number of resources given

        actionController.useSpecialProduction(player, Resources.COIN, 0, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "You sent the wrong number of resources");


        //wrong resource given

        actionController.useSpecialProduction(player, Resources.FAITH, 0, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}));
        assertEquals(player.getPrivateCommunication().getMessage(), "The resource you sent is not SHIELD,STONE,SERVANT,COIN.");

        //normal functioning
        actionController.useSpecialProduction(player, Resources.COIN, 0, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}));

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 0, 0}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(3), (Integer) 2);

    }

    @Test
    public void NormalProduction() {

        ActionController actionController = new ActionController();
        HumanPlayer player = new HumanPlayer("Harry", true);
        GameBoard gameBoard = new GameBoard();
        gameBoard.addPlayer(player);
        for (int i = 0; i < 4; i++)
            gameBoard.getDeck(Colors.BLUE, 1).popFirstCard();

        gameBoard.getDeck(Colors.BLUE, 1).addCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, new int[]{0, 1, 2, 0}, new int[]{7, 5, 2, 0, 2}, Colors.BLUE, 1));
        actionController.getProduction(Colors.BLUE, 1, gameBoard, -1, player, new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0, 25}));
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard = new LeaderCard(1, 2, null, null, null, new int[]{0, 5, 0, 0});
        // Add the corresponding effect
        for (int i = 0; i < 4; i++) {
            if (leaderCard.getEffect().get(i) != 0) {
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
            }
        }


        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        player.getPlayerBoard().addExtraResources(Resources.COIN, 4);
        //wrong index given

        actionController.useNormalProduction(player, 2, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "You don't have a card in this position");
        //wrong number of resources given

        actionController.useNormalProduction(player, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3}));
        assertEquals(player.getPrivateCommunication().getMessage(), "That's not the cost of the card, you have sent the wrong number of resources");

        //correct resources but not enough int the warehouse

        actionController.useNormalProduction(player, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 1, 0, 0}));
        assertEquals(player.getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.INSUFFICIENT_RESOURCES);

        player.getPlayerBoard().addWarehouseResource(Resources.STONE, 2);
        //normal functioning
        actionController.useNormalProduction(player, 0, new ExchangeResources(new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 1, 0, 0}));

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{7, 5, 2, 0}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(1), (Integer) 3);

    }


}
