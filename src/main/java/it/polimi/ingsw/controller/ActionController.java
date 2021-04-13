package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.CardAlreadyUsedException;
import it.polimi.ingsw.exceptions.playerboard.InsufficientColorsException;
import it.polimi.ingsw.exceptions.playerboard.InsufficientResourcesException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.messages.MarketReplyMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.ResourceAckMessage;

import java.util.ArrayList;
import java.util.Optional;


public class ActionController {
    public MarketReplyMessage getMarket(GameBoard gameBoard, HumanPlayer humanPlayer, int rowIndex, int colIndex){
        ArrayList<Resources> list;
        if(rowIndex == 3){
            list = gameBoard.pushColumnMarket(colIndex);
        } else {
            list = gameBoard.pushRowMarket(rowIndex);
        }
        return new MarketReplyMessage(humanPlayer.getName(), list);
    }

    public void addFaithPoint(GameBoard gameBoard, HumanPlayer humanPlayer){
        int playerPosition = gameBoard.getPlayers().indexOf(humanPlayer);
        gameBoard.movePlayerFaithPath(playerPosition, 1);
        // TODO: model sends updates. To be handled in the model.
    }

    public Message addResourceToWarehouse(HumanPlayer player, Resources resource, int rowPosition, int receivedResourceIndex){
        Message reply;
        if(player.getPlayerBoard().addWarehouseResource(resource, rowPosition)){
            reply = new ResourceAckMessage(player.getName(), receivedResourceIndex);
        } else {
            reply = new ErrorMessage(player.getName(), "Invalid warehouse move!");
        }
        return reply;
    }

    public void shiftWarehouseRows(HumanPlayer player, int startingRow, int newRowPosition){
        if(player.getPlayerBoard().shiftWarehouseRows(startingRow, newRowPosition)){
            // Andato a buon fine
            // TODO: Send model updates
        } else {
            // Errore
        }
    }

    public void useProduction(){
    }
    public void getProduction(){
    }
    public void activateLeader(LeaderCard leaderCard, HumanPlayer player){
        // Check if card is already used and owned by the player
        // TODO: Check player leader cards(Color cost and resources cost)
        if(!player.getPlayerBoard().checkColors(leaderCard.getCostColor()))
        {
            throw new InsufficientColorsException();
        }
        if(!player.getPlayerBoard().checkResources(leaderCard.getCostResources()))
        {
            throw new InsufficientResourcesException();
        }
        if(leaderCard.getUncovered()){
            throw new CardAlreadyUsedException();
        }
        leaderCard.flipCard();

        // Add the corresponding advantage. Check if the board is already decorated, if not decorate it and add the effect.
        switch(leaderCard.getAdvantage()){
            case PROD:
                // Check if there is another leaderCard of the same type
                if(!(player.getPlayerBoard() instanceof DecoratedProductionPlayerBoard)){
                    player.setPlayerBoard(new DecoratedProductionPlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for(Integer i : leaderCard.getEffect1()){
                    if(i != 0){
                        player.getPlayerBoard().addProduction(Resources.transform(i));
                    }
                }
            case SALES:
                // Check if there is another leaderCard of the same type
                if(!(player.getPlayerBoard() instanceof DecoratedCostPlayerBoard)){
                    player.setPlayerBoard(new DecoratedCostPlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for(Integer i : leaderCard.getEffect1()){
                    if(i != 0){
                        player.getPlayerBoard().addDiscount(Resources.transform(i), i);
                    }
                }
            case CHANGE:
                // Check if there is another leaderCard of the same type
                if(!(player.getPlayerBoard() instanceof DecoratedChangePlayerBoard)){
                    player.setPlayerBoard(new DecoratedChangePlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for(Integer i : leaderCard.getEffect1()){
                    if(i != 0){
                        player.getPlayerBoard().addSubstitute(Resources.transform(i));
                    }
                }
            case WAREHOUSE:
                // Check if there is another leaderCard of the same type
                if(!(player.getPlayerBoard() instanceof DecoratedWarehousePlayerBoard)){
                    player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for(Integer i : leaderCard.getEffect1()){
                    if(i != 0){
                        player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
                    }
                }
            default:
                // Exception
        }
    }

    public void foldLeader(LeaderCard c, HumanPlayer player, int playerNumber, GameBoard gameBoard){
        PlayerBoard pb=player.getPlayerBoard();
        pb.removeLeaderCard(c);
        gameBoard.movePlayerFaithPath(playerNumber,1);
    }
}
