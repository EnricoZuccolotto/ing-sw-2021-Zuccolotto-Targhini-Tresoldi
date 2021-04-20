package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.controller.RoundController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.exceptions.playerboard.CardAlreadyUsedException;
import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.DecoratedProductionPlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
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
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        gb.init(gb);
        RoundController g=new RoundController(gb);
        g.init(gb.getPlayers().get(0));
        g.handle_firstTurn();
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);

        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,2,3));
        g.handle_firstAction(new FirstActionMessage("Enry", MessageType.FIRST_ACTION,1,0));

        ArrayList<Resources> r=new ArrayList<>();
        r.add(Resources.STONE);
        g.handle_secondAction(new SecondActionMessage("Enry",MessageType.SECOND_ACTION,r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        try {
            g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 4));
            fail();
        }catch (IndexOutOfBoundsException e){
            assertEquals(0,0);
        }
        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(0).flipCard();
        g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 1));
        assertEquals(gb.getPlayers().get(0).getPlayerBoard().getLeaderCardsNumber(),0);
        assertEquals(gb.getPlayerFaithPathPosition(0),1);
        assertEquals(g.getTurnState(),TurnState.NORMAL_ACTION);
        try {
            g.handle_foldLeader(new LeaderMessage("Harry", MessageType.FOLD_LEADER, 0));
            fail();
        }catch (IllegalActionException e){
            assertEquals(0,0);
        }
        assertEquals(g.getTurnState(),TurnState.NORMAL_ACTION);
    }
    @Test
    public void Handle_addFaithPointsTest(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        gb.init(gb);
        RoundController g=new RoundController(gb);
        g.init(gb.getPlayers().get(0));
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);

        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,2,3));
        g.handle_firstAction(new FirstActionMessage("Enry", MessageType.FIRST_ACTION,1,0));

        ArrayList<Resources> r=new ArrayList<>();
        r.add(Resources.STONE);
        g.handle_secondAction(new SecondActionMessage("Enry",MessageType.SECOND_ACTION,r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        g.handle_addFaithPoint(12);
        assertEquals(gb.getPlayerFaithPathPosition(0),12);
        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_addFaithPoint(123);
        assertEquals(g.getWinnerPlayer(),0);

    }
    @Test
    public void checkWinnerMultiplayer(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",true));
        gb.addPlayer(new HumanPlayer("Ron",false));
        gb.addPlayer(new HumanPlayer("Hermione",false));
        gb.init(gb);
        RoundController g=new RoundController(gb);
        g.init(gb.getPlayers().get(1));
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);
        //first Turn
        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,2,3));
        g.handle_firstAction(new FirstActionMessage("Enry", MessageType.FIRST_ACTION,1,0));
        g.handle_firstAction(new FirstActionMessage("Ron", MessageType.FIRST_ACTION,2,3));
        g.handle_firstAction(new FirstActionMessage("Hermione", MessageType.FIRST_ACTION,1,0));

        ArrayList<Resources> r=new ArrayList<>();
        r.add(Resources.STONE);

        g.handle_secondAction(new SecondActionMessage("Ron",MessageType.SECOND_ACTION,r));
        g.handle_secondAction(new SecondActionMessage("Enry",MessageType.SECOND_ACTION,r));
        r.add(Resources.SHIELD);
        g.handle_secondAction(new SecondActionMessage("Hermione",MessageType.SECOND_ACTION,r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_addFaithPoint(25);

        assertFalse(g.isWinner());
        assertEquals(g.getWinnerPlayer(),1);
        g.handle_endTurn();
        g.handle_endTurn();
        g.handle_endTurn();
        g.handle_endTurn();
        assertEquals(g.getTurnState(),TurnState.END);
        assertTrue(g.isWinner());

    }


    @Test
    public void checkWinnerSinglePlayer(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",true));
        gb.init(gb);
        RoundController g=new RoundController(gb);
        g.init(gb.getPlayers().get(0));
        assertEquals(g.getGameState(), GameState.SINGLEPLAYER);
        //first Turn
        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,1,0));
        assertEquals("Harry", g.getPlayerInTurn().getName());

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);

        g.handle_addFaithPoint(25);

        assertTrue(g.isWinner());
        assertEquals(g.getWinnerPlayer(),0);
        assertEquals(g.getTurnState(),TurnState.END);


    }

    @Test
    public void Handle_useProductionTest(){
        GameBoard gb=new GameBoard();
        gb.addPlayer(new HumanPlayer("Harry",false));
        gb.addPlayer(new HumanPlayer("Enry",false));
        gb.init(gb);
        RoundController g=new RoundController(gb);
        g.init(gb.getPlayers().get(0));
        assertEquals(g.getGameState(), GameState.MULTIPLAYER);
        //first Turn

        g.handle_firstTurn();
        g.handle_firstAction(new FirstActionMessage("Harry", MessageType.FIRST_ACTION,2,3));
        g.handle_firstAction(new FirstActionMessage("Enry", MessageType.FIRST_ACTION,1,0));

        ArrayList<Resources> r=new ArrayList<>();
        r.add(Resources.STONE);
        g.handle_secondAction(new SecondActionMessage("Enry",MessageType.SECOND_ACTION,r));

        assertEquals(g.getTurnState(), TurnState.FIRST_LEADER_ACTION);
        //adding special productions
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,0}));
        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(2).flipCard();
        gb.getPlayers().get(0).getPlayerBoard().addLeaderCard(new LeaderCard(10,4,null,null,null,new int []{0,0,1,0},new int []{0,0,0,0}));
        gb.getPlayers().get(0).getPlayerBoard().getLeaderCard(3).flipCard();
        gb.getPlayers().get(0).setPlayerBoard(new DecoratedProductionPlayerBoard(gb.getPlayers().get(0).getPlayerBoard()));
        gb.getPlayers().get(0).getPlayerBoard().addProduction(Resources.SHIELD);
        gb.getPlayers().get(0).getPlayerBoard().addProduction(Resources.STONE);
        //adding Resources
        gb.getPlayers().get(0).getPlayerBoard().addStrongboxResource(Resources.STONE,100);
        gb.getPlayers().get(0).getPlayerBoard().addStrongboxResource(Resources.SHIELD,100);
        //adding  production cards
        gb.getPlayers().get(0).getPlayerBoard().addProductionCard(new DevelopmentCard(4,2,new int[]{0,0,0,0},new int[]{0,0,2,0},new int[]{7,0,0,0,2}, Colors.BLUE,1));
        gb.getPlayers().get(0).getPlayerBoard().addProductionCard(new DevelopmentCard(4,2,new int[]{0,0,0,0},new int[]{0,0,2,0},new int[]{0,5,0,0,2}, Colors.BLUE,1));
        gb.getPlayers().get(0).getPlayerBoard().addProductionCard(new DevelopmentCard(4,2,new int[]{0,0,0,0},new int[]{0,0,2,0},new int[]{7,0,0,0,2}, Colors.BLUE,1));
        //normal base production functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,1,0,0}));
        g.handle_useBaseProduction(new UseProductionBaseMessage("Harry",MessageType.USE_BASE_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 1, 1}, new int[]{0, 0, 0, 0}), Resources.COIN));
        assertEquals(g.getTurnState(),TurnState.PRODUCTION_ACTIONS);
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,1,0,0}));
        //redoing base production
        try {
            g.handle_useBaseProduction(new UseProductionBaseMessage("Harry",MessageType.USE_BASE_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 2, 0}, new int[]{0, 0, 0, 0}), Resources.COIN));
            fail();
        }catch (CardAlreadyUsedException e){
                assertEquals(0,0);
        }

        //normal  production 1 functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{7,0,0,0}));
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry",MessageType.USE_NORMAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 2, 0}, new int[]{0, 0, 0, 0}),0));
        assertEquals(g.getTurnState(),TurnState.PRODUCTION_ACTIONS);
        assertEquals(2,gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{7,0,0,0}));
        //redoing normal  production 1
        try {
            g.handle_useNormalProduction(new UseProductionNormalMessage("Harry",MessageType.USE_NORMAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 2, 0}, new int[]{0, 0, 0, 0}),0));
            fail();
        }catch (CardAlreadyUsedException e){
            assertEquals(0,0);
        }
        //normal  production 2 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,6,0,0}));
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry",MessageType.USE_NORMAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 2, 0}, new int[]{0, 0, 0, 0}),1));
        assertEquals(g.getTurnState(),TurnState.PRODUCTION_ACTIONS);
        assertEquals(4,gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,6,0,0}));
        //redoing normal production 2
        try {
            g.handle_useNormalProduction(new UseProductionNormalMessage("Harry",MessageType.USE_NORMAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 1, 0}, new int[]{0, 0, 0, 0}),1));
            fail();
        }catch (CardAlreadyUsedException e){
            assertEquals(0,0);
        }
        //normal  production 3 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{14,0,0,0}));
        g.handle_useNormalProduction(new UseProductionNormalMessage("Harry",MessageType.USE_NORMAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 2, 0}, new int[]{0, 0, 0, 0}),2));
        assertEquals(g.getTurnState(),TurnState.PRODUCTION_ACTIONS);
        assertEquals(6,gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{14,0,0,0}));
        //redoing base production 3
        try {
            g.handle_useNormalProduction(new UseProductionNormalMessage("Harry",MessageType.USE_NORMAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 2, 0}, new int[]{0, 0, 0, 0}),2));
            fail();
        }catch (CardAlreadyUsedException e){
            assertEquals(0,0);
        }
        //special production 2 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,7,0,0}));
        g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry",MessageType.USE_SPECIAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 1, 0}, new int[]{0, 0, 0, 0}),Resources.COIN,3));
        assertEquals(g.getTurnState(),TurnState.PRODUCTION_ACTIONS);
        assertEquals(7,gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,7,0,0}));
        //redoing special production 2
        try {
            g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry",MessageType.USE_SPECIAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 1, 0}, new int[]{0, 0, 0, 0}),Resources.COIN,3));
            fail();
        }catch (CardAlreadyUsedException e){
            assertEquals(0,0);
        }
        //special production 2 normal functioning
        assertFalse(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,8,0,0}));
        g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry",MessageType.USE_SPECIAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 0, 1}, new int[]{0, 0, 0, 0}),Resources.COIN,2));
        assertEquals(g.getTurnState(),TurnState.LAST_LEADER_ACTION);
        assertEquals(8,gb.getPlayerFaithPathPosition(0));
        assertTrue(gb.getPlayers().get(0).getPlayerBoard().checkResourcesStrongbox(new int[]{0,8,0,0}));
        //redoing special production 2
        try {
            g.handle_useSpecialProduction(new UseProductionSpecialMessage("Harry",MessageType.USE_SPECIAL_PRODUCTION,new ExchangeResources(new int[]{0, 0, 0, 0}, new int[]{0,0, 0, 1}, new int[]{0, 0, 0, 0}),Resources.COIN,2));
            fail();
        }catch (CardAlreadyUsedException e){
            assertEquals(0,0);
        }



    }

}
