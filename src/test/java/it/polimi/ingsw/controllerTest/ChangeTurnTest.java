package it.polimi.ingsw.controllerTest;


import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.RoundController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.FirstActionMessage;
import it.polimi.ingsw.network.messages.FoldLeaderMessage;
import it.polimi.ingsw.network.messages.MessageType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeTurnTest {
    @Test
    public void FirstActionIsLeader(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        gb.init(gb);
        RoundController g=new RoundController(gb);
        g.setPlayerInTurn(gb.getPlayers().get(0));
        g.handle_firstTurn();

        assertEquals(g.getTurnState(), TurnState.FIRST_TURN);
        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,2,3));
        assertEquals(g.getTurnState(), TurnState.FIRST_TURN);
        g.handle_firstAction(new FirstActionMessage("Enry", MessageType.FIRST_ACTION,1,0));
        assertEquals("Harry", g.getPlayerInTurn().getName());
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.LD_LEADERACTION)) {
            g.handle_foldLeader(new FoldLeaderMessage("Harry",MessageType.FOLD_LEADER,0));
        }
        HumanPlayer p=g.getPlayerInTurn();
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.STD_GETPRODUCTION))
            g.nextState(Action.STD_GETPRODUCTION);
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.LD_LEADERACTION)) {
            g.nextState(Action.LD_LEADERACTION);
            gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
        }
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.END_TURN)) {
            g.handle_endTurn();
        }
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);

    }
    @Test
    public void FirstActionIsStd(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        RoundController g=new RoundController(gb);
        g.setPlayerInTurn(gb.getPlayers().get(0));
        g.handle_firstTurn();

        assertEquals(g.getTurnState(), TurnState.FIRST_TURN);
        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,2,3));
        assertEquals(g.getTurnState(), TurnState.FIRST_TURN);
        g.handle_firstAction(new FirstActionMessage("Enry", MessageType.FIRST_ACTION,1,0));
        assertEquals("Harry", g.getPlayerInTurn().getName());
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.STD_GETMARKET))
            g.nextState(Action.STD_GETMARKET);
        HumanPlayer p=g.getPlayerInTurn();
        assertEquals(g.getTurnState(), TurnState.WAREHOUSE_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.SORTING_WAREHOUSE))
            g.nextState(Action.SORTING_WAREHOUSE);
        if(TurnState.isPossible(g.getTurnState(),Action.SHIFT_WAREHOUSE))
            g.nextState(Action.SHIFT_WAREHOUSE);
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.LD_LEADERACTION)) {
            g.nextState(Action.LD_LEADERACTION);
            gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
        }
        assertEquals(g.getTurnState(), TurnState.LAST_LEADER_ACTION);
        if(TurnState.isPossible(g.getTurnState(),Action.END_TURN)) {
            g.handle_endTurn();
        }
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);

    }


}
