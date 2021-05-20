package it.polimi.ingsw.controllerTest;


import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.RoundController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.FirstActionMessage;
import it.polimi.ingsw.network.messages.LeaderMessage;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.SecondActionMessage;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChangeTurnTest {
    @Test
    public void FirstTurnTest(){
        ArrayList<LeaderCard> expected=new ArrayList<>();
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.init(gb);
        RoundController g = new RoundController(gb);
        g.init();
        g.handle_firstTurn();
        expected.add(gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0));
        expected.add(gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(1));
        expected.add(gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(2));
        expected.add(gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(3));
        assertEquals(gb.getPlayers().get(0).getPlayerBoard().getLeaderCardsNumber(), 4);
        assertEquals(gb.getPlayers().get(1).getPlayerBoard().getLeaderCardsNumber(), 4);
        assertEquals(g.getTurnState(), TurnState.FIRST_TURN);
        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        assertEquals(gb.getPlayers().get(0).getPlayerBoard().getLeaderCardsNumber(), 2);
        expected.remove(3);
        expected.remove(2);
        assertEquals(expected.get(0), gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0));
        assertEquals(expected.get(1), gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(1));
        assertEquals(g.getTurnState(), TurnState.FIRST_TURN);
        expected.clear();
        expected.add(gb.getPlayers().get(1).getPlayerBoard().getLeaderCard(0));
        expected.add(gb.getPlayers().get(1).getPlayerBoard().getLeaderCard(1));
        expected.add(gb.getPlayers().get(1).getPlayerBoard().getLeaderCard(2));
        expected.add(gb.getPlayers().get(1).getPlayerBoard().getLeaderCard(3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));
        assertEquals(2, gb.getPlayers().get(1).getPlayerBoard().getLeaderCardsNumber());
        expected.remove(1);
        expected.remove(0);
        assertEquals(expected.get(0), gb.getPlayers().get(1).getPlayerBoard().getLeaderCard(0));
        assertEquals(expected.get(1), gb.getPlayers().get(1).getPlayerBoard().getLeaderCard(1));
        assertEquals("Harry", g.getPlayerInTurn().getName());

    }

    @Test
    public void SecondTurnTest() {
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.addPlayer(new HumanPlayer("Ron", false));
        gb.addPlayer(new HumanPlayer("Hermione", false));
        gb.init(gb);
        RoundController g = new RoundController(gb);
        g.init();
        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));
        g.handle_firstAction(new FirstActionMessage("Ron", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Hermione", 1, 0));
        assertEquals(g.getTurnState(), TurnState.SECOND_TURN);
        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);

        g.handle_secondAction(new SecondActionMessage("Hermione", r));
        assertEquals(gb.getPlayer("Hermione").getPrivateCommunication().getCommunicationMessage(), CommunicationMessage.ILLEGAL_ACTION);

        g.handle_secondAction(new SecondActionMessage("Ron", r));
        assertTrue(gb.getPlayers().get(2).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 1, 0}));
        g.handle_secondAction(new SecondActionMessage("Enry", r));
        assertTrue(gb.getPlayers().get(1).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 1, 0}));
        assertEquals(1, gb.getPlayerFaithPathPosition(2));
        r.add(Resources.SHIELD);
        g.handle_secondAction(new SecondActionMessage("Hermione", r));
        assertTrue(gb.getPlayers().get(3).getPlayerBoard().checkResourcesStrongbox(new int[]{0, 0, 1, 1}));
        assertEquals(1, gb.getPlayerFaithPathPosition(3));
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        assertEquals(g.getPlayerInTurn().getName(), "Harry");
    }

    @Test
    public void FirstActionIsLeader() {
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.init(gb);
        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        RoundController g = new RoundController(gb);
        g.init();
        g.handle_firstTurn();

        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));

        g.handle_secondAction(new SecondActionMessage("Enry", r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.ACTIVE_LEADER)) {
            g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 0));
        }
        HumanPlayer p = g.getPlayerInTurn();
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.BUY_DEVELOPMENT_CARD))
            g.nextState(Action.BUY_DEVELOPMENT_CARD);
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.ACTIVE_LEADER)) {
            g.nextState(Action.ACTIVE_LEADER);
            gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
        }
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.END_TURN)) {
            g.handle_endTurn();
        }
        System.out.println(gb.getPlayers().get(0).getPlayerBoard().getLeaderCardsNumber());
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);

    }

    @Test
    public void FirstActionIsStd() {
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        gb.init(gb);
        ArrayList<Resources> r = new ArrayList<>();
        r.add(Resources.STONE);
        RoundController g = new RoundController(gb);
        g.init();
        g.handle_firstTurn();

        g.handle_firstAction(new FirstActionMessage("Harry", 2, 3));
        g.handle_firstAction(new FirstActionMessage("Enry", 1, 0));

        g.handle_secondAction(new SecondActionMessage("Enry", r));

        assertEquals("Harry", g.getPlayerInTurn().getName());
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.GET_RESOURCES_FROM_MARKET))
            g.nextState(Action.GET_RESOURCES_FROM_MARKET);
        HumanPlayer p = g.getPlayerInTurn();
        assertEquals(g.getTurnState(), TurnState.WAREHOUSE_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.SORTING_WAREHOUSE))
            g.nextState(Action.SORTING_WAREHOUSE);
        if (TurnState.isPossible(g.getTurnState(), Action.SHIFT_WAREHOUSE))
            g.nextState(Action.SHIFT_WAREHOUSE);
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if (TurnState.isPossible(g.getTurnState(), Action.ACTIVE_LEADER)) {
            gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
            g.nextState(Action.ACTIVE_LEADER);
        }
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.END_TURN)) {
            g.handle_endTurn();
        }
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);

    }


}
