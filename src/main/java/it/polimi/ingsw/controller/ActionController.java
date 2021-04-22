package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.*;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.ExchangeResources;

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
     * @return List of market-obtained resources
     */
    public ArrayList<Resources> getMarket(GameBoard gameBoard, HumanPlayer humanPlayer, int rowIndex, int colIndex){
        ArrayList<Resources> list;
        if(rowIndex == 3){
            list = gameBoard.pushColumnMarket(colIndex);
        } else {
            list = gameBoard.pushRowMarket(rowIndex);
        }
        return list;
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
    }

    public void addResourceToWarehouse(HumanPlayer player, Resources resource, int rowPosition, int receivedResourceIndex){
        if(player.getTemporaryResourceStorage().get(receivedResourceIndex).equals(resource)){
            if(rowPosition == 0)
                player.getPlayerBoard().addExtraResources(resource, 1);
            else
                player.getPlayerBoard().addWarehouseResource(resource, rowPosition);
            player.removeItemFromTemporaryList(receivedResourceIndex);
            player.sendUpdateToPlayer();
            // TODO: handle errors.
        } else {
            // TODO: Resource not found
        }
    }

    public void shiftWarehouseRows(HumanPlayer player, int startingRow, int newRowPosition){
        if(player.getPlayerBoard().shiftWarehouseRows(startingRow, newRowPosition)){
            player.sendUpdateToPlayer();
        } else {
            // TODO: Error
        }
    }

    public void useBaseProduction(HumanPlayer player, int n, Resources output, ExchangeResources exchangeResources){
        //check se le risorse mandate dal player sono giuste
        int [] resWar= exchangeResources.getWarehouse();
        int [] resStr=exchangeResources.getStrongbox();
        int [] resSpeWar= exchangeResources.getSpecialWarehouse();
            if(output.ordinal()>4)
                throw new IllegalResourceException();
            int cont=0;

        for(int i=0;i<4;i++)
            cont += resSpeWar[i]+resStr[i]+resWar[i];
        if(cont != n)
            throw new IllegalResourceException();


        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, exchangeResources);


        //pago le varie risorse
        payResources(player, exchangeResources);

        //aggiungo le risorse d'output
        player.getPlayerBoard().addStrongboxResource(output, 1);

        //aggiorno la view
        player.sendUpdateToPlayer();
    }


    public void useNormalProduction(HumanPlayer player,int index,ExchangeResources exchangeResources){
        //check se le risorse mandate dal player sono uguali al costo della carta
        int [] resWar= exchangeResources.getWarehouse();
        int [] resStr=exchangeResources.getStrongbox();
        int [] resSpeWar= exchangeResources.getSpecialWarehouse();
        int [] cost=player.getPlayerBoard().getProductionCost(index);
        for(int i=0;i<4;i++)
            if (cost[i] != (resWar[i] + resSpeWar[i] + resStr[i]))
                throw new InsufficientResourcesException();

        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, exchangeResources);

        //pago le varie risorse
        payResources(player, exchangeResources);

        //aggiungo le risorse
        int[] result = player.getPlayerBoard().getProductionResult(index);
        for (int i = 0; i < 4; i++)
            player.getPlayerBoard().addStrongboxResource(Resources.transform(i), result[i]);

        //aggiorno la view
        player.sendUpdateToPlayer();
    }
    public void useSpecialProduction(HumanPlayer player,Resources output ,int index,ExchangeResources exchangeResources) {
        int[] resWar = exchangeResources.getWarehouse();
        int[] resStr = exchangeResources.getStrongbox();
        int[] resSpeWar = exchangeResources.getSpecialWarehouse();
        if (!player.getPlayerBoard().getLeaderCard(index).getUncovered())
            throw new IllegalActionException();
        ArrayList<Integer> cost = player.getPlayerBoard().getLeaderCard(index).getEffect();
        for (int i = 0; i < 4; i++)
            if (cost.get(i) != resSpeWar[i] + resStr[i] + resWar[i])
                throw new IllegalResourceException();

        useBaseProduction(player, 1, output, exchangeResources);
    }

    public void getProduction(Colors color, int level, GameBoard gameBoard, int index, HumanPlayer player, ExchangeResources exchangeResources) {
        int[] resWar = exchangeResources.getWarehouse();
        int[] resStr = exchangeResources.getStrongbox();
        int[] resSpeWar = exchangeResources.getSpecialWarehouse();
        boolean winner = false;
        DevelopmentCard card = gameBoard.getDeck(color, level).getFirstCard();
        if (card == null)
            throw new IllegalActionException();
        //check se le risorse mandate dal player sono uguali al costo della carta
        for (int i = 0; i < 4; i++)
            if (card.getCostCard()[i] != (resWar[i] + resSpeWar[i] + resStr[i]))
                throw new InsufficientResourcesException();

        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        isResourcesAvailable(player, exchangeResources);

        //provo ad aggiungere la carta nel posto che vuole lui o se non specificato nell'unico posto libero
        try {
            if (index < 0)
                player.getPlayerBoard().addProductionCard(card);
            else
                player.getPlayerBoard().addProductionCard(card, index);
        } catch (WinnerException e) {
            winner = true;
        }
        //pago le varie risorse
        payResources(player, exchangeResources);

        // tolgo la carta
        gameBoard.getDeck(color, level).popFirstCard();
        //aggiorno la view
        player.sendUpdateToPlayer();
        if (winner)
            throw new WinnerException();

    }
    public void activateLeader(int index, HumanPlayer player){

        LeaderCard leaderCard=player.getPlayerBoard().getLeaderCard(index);
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
                    if (leaderCard.getEffect().get(i) != 0) {
                        player.getPlayerBoard().addProduction(Resources.transform(i));
                    }
                }
            case SALES:
                // Check if there is another leaderCard of the same type
                if (!(player.getPlayerBoard() instanceof DecoratedCostPlayerBoard)) {
                    player.setPlayerBoard(new DecoratedCostPlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for (int i = 0; i < 4; i++) {
                    if (leaderCard.getEffect().get(i) != 0) {
                        player.getPlayerBoard().addDiscount(Resources.transform(i), leaderCard.getEffect().get(i));
                    }
                }
            case CHANGE:
                // Check if there is another leaderCard of the same type
                if (!(player.getPlayerBoard() instanceof DecoratedChangePlayerBoard)) {
                    player.setPlayerBoard(new DecoratedChangePlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for (int i = 0; i < 4; i++) {
                    if (leaderCard.getEffect().get(i) != 0) {
                        player.getPlayerBoard().addSubstitute(Resources.transform(i));
                    }
                }
            case WAREHOUSE:
                // Check if there is another leaderCard of the same type
                if (!(player.getPlayerBoard() instanceof DecoratedWarehousePlayerBoard)) {
                    player.setPlayerBoard(new DecoratedWarehousePlayerBoard(player.getPlayerBoard()));
                }
                // Add the corresponding effect
                for (int i = 0; i < 4; i++) {
                    if (leaderCard.getEffect().get(i) != 0) {
                        player.getPlayerBoard().addWarehouseSpace(Resources.transform(i), leaderCard.getEffect().get(i));
                    }
                }
            default:
                // Exception
        }
        //aggiorno la view
        player.sendUpdateToPlayer();
    }

    public void firstAction(int index1,int index2, HumanPlayer player){

        if (index1 == index2 || index1 > 3 || index2 > 3 || index1 < 0 || index2 < 0)
            throw new IllegalActionException();

        if (index1 > index2) {
            int n;
            n = index1;
            index1 = index2;
            index2 = n;
        }
        foldLeader(index2, player);
        foldLeader(index1, player);
        //aggiorno la view
        player.sendUpdateToPlayer();
    }
    public void secondAction(ArrayList<Resources> resources,int indexPlayer, HumanPlayer player){
        switch (indexPlayer){
            case 0:
                break;
            case 1:
            case 2: {
                if (resources.size() == 1) {
                    if (resources.get(0).ordinal() >= 4)
                        throw new IllegalResourceException();
                    player.getPlayerBoard().addStrongboxResource(resources.get(0), 1);
                } else throw new IllegalActionException();
                break;
            }
            case 3: {
                if (resources.size() == 2) {
                    if (resources.get(0).ordinal() >= 4 || resources.get(1).ordinal() >= 4)
                        throw new IllegalResourceException();
                    player.getPlayerBoard().addStrongboxResource(resources.get(0), 1);
                    player.getPlayerBoard().addStrongboxResource(resources.get(1), 1);
                } else throw new IllegalActionException();
                break;
            }
        }
        //aggiorno la view
        player.sendUpdateToPlayer();
    }

    public void foldLeader(int index, HumanPlayer player) {
        PlayerBoard pb = player.getPlayerBoard();
        LeaderCard c = pb.getLeaderCard(index);
        if (!c.getUncovered()) {
            pb.removeLeaderCard(c);
        } else throw new IllegalActionException();
        //aggiorno la view
        player.sendUpdateToPlayer();
    }

    public void isResourcesAvailable(HumanPlayer player, ExchangeResources exchangeResources) {
        if (!player.getPlayerBoard().checkResourcesSpecialWarehouse(exchangeResources.getSpecialWarehouse()) || !player.getPlayerBoard().checkResourcesStrongbox(exchangeResources.getStrongbox()) || !player.getPlayerBoard().checkResourcesWarehouse(exchangeResources.getWarehouse()))
            throw new InsufficientResourcesException();
    }

    public void payResources(HumanPlayer player, ExchangeResources exchangeResources) {
        player.getPlayerBoard().payResourcesStrongbox(exchangeResources.getStrongbox());
        player.getPlayerBoard().payResourcesSpecialWarehouse(exchangeResources.getSpecialWarehouse());
        player.getPlayerBoard().payResourcesWarehouse(exchangeResources.getWarehouse());
    }

}
