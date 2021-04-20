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

/**
 * This class handles all possible game actions.
 */
public class ActionController {

    /**
     * Starts a market action by specifying the row or column the user wants to get.
     * @param gameBoard Current game.
     * @param humanPlayer Player that requested the action
     * @param rowIndex Market row index. To obtain a column this must be set to 3.
     * @param colIndex Column index. If rowIndex == 3 this indicates the column, else this number is not considered
     * @return
     */
    public MarketReplyMessage getMarket(GameBoard gameBoard, HumanPlayer humanPlayer, int rowIndex, int colIndex){
        // TODO: Set this action to only modify the model, the model will send updates to the client.
        ArrayList<Resources> list;
        if(rowIndex == 3){
            list = gameBoard.pushColumnMarket(colIndex);
        } else {
            list = gameBoard.pushRowMarket(rowIndex);
        }
        return new MarketReplyMessage(humanPlayer.getName(), list);
    }

    /**
     * Adds a faith point for player {@code humanPlayer}.
     * @param gameBoard Current game.
     * @param humanPlayer Player that needs new faith points.
     * @param number Number of faith points to be added
     */
    public void addFaithPoint(GameBoard gameBoard, HumanPlayer humanPlayer, int number){
        int playerPosition = gameBoard.getPlayers().indexOf(humanPlayer);
        gameBoard.movePlayerFaithPath(playerPosition, number);
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
        int cont = 0;

        for(int i=0;i<4;i++)
            cont += resSpeWar[i]+resStr[i]+resWar[i];
        if(cont != n)
            throw new IllegalResourceException();

        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, resWar, resStr, resSpeWar);

        //pago le varie risorse
        payResources(player, resWar, resStr, resSpeWar);

        //aggiungo le risorse d'output
        player.getPlayerBoard().addStrongboxResource(output,1);
    }

    public void useNormalProduction(HumanPlayer player,int index,int [] resWar,int [] resStr,int [] resSpeWar){
        // check se le risorse mandate dal player sono uguali al costo della carta

        int[] cost = player.getPlayerBoard().getProductionCost(index);
        for(int i=0;i<4;i++)
            if(cost[i]!=(resWar[i]+resSpeWar[i]+resStr[i]))
                throw new InsufficientResourcesException();

        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, resWar, resStr, resSpeWar);

        //pago le varie risorse
        payResources(player, resWar, resStr, resSpeWar);

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

            useBaseProduction(player, 1, output, resWar, resStr, resSpeWar);
    }

    public void getProduction(int color,int level,GameBoard gameBoard,int index,HumanPlayer player,int [] resWar,int [] resStr,int [] resSpeWar){
        DevelopmentCard card=gameBoard.getDeck(color,level).getFirstCard();

        //check se le risorse mandate dal player sono uguali al costo della carta
        for(int i=0;i<4;i++)
            if(card.getCostCard()[i]!=(resWar[i]+resSpeWar[i]+resStr[i]))
                throw new InsufficientResourcesException();

            //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, resWar, resStr, resSpeWar);

        //provo ad aggiungere la carta nel posto che vuole lui o se non specificato nell'unico posto libero
        // Se indice = -1 aggiungi nel primo punto disponibile
        if(index<0)
            player.getPlayerBoard().addProductionCard(card);
        else
            player.getPlayerBoard().addProductionCard(card,index);

        //pago le varie risorse
        payResources(player, resWar, resStr, resSpeWar);

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
                for(int i=0;i<4;i++){
                    if(leaderCard.getEffect1().get(i)!=0){
                        player.getPlayerBoard().addProduction(Resources.transform(i));
                    }
                }
            case SALES:
                // Check if there is another leaderCard of the same type
                if(!(player.getPlayerBoard() instanceof DecoratedCostPlayerBoard)){
                    player.setPlayerBoard(new DecoratedCostPlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for(int i=0;i<4;i++){
                    if(leaderCard.getEffect1().get(i)!=0){
                        player.getPlayerBoard().addDiscount(Resources.transform(i),leaderCard.getEffect1().get(i));
                    }
                }
            case CHANGE:
                // Check if there is another leaderCard of the same type
                if(!(player.getPlayerBoard() instanceof DecoratedChangePlayerBoard)){
                    player.setPlayerBoard(new DecoratedChangePlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for(int i=0;i<4;i++){
                    if(leaderCard.getEffect1().get(i)!=0){
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

    public void firstAction(int index1,int index2, HumanPlayer player){
        PlayerBoard pb=player.getPlayerBoard();
        if (index1==index2)
            throw new IllegalActionException();

        if(index1>index2)
        {
            int n;
            n=index1;
            index1=index2;
            index2=n;
        }
        foldLeader(index2,player);
        foldLeader(index1,player);

    }
    public void foldLeader(int index, HumanPlayer player){
        PlayerBoard pb=player.getPlayerBoard();
        LeaderCard c= pb.getLeaderCard(index);
        if(!c.getUncovered()) {
            pb.removeLeaderCard(c);
        }
        else throw new IllegalActionException();
    }

public void isResourcesAvailable(HumanPlayer player,int [] resWar,int [] resStr,int [] resSpeWar){
         if(!player.getPlayerBoard().checkResourcesSpecialWarehouse(resSpeWar)||!player.getPlayerBoard().checkResourcesStrongbox(resStr)||!player.getPlayerBoard().checkResourcesWarehouse(resWar))
        throw new InsufficientResourcesException();
}
    public void payResources(HumanPlayer player,int [] resWar,int [] resStr,int [] resSpeWar){
        player.getPlayerBoard().payResourcesStrongbox(resStr);
        player.getPlayerBoard().payResourcesSpecialWarehouse(resSpeWar);
        player.getPlayerBoard().payResourcesWarehouse(resWar);
    }

}
