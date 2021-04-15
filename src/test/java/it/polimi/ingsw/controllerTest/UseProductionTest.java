package it.polimi.ingsw.controllerTest;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.exceptions.playerboard.InsufficientResourcesException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.DecoratedWarehousePlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class UseProductionTest {
    @Test
    public void BaseProduction(){

        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard=new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,3});
        // Add the corresponding effect
        for(int i=0;i<4;i++){
            if(leaderCard.getEffect1().get(i)!=0){
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
            }}

        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addExtraResources(Resources.SHIELD,3);
//wrong number of resources given
    try {
    actionController.useBaseProduction(player,2,Resources.COIN,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});
    fail();
    }catch (IllegalResourceException e){
        assertTrue (true);
    }
    //wrong resource given
        try {
            actionController.useBaseProduction(player,2,Resources.FAITH,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch (IllegalResourceException e){
            assertTrue (true);
        }
        //normal functioning
        actionController.useBaseProduction(player,2,Resources.SHIELD,new int[]{0, 0, 1, 0}, new int[]{0,0, 0, 0}, new int[]{0, 0, 0, 1});

         assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{0,0,0,1}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(3),(Integer)2);

    }
    @Test
    public void SpecialProduction(){

        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard=new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,3});
        // Add the corresponding effect
        for(int i=0;i<4;i++){
            if(leaderCard.getEffect1().get(i)!=0){
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
            }}
        player.getPlayerBoard().addLeaderCard(new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,3}));
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addExtraResources(Resources.SHIELD,3);
        // card not active
        try {
            actionController.useSpecialProduction(player,Resources.COIN,0,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch (IllegalActionException e){
            assertTrue (true);
        }
        player.getPlayerBoard().getLeaderCard(0).flipCard();
    //wrong number of resources given
        try {
            actionController.useSpecialProduction(player,Resources.COIN,0,new int[]{0, 0, 0, 0}, new int[]{0,0, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch (IllegalResourceException e){
            assertTrue (true);
        }
        //wrong resource given
        try {
            actionController.useSpecialProduction(player,Resources.FAITH,0,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch (IllegalResourceException e){
            assertTrue (true);
        }
        //normal functioning
        actionController.useSpecialProduction(player,Resources.COIN,0,new int[]{0, 0, 0, 0}, new int[]{0,0, 0, 0}, new int[]{0, 0, 0, 1});

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{0,1,0,0}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(3),(Integer)2);

    }
    @Test
    public void NormalProduction(){

        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        for (int i=0;i<4;i++)
            gameBoard.getDeck(0,1).popFirstCard();

        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,0,0,0},new int[]{0,1,2,0},new int[]{7,5,2,0,0,2}, Colors.BLUE,1));
        actionController.getProduction(0,1,gameBoard,-1,player,new int[]{0,0,0,0},new int[]{0,0,0,0},new int[]{0,0,0,0,0,25});
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard=new LeaderCard(1,2,null,null,null,new int []{0,1,0,0},new int []{0,5,0,0});
        // Add the corresponding effect
        for(int i=0;i<4;i++){
            if(leaderCard.getEffect1().get(i)!=0){
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
            }}


        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addExtraResources(Resources.COIN,4);
        //wrong index given
        try {
            actionController.useNormalProduction(player,2,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch (IndexOutOfBoundsException e){
            assertTrue (true);
        }
        //wrong number of resources given
        try {
            actionController.useNormalProduction(player,0,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch (InsufficientResourcesException e){
            assertTrue (true);
        }
        //correct resources but not enough int the warehouse
        try {
            actionController.useNormalProduction(player,0,new int[]{0, 0, 2, 0}, new int[]{0,0, 0, 0}, new int[]{0, 1, 0, 0});
            fail();
        }catch (InsufficientResourcesException e){
            assertTrue (true);
        }
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        //normal functioning
        actionController.useNormalProduction(player,0,new int[]{0, 0, 2, 0}, new int[]{0,0, 0, 0}, new int[]{0, 1, 0, 0});

        assertTrue(player.getPlayerBoard().checkResourcesStrongbox(new int[]{7,5,2,0}));
        assertEquals(player.getPlayerBoard().getExtraResources().get(1),(Integer)3);

    }


}
