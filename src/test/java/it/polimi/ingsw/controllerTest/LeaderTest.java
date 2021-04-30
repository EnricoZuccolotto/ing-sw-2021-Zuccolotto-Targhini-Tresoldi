package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.RoundController;
import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.FirstActionMessage;
import it.polimi.ingsw.network.messages.LeaderMessage;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.SecondActionMessage;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderTest {
    @Test
    public void activateSingleLeader(){
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

        PlayerBoard playerBoard = g.getPlayerInTurn().getPlayerBoard();
        playerBoard.removeLeaderCard(playerBoard.getLeaderCard(0));
        playerBoard.removeLeaderCard(playerBoard.getLeaderCard(0));
        int[] zeroCost = new int[]{0, 0, 0, 0};
        int[] discountArray = new int[]{1, 0, 0, 0};
        LeaderCard testCard = new LeaderCard(3, 12, zeroCost, zeroCost, Advantages.SALES, discountArray);
        playerBoard.addLeaderCard(testCard);
        assertEquals(testCard, playerBoard.getLeaderCard(0));

        g.handle_activeLeader(new LeaderMessage("Harry", MessageType.ACTIVE_LEADER, 0));
        // PlayerBoard should be changed.
        playerBoard = g.getPlayerInTurn().getPlayerBoard();
        assertEquals(playerBoard.getResourceDiscount(Resources.SERVANT), 1);
        assertEquals(playerBoard.getResourceDiscount(Resources.COIN), 0);
        assertEquals(playerBoard.getResourceDiscount(Resources.SHIELD), 0);
        assertEquals(playerBoard.getResourceDiscount(Resources.STONE), 0);
        // Testing other decorators
        try{
            playerBoard.addExtraResources(Resources.SERVANT, 1);
            fail();
        } catch(IllegalDecoratorException e){
            assertTrue(true);
        }
        try{
            playerBoard.addSubstitute(Resources.SERVANT);
            fail();
        } catch(IllegalDecoratorException e){
            assertTrue(true);
        }
        assertNull(playerBoard.getProductions(Resources.SERVANT));
    }

    @Test
    public void activateTwoOfTheSameLeaders(){
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

        PlayerBoard playerBoard = g.getPlayerInTurn().getPlayerBoard();
        playerBoard.removeLeaderCard(playerBoard.getLeaderCard(0));
        playerBoard.removeLeaderCard(playerBoard.getLeaderCard(0));
        int[] zeroCost = new int[]{0, 0, 0, 0};
        int[] discountArrayNumberOne = new int[]{1, 0, 0, 0};
        int[] discountArrayNumberTwo = new int[]{0, 0, 4, 0};
        LeaderCard testCardNumberOne = new LeaderCard(3, 12, zeroCost, zeroCost, Advantages.SALES, discountArrayNumberOne);
        LeaderCard testCardNumberTwo = new LeaderCard(3, 12, zeroCost, zeroCost, Advantages.SALES, discountArrayNumberTwo);
        playerBoard.addLeaderCard(testCardNumberOne);
        playerBoard.addLeaderCard(testCardNumberTwo);
        assertEquals(testCardNumberOne, playerBoard.getLeaderCard(0));
        assertEquals(testCardNumberTwo, playerBoard.getLeaderCard(1));
        g.getActionController().activateLeader(0, g.getPlayerInTurn());
        g.getActionController().activateLeader(1, g.getPlayerInTurn());

        // PlayerBoard should be changed.
        playerBoard = g.getPlayerInTurn().getPlayerBoard();
        assertEquals(playerBoard.getResourceDiscount(Resources.SERVANT), 1);
        assertEquals(playerBoard.getResourceDiscount(Resources.COIN), 0);
        assertEquals(playerBoard.getResourceDiscount(Resources.STONE), 4);
        assertEquals(playerBoard.getResourceDiscount(Resources.SHIELD), 0);

        // Testing other decorators
        try{
            playerBoard.addExtraResources(Resources.SERVANT, 1);
            fail();
        } catch(IllegalDecoratorException e){
            assertTrue(true);
        }
        try{
            playerBoard.addSubstitute(Resources.SERVANT);
            fail();
        } catch(IllegalDecoratorException e){
            assertTrue(true);
        }
        assertNull(playerBoard.getProductions(Resources.SERVANT));
    }

    @Test
    public void activateTwoDifferentLeaders(){
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

        PlayerBoard playerBoard = g.getPlayerInTurn().getPlayerBoard();
        playerBoard.removeLeaderCard(playerBoard.getLeaderCard(0));
        playerBoard.removeLeaderCard(playerBoard.getLeaderCard(0));
        int[] zeroCost = new int[]{0, 0, 0, 0};
        int[] discountArrayNumberOne = new int[]{1, 0, 0, 0};
        int[] discountArrayNumberTwo = new int[]{0, 0, 4, 0};
        LeaderCard testCardNumberOne = new LeaderCard(3, 12, zeroCost, zeroCost, Advantages.SALES, discountArrayNumberOne);
        LeaderCard testCardNumberTwo = new LeaderCard(3, 12, zeroCost, zeroCost, Advantages.WAREHOUSE, discountArrayNumberTwo);
        playerBoard.addLeaderCard(testCardNumberOne);
        playerBoard.addLeaderCard(testCardNumberTwo);
        assertEquals(testCardNumberOne, playerBoard.getLeaderCard(0));
        assertEquals(testCardNumberTwo, playerBoard.getLeaderCard(1));
        g.getActionController().activateLeader(0, g.getPlayerInTurn());
        g.getActionController().activateLeader(1, g.getPlayerInTurn());

        // PlayerBoard should be changed.
        playerBoard = g.getPlayerInTurn().getPlayerBoard();
        assertEquals(playerBoard.getResourceDiscount(Resources.SERVANT), 1);
        assertEquals(playerBoard.getResourceDiscount(Resources.COIN), 0);
        assertEquals(playerBoard.getResourceDiscount(Resources.STONE), 0);
        assertEquals(playerBoard.getResourceDiscount(Resources.SHIELD), 0);
        playerBoard.addExtraResources(Resources.STONE, 3);

        // Testing other decorators
        assertFalse(playerBoard.addExtraResources(Resources.SERVANT, 5));
        assertFalse(playerBoard.addExtraResources(Resources.COIN, 5));
        assertFalse(playerBoard.addExtraResources(Resources.SHIELD, 5));
        assertFalse(playerBoard.addExtraResources(Resources.STONE, 5));

        try{
            playerBoard.addSubstitute(Resources.SERVANT);
            fail();
        } catch(IllegalDecoratorException e){
            assertTrue(true);
        }

        assertNull(playerBoard.getProductions(Resources.SERVANT));
    }
}
