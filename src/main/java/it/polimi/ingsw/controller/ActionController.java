package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;

import java.util.List;

public class ActionController {
    public void getMarket(GameBoard gameBoard, HumanPlayer humanPlayer, int index){
        List<Resources> list= gameBoard.pushColumnMarket(index);
    }
    public void useProduction(){
    }
    public void getProduction(){
    }
    public void activateLeader(LeaderCard leaderCard, HumanPlayer player){
        // Check if card is already used and owned by the player
        // TODO: Check player leader cards
        if(leaderCard.getUncovered()){
            // Eccezione carta già usata
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
