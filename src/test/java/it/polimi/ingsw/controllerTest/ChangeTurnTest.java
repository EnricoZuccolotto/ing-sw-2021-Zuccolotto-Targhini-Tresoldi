package it.polimi.ingsw.controllerTest;


import it.polimi.ingsw.controller.RoundController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeTurnTest {
    @Test
    public void FirstActionIsLeader(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        RoundController g=new RoundController(gb);
        g.setPlayerinTurn(gb.getPlayers().get(0));
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(1,1,null,null,null,null,null));

        g.handle_activeLeader();
        HumanPlayer p=g.getPlayerInTurn();
        assertEquals(g.getTurnstate(), TurnState.NORMAL_ACTION);
        g.handle_getProduction();
        assertEquals(g.getTurnstate(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);
    }
    @Test
    public void FirstActionIsStd(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        RoundController g=new RoundController(gb);
        g.setPlayerinTurn(gb.getPlayers().get(0));
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(1,1,null,null,null,null,null));
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(2,1,null,null,null,null,null));
        assertEquals(g.getTurnstate(), TurnState.FIRST_LEADER_ACTION);
        g.handle_getMarket();
        HumanPlayer p=g.getPlayerInTurn();
        assertEquals(g.getTurnstate(), TurnState.WAREHOUSE_ACTION);
        g.handle_sortingWarehouse();
        g.handle_shiftWarehouse();
        g.handle_activeLeader();
        assertEquals(g.getTurnstate(), TurnState.LAST_LEADER_ACTION);
        g.handle_foldLeader();
        assertEquals(g.getTurnstate(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);
        try{
            g.handle_activeLeader();
            fail();
        }
        catch ( IllegalActionException e){
            assertTrue(true);
        }
    }
    @Test
    public void FirstActionIsLeaderNoCards() {
        GameBoard gb = new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry", false));
        gb.addPlayer(new HumanPlayer("Enry", false));
        RoundController g = new RoundController(gb);
        g.setPlayerinTurn(gb.getPlayers().get(0));
        assertEquals(g.getTurnstate(), TurnState.FIRST_LEADER_ACTION);
        try{
            g.handle_activeLeader();
            fail();
        }
        catch ( IllegalActionException e){
            assertTrue(true);
        }
        HumanPlayer p = g.getPlayerInTurn();
        assertEquals(g.getTurnstate(), TurnState.NORMAL_ACTION);
        g.handle_endTurn();
        assertEquals(g.getTurnstate(), TurnState.FIRST_LEADER_ACTION);
        assertNotEquals(g.getPlayerInTurn(), p);
    }
}
