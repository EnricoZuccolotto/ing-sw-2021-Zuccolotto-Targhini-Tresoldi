package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.*;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
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

    public void useBaseProduction(HumanPlayer player,int n,Resources output,int [] resWar,int [] resStr,int [] resSpeWar){
        //check se le risorse mandate dal player sono giuste
            if(output.ordinal()>4)
                throw new IllegalResourceException();
            int cont=0;
            for(int i=0;i<4;i++)
                cont+=resSpeWar[i]+resStr[i]+resWar[i];
            if(cont!=n)
                throw new IllegalResourceException();


//controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, resWar, resStr, resSpeWar);


        //pago le varie risorse
        player.getPlayerBoard().payResourcesStrongbox(resStr);
        player.getPlayerBoard().payResourcesSpecialWarehouse(resSpeWar);
        player.getPlayerBoard().payResourcesWarehouse(resWar);

        //aggiungo la risorsa d'output
        player.getPlayerBoard().addStrongboxResource(output,1);


    }
    public void useNormalProduction(HumanPlayer player,int index,int [] resWar,int [] resStr,int [] resSpeWar){
        //check se le risorse mandate dal player sono uguali al costo della carta
        int [] cost=player.getPlayerBoard().getProductionCost(index);
        for(int i=0;i<4;i++)
            if(cost[i]!=(resWar[i]+resSpeWar[i]+resStr[i]))
                throw new InsufficientResourcesException();

        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, resWar, resStr, resSpeWar);

        //pago le varie risorse
        player.getPlayerBoard().payResourcesStrongbox(resStr);
        player.getPlayerBoard().payResourcesSpecialWarehouse(resSpeWar);
        player.getPlayerBoard().payResourcesWarehouse(resWar);

        //aggiungo le risorse
        int [] result=player.getPlayerBoard().getProductionResult(index);
        for(int i=0;i<4;i++)
            player.getPlayerBoard().addStrongboxResource(Resources.transform(i),result[i]);

    }
    public void useSpecialProduction(HumanPlayer player,Resources output ,int index,int [] resWar,int [] resStr,int [] resSpeWar){
        if(!player.getPlayerBoard().getLeaderCard(index).getUncovered())
            throw new IllegalActionException();
        ArrayList<Integer> cost=player.getPlayerBoard().getLeaderCard(index).getEffect1();
        for(int i=0;i<4;i++)
            if(cost.get(i)!=resSpeWar[i]+resStr[i]+resWar[i])
                throw new IllegalResourceException();
            useBaseProduction(player, 2, output, resWar, resStr, resSpeWar);
    }

    public void getProduction(int color,int level,GameBoard gameBoard,Optional<Integer> index,HumanPlayer player,int [] resWar,int [] resStr,int [] resSpeWar){
        DevelopmentCard card=gameBoard.getDeck(color,level).getFirstCard();

        //check se le risorse mandate dal player sono uguali al costo della carta
        for(int i=0;i<4;i++)
            if(card.getCostCard()[i]!=(resWar[i]+resSpeWar[i]+resStr[i]))
                throw new InsufficientResourcesException();

            //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, resWar, resStr, resSpeWar);

        //provo ad aggiungere la carta nel posto che vuole lui o se non specificato nell'unico posto libero
        if(index.isPresent())
            player.getPlayerBoard().addProductionCard(card,index.get());
        else
            player.getPlayerBoard().addProductionCard(card);

        //pago le varie risorse
        player.getPlayerBoard().payResourcesStrongbox(resStr);
        player.getPlayerBoard().payResourcesSpecialWarehouse(resSpeWar);
        player.getPlayerBoard().payResourcesWarehouse(resWar);

        // tolgo la carta
        gameBoard.getDeck(color,level).popFirstCard();
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
                for(int i=0;i<4;i++){
                    if(leaderCard.getEffect1().get(i)!=0){
                        player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect2().get(i));
                    }
                }
            default:
                // Exception
        }
    }

    public void foldLeader(LeaderCard c, HumanPlayer player, int playerNumber){
        PlayerBoard pb=player.getPlayerBoard();
        if(!c.getUncovered()) {
            pb.removeLeaderCard(c);
        }
        else throw new IllegalActionException();
    }

public void isResourcesAvailable(HumanPlayer player,int [] resWar,int [] resStr,int [] resSpeWar){
         if(!player.getPlayerBoard().checkResourcesSpecialWarehouse(resSpeWar)||!player.getPlayerBoard().payResourcesStrongbox(resStr)||!player.getPlayerBoard().checkResourcesWarehouse(resWar))
        throw new InsufficientResourcesException();
}


}
