package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.exceptions.playerboard.IllegalResourceException;
import it.polimi.ingsw.exceptions.playerboard.InsufficientLevelException;
import it.polimi.ingsw.exceptions.playerboard.InsufficientResourcesException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.DecoratedWarehousePlayerBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Optional;

public class ActionGetProductionTest {
    @Test
    public void CorrectNumberResources(){
        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        for (int i=0;i<4;i++)
            gameBoard.getDeck(0,1).popFirstCard();

        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,1,2,3},null,null, Colors.BLUE,1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN,1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard=new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,3});
        // Add the corresponding effect
        for(int i=0;i<4;i++){
            if(leaderCard.getEffect1().get(i)!=0){
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
            }}

        player.getPlayerBoard().addExtraResources(Resources.SHIELD,3);


        actionController.getProduction(0, 1, gameBoard,-1, player,new int[]{0, 0, 2, 0}, new int[]{0,1, 0, 0}, new int[]{0, 0, 0, 3});

        assertEquals(0, gameBoard.getDeck(0, 1).DeckLength());
        assertEquals(player.getPlayerBoard().getExtraResources().get(3),(Integer)0);
    }
    @Test
    public void WrongNumberResourcesSent(){
        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        for (int i=0;i<4;i++)
            gameBoard.getDeck(0,1).popFirstCard();

        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,1,2,3},null,null, Colors.BLUE,1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN,1);
        player.getPlayerBoard().addStrongboxResource(Resources.SHIELD,3);
        player.getPlayerBoard().addStrongboxResource(Resources.STONE,2);
        try {
            actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,0,0}, new int[]{0, 1, 0, 3}, new int[]{0, 0, 0, 0});
            fail();
        }catch(InsufficientResourcesException e){
            assertTrue(true);
        }
        assertEquals(1, gameBoard.getDeck(0, 1).DeckLength());

        actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,0,0}, new int[]{0, 1, 2, 3}, new int[]{0, 0, 0, 0});
        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,0,0,0},null,null, Colors.BLUE,2));
        actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,0,0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0});
        assertEquals(0, gameBoard.getDeck(0, 1).DeckLength());
        }
    @Test
    public void WrongNumberResources(){
        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        for (int i=0;i<4;i++)
            gameBoard.getDeck(0,1).popFirstCard();

        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,1,2,3},null,null, Colors.BLUE,1));
        player.getPlayerBoard().addStrongboxResource(Resources.COIN,1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard=new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,3});
        // Add the corresponding effect
        for(int i=0;i<4;i++){
            if(leaderCard.getEffect1().get(i)!=0){
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
            }}

        player.getPlayerBoard().addExtraResources(Resources.SHIELD,3);
    try {
    actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,2,0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3});
    fail();
    }catch(InsufficientResourcesException e){
        assertTrue(true);
    }
        assertEquals(1, gameBoard.getDeck(0, 1).DeckLength());
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,2,0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3});
        assertEquals(0, gameBoard.getDeck(0, 1).DeckLength());
    }



    @Test
    public void WrongLevel(){
        ActionController actionController=new ActionController();
        HumanPlayer player=new HumanPlayer("Harry",true);
        GameBoard gameBoard=new GameBoard();
        gameBoard.addPlayer(player);
        for (int i=0;i<4;i++)
            gameBoard.getDeck(0,1).popFirstCard();

        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,0,0,0},null,null, Colors.BLUE,1));

        gameBoard.getDeck(0,1).addCard(new DevelopmentCard(4,2,new int[]{0,1,2,3},null,null, Colors.BLUE,3));

        actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,0,0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0});
        player.getPlayerBoard().addStrongboxResource(Resources.COIN,1);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.getPlayerBoard().addWarehouseResource(Resources.STONE,2);
        player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
        LeaderCard leaderCard=new LeaderCard(1,2,null,null,null,new int []{0,0,0,1},new int []{0,0,0,3});
        // Add the corresponding effect
        for(int i=0;i<4;i++){
            if(leaderCard.getEffect1().get(i)!=0){
                player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
            }}

        player.getPlayerBoard().addExtraResources(Resources.SHIELD,3);
        try {
            actionController.getProduction(0, 1, gameBoard, -1, player,new int[]{0,0,2,0}, new int[]{0, 1, 0, 0}, new int[]{0, 0, 0, 3});
            fail();
        }catch(InsufficientLevelException e){
            assertTrue(true);
        }


        assertEquals(1, gameBoard.getDeck(0, 1).DeckLength());
    }

}
