package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.controller.RoundController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.DecoratedProductionPlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.communication.CommunicationMessage;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.ExchangeResources;
import it.polimi.ingsw.network.messages.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RoundControllerTest {
    @Test
    public void Handle_foldLeaderTest(){
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.init(gb);
        RoundController g = new RoundController(gb, null);
        g.init();
        g.handle_firstTurn();
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);

        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));

        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        g.handle_secondAction(new SecondActionMessage("Enry", r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 4));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);

        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
        g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 1));
        assertEquals(gb.getPlayers().get(0).getPlayerBoard().getLeaderCardsNumber(), 0);
        assertEquals(gb.getPlayerFaithPathPosition(0), 1);
        assertEquals(g.getTurnState(), TurnState.NORMAL_ACTION);

        g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 0));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getMessage(), "You cannot do leader action");
        assertEquals(g.getTurnState(), TurnState.NORMAL_ACTION);
    }

    @Test
    public void Handle_addFaithPointsTest() {
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.init(gb);
        RoundController g = new RoundController(gb, null);
        g.init();
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);

        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));

        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        g.handle_secondAction(new SecondActionMessage("Enry", r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        g.handle_addFaithPoint(12, gb.getPlayer("Harry"));
        assertEquals(gb.getPlayerFaithPathPosition(0), 12);
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_addFaithPoint(123, gb.getPlayer("Harry"));
        assertEquals(g.getWinnerPlayer(), 0);

    }

    @Test
    public void checkWinnerMultiplayer() {
        GameController gc = new GameController(false);
        GameBoard gb = gc.getInstance();
        RoundController g = new RoundController(gb, gc);
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", true));
        gb.addPlayer(new HumanPlayer("Ron", false));
        gb.addPlayer(new HumanPlayer("Hermione", false));
        gb.init(gb);

        g.init();
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);
        //first Turn
        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));
        g.handle_firstAction(new FirstActionMessage("Ron", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Hermione", 1, 0));

        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);

        g.handle_secondAction(new SecondActionMessage("Ron", r));
        g.handle_secondAction(new SecondActionMessage("Enry", r));
        r.add(Resources.SHIELD);
        g.handle_secondAction(new SecondActionMessage("Hermione", r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_addFaithPoint(25, gb.getPlayer("Enry"));

        assertFalse(g.isWinner());
        assertEquals(g.getWinnerPlayer(), 1);
        g.handle_endTurn();
        g.handle_endTurn();
        g.handle_endTurn();
        g.handle_endTurn();
        assertEquals(g.getTurnState(),TurnState.END);
        assertTrue(g.isWinner());

    }


    @Test
    public void checkWinnerSinglePlayer(){
        GameController gc = new GameController(false);
        GameBoard gb = gc.getInstance();
        gb.addPlayer(new HumanPlayer("Harry",true));
        gb.init(gb);
        RoundController g = new RoundController(gb, gc);
        g.init();
        assertEquals(g.getGameState(), GameState.SINGLEPLAYER);
        //first Turn
        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", 1, 0));
        assertEquals("Harry", g.getPlayerInTurn().getName());

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_addFaithPoint(25, gb.getPlayer("Harry"));

        assertTrue(g.isWinner());
        assertEquals(g.getWinnerPlayer(),0);
        assertEquals(g.getTurnState(),TurnState.END);


    }

    @Test
    public void Handle_useProductionTest() {
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.init(gb);
        RoundController g = new RoundController(gb, null);
        g.init();
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);
        //first Turn

        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));

        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        g.handle_secondAction(new SecondActionMessage("Enry", r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        //adding special productions
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(1, 2, null, null, null, new int[]{0, 0, 0, 1}));
        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(2).flipCard();
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(10, 4, null, null, null, new int[]{0, 0, 1, 0}));
        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(3).flipCard();
        gb.getPlayers().get(0).setPlayerBoard(new DecoratedProductionPlayerBoard(gb.getPlayers().get(0).getPlayerBoard()));
        gb.getPlayers().get(0).getPlayerBoard().addProduction(Resources.SHIELD);
        gb.getPlayers().get(0).getPlayerBoard().addProduction(Resources.STONE);
        //adding Resources
        gb.getPlayers().get(0).getPlayerBoard().addStrongboxResource(Resources.STONE, 100);
        gb.getPlayers().get(0).getPlayerBoard().addStrongboxResource(Resources.SHIELD, 100);
        //adding  production cards
        gb.getPlayers().get(0).getPlayerBoard().addProductionCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{7, 0, 0, 0, 2}, Colors.BLUE, 1), 0);
        gb.getPlayers().get(0).getPlayerBoard().addProductionCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 5, 0, 0, 2}, Colors.BLUE, 1), 1);
        gb.getPlayers().get(0).getPlayerBoard().addProductionCard(new DevelopmentCard(4, 2, new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{7, 0, 0, 0, 2}, Colors.BLUE, 1), 2);
        //normal base production functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 0, 0}));
        g.handle_useBaseProduction(new UseProductionBaseMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 1, 1}, new int[]{0, 0, 0, 0}), Resources.COIN));
        assertEquals(g.getTurnState(), TurnState.PRODUCTION_ACTIONS);
        g.addOutputProductions();
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 1, 0, 0}));
        //redoing base production

        g.handle_useBaseProduction(new UseProductionBaseMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}), Resources.COIN));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);


        //normal  production 1 functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{7, 0, 0, 0}));
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}), 0));
        assertEquals(g.getTurnState(), TurnState.PRODUCTION_ACTIONS);
        g.addOutputProductions();
        assertEquals(2, gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{7, 0, 0, 0}));
        //redoing normal  production 1
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}), 0));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
        //normal  production 2 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 6, 0, 0}));
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}), 1));
        assertEquals(g.getTurnState(), TurnState.PRODUCTION_ACTIONS);
        g.addOutputProductions();
        assertEquals(4, gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 6, 0, 0}));
        //redoing normal production 2
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 1, 0}, new int[]{0, 0, 0, 0}), 1));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
        //normal  production 3 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{14, 0, 0, 0}));
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}), 2));
        assertEquals(g.getTurnState(), TurnState.PRODUCTION_ACTIONS);
        g.addOutputProductions();
        assertEquals(6, gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{14, 0, 0, 0}));
        //redoing base production 3
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 2, 0}, new int[]{0, 0, 0, 0}), 2));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
        //special production 2 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 7, 0, 0}));
        g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 1, 0}, new int[]{0, 0, 0, 0}), Resources.COIN, 3));
        assertEquals(g.getTurnState(), TurnState.PRODUCTION_ACTIONS);
        g.addOutputProductions();
        assertEquals(7, gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 7, 0, 0}));
        //redoing special production 2
        g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 1, 0}, new int[]{0, 0, 0, 0}), Resources.COIN, 3));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);
        //special production 2 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 8, 0, 0}));
        g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}), Resources.COIN, 2));
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        g.addOutputProductions();
        assertEquals(8, gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 8, 0, 0}));
        //redoing special production 2
        g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry", new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 1}, new int[]{0, 0, 0, 0}), Resources.COIN, 2));
        assertEquals(gb.getPlayer("Harry").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);


    }




}


