package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.InsufficientLevelException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
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
        if (humanPlayer != null) {
            int playerPosition = gameBoard.getPlayers().indexOf(humanPlayer);
            gameBoard.movePlayerFaithPath(playerPosition, number);
        } else gameBoard.movePlayerFaithPath(1, number);
    }

    public void addResourceToWarehouse(HumanPlayer player, Resources resource, int rowPosition, int receivedResourceIndex){
        Resources item = player.getTemporaryResourceStorage().get(receivedResourceIndex);
        if (item.equals(resource) || (item.equals(Resources.WHITE) && player.getPlayerBoard().isResourceSubstitutable(resource))) {
            if (rowPosition == 0)
                player.getPlayerBoard().addExtraResources(resource, 1);
            else
                player.getPlayerBoard().addWarehouseResource(resource, rowPosition);
            player.removeItemFromTemporaryList(receivedResourceIndex);
            player.sendUpdateToPlayer();
            // TODO: handle errors.
        } else {
            player.setPrivateCommunication("Resource not found", CommunicationMessage.INSUFFICIENT_RESOURCES);
        }
    }

    public void shiftWarehouseRows(HumanPlayer player, int startingRow, int newRowPosition){
        if(player.getPlayerBoard().shiftWarehouseRows(startingRow, newRowPosition)){
            player.sendUpdateToPlayer();
        } else {
            player.setPrivateCommunication("You cannot switch these rows", CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    public boolean useBaseProduction(HumanPlayer player, int n, Resources output, ExchangeResources exchangeResources) {
        //check se le risorse mandate dal player sono giuste
        int[] resWar = exchangeResources.getWarehouse();
        int[] resStr = exchangeResources.getStrongbox();
        int[] resSpeWar = exchangeResources.getSpecialWarehouse();
        if (output.ordinal() >= 4) {
            player.setPrivateCommunication("The resource you sent is not SHIELD,STONE,SERVANT,COIN.", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }

        int cont = 0;
        for (int i = 0; i < 4; i++)
            cont += resSpeWar[i] + resStr[i] + resWar[i];
        if (cont != n) {
            player.setPrivateCommunication("You sent the wrong number of resources", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }


        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        if (isResourcesAvailable(player, exchangeResources, true)) {

            //pago le varie risorse
            payResources(player, exchangeResources);

            //aggiungo le risorse d'output
            player.getPlayerBoard().addStrongboxResource(output, 1);
        } else return false;
        //aggiorno la view
        player.sendUpdateToPlayer();
        return true;
    }


    public boolean useNormalProduction(HumanPlayer player, int index, ExchangeResources exchangeResources) {
        //check se le risorse mandate dal player sono uguali al costo della carta
        int[] resWar = exchangeResources.getWarehouse();
        int[] resStr = exchangeResources.getStrongbox();
        int[] resSpeWar = exchangeResources.getSpecialWarehouse();
        int[] cost;
        try {
            cost = player.getPlayerBoard().getProductionCost(index);
        } catch (IndexOutOfBoundsException e) {
            player.setPrivateCommunication("You don't have a card in this position", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }

        for (int i = 0; i < 4; i++)
            if (cost[i] != (resWar[i] + resSpeWar[i] + resStr[i])) {
                player.setPrivateCommunication("That's not the cost of the card, you have sent the wrong number of resources", CommunicationMessage.INSUFFICIENT_RESOURCES);
                return false;
            }

        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        if (isResourcesAvailable(player, exchangeResources, true)) {


            //pago le varie risorse
            payResources(player, exchangeResources);

            //aggiungo le risorse
            int[] result = player.getPlayerBoard().getProductionResult(index);
            for (int i = 0; i < 4; i++)
                player.getPlayerBoard().addStrongboxResource(Resources.transform(i), result[i]);
        } else return false;
        //aggiorno la view
        player.sendUpdateToPlayer();
        return true;
    }

    public boolean useSpecialProduction(HumanPlayer player, Resources output, int index, ExchangeResources exchangeResources) {
        int[] resWar = exchangeResources.getWarehouse();
        int[] resStr = exchangeResources.getStrongbox();
        int[] resSpeWar = exchangeResources.getSpecialWarehouse();
        if (!player.getPlayerBoard().getLeaderCard(index).getUncovered()) {
            player.setPrivateCommunication("You need to activate this card to use it", CommunicationMessage.CARD_ALREADY_USED);
            return false;
        }
        ArrayList<Integer> cost = player.getPlayerBoard().getLeaderCard(index).getEffect();
        for (int i = 0; i < 4; i++)
            if (cost.get(i) != resSpeWar[i] + resStr[i] + resWar[i]) {
                player.setPrivateCommunication("You sent the wrong number of resources", CommunicationMessage.ILLEGAL_ACTION);
                return false;
            }

        return useBaseProduction(player, 1, output, exchangeResources);
    }

    public boolean getProduction(Colors color, int level, GameBoard gameBoard, int index, HumanPlayer player, ExchangeResources exchangeResources) {
        int[] resWar = exchangeResources.getWarehouse();
        int[] resStr = exchangeResources.getStrongbox();
        int[] resSpeWar = exchangeResources.getSpecialWarehouse();
        boolean winner = false;
        DevelopmentCard card = gameBoard.getDeck(color, level).getFirstCard();

        if (card == null) {
            player.setPrivateCommunication("This deck is empty", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }
        //check se le risorse mandate dal player sono uguali al costo della carta
        for (int i = 0; i < 4; i++)
            if (card.getCostCard()[i] != (resWar[i] + resSpeWar[i] + resStr[i])) {
                player.setPrivateCommunication("The resources sent are different from the cost of the card", CommunicationMessage.ILLEGAL_ACTION);
                return false;
            }
        //controllo se le risorse ho abbastanza risorse nei magazzini rispetto alle divisioni mandate
        if (isResourcesAvailable(player, exchangeResources, false)) {

            //provo ad aggiungere la carta nel posto che vuole lui o se non specificato nell'unico posto libero
            try {
                if (index < 0)
                    player.getPlayerBoard().addProductionCard(card);
                else
                    player.getPlayerBoard().addProductionCard(card, index);
            } catch (WinnerException e) {
                winner = true;
            } catch (InsufficientLevelException | IndexOutOfBoundsException e) {
                player.setPrivateCommunication("You don't have a space available", CommunicationMessage.ILLEGAL_ACTION);
                return false;
            }
            //pago le varie risorse
            payResources(player, exchangeResources);

            // tolgo la carta
            gameBoard.getDeck(color, level).popFirstCard();

            //aggiorno la view
            player.sendUpdateToPlayer();

            if (winner)
                throw new WinnerException();
        } else return false;


        return true;
    }

    public boolean activateLeader(int index, HumanPlayer player) {

        LeaderCard leaderCard = player.getPlayerBoard().getLeaderCard(index);

        if (!player.getPlayerBoard().checkColors(leaderCard.getCostColor())) {
            player.setPrivateCommunication("You don't have enough colors to activate this card", CommunicationMessage.INSUFFICIENT_RESOURCES);
            return false;
        }
        if (!player.getPlayerBoard().checkResources(leaderCard.getCostResources())) {
            player.setPrivateCommunication("You don't have enough resources to activate this card", CommunicationMessage.INSUFFICIENT_RESOURCES);
            return false;
        }
        if(leaderCard.getUncovered()) {
            player.setPrivateCommunication("You already used this card", CommunicationMessage.CARD_ALREADY_USED);
            return false;
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

        }
        //aggiorno la view
        player.sendUpdateToPlayer();
        return true;
    }

    public boolean firstAction(int index1, int index2, HumanPlayer player) {

        if (index1 == index2 || index1 > 3 || index2 > 3 || index1 < 0 || index2 < 0) {
            player.setPrivateCommunication("You don't have this card/s", CommunicationMessage.CARD_ALREADY_USED);
            return false;
        }

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
        return true;
    }

    public boolean secondAction(ArrayList<Resources> resources, int indexPlayer, HumanPlayer player) {
        switch (indexPlayer) {
            case 0: {
                player.setPrivateCommunication("You can't play in this preparation turn", CommunicationMessage.ILLEGAL_ACTION);
                return false;
            }
            case 1:
            case 2: {
                if (resources.size() == 1) {
                    if (resources.get(0).ordinal() >= 4) {
                        player.setPrivateCommunication("The resource you sent is not SHIELD,STONE,SERVANT,COIN.", CommunicationMessage.ILLEGAL_ACTION);
                        return false;
                    }
                    player.getPlayerBoard().addStrongboxResource(resources.get(0), 1);

                } else {
                    player.setPrivateCommunication("You sent the wrong number of resources", CommunicationMessage.ILLEGAL_ACTION);
                    return false;
                }
                break;
            }
            case 3: {
                if (resources.size() == 2) {
                    if (resources.get(0).ordinal() >= 4 || resources.get(1).ordinal() >= 4) {
                        player.setPrivateCommunication("The resource you sent is not SHIELD,STONE,SERVANT,COIN.", CommunicationMessage.ILLEGAL_ACTION);
                        return false;
                    }
                    player.getPlayerBoard().addStrongboxResource(resources.get(0), 1);
                    player.getPlayerBoard().addStrongboxResource(resources.get(1), 1);
                } else {
                    player.setPrivateCommunication("You sent the wrong number of resources", CommunicationMessage.ILLEGAL_ACTION);
                    return false;
                }
                break;
            }
        }
        //aggiorno la view
        player.sendUpdateToPlayer();
        return true;
    }

    public boolean foldLeader(int index, HumanPlayer player) {
        PlayerBoard pb = player.getPlayerBoard();
        LeaderCard c;
        try {
            c = pb.getLeaderCard(index);
        } catch (IndexOutOfBoundsException e) {
            player.setPrivateCommunication(e.getMessage(), CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }
        if (!c.getUncovered()) {
            pb.removeLeaderCard(c);
        } else {
            player.setPrivateCommunication("You cannot discard this card.", CommunicationMessage.CARD_ALREADY_USED);
            return false;
        }
        //aggiorno la view
        player.sendUpdateToPlayer();
        return true;
    }

    public boolean isResourcesAvailable(HumanPlayer player, ExchangeResources exchangeResources, boolean buy_use) {
        String s;
        if (buy_use)
            s = "to buy this card";
        else s = "to use this card";
        if (!player.getPlayerBoard().checkResourcesWarehouse(exchangeResources.getWarehouse())) {
            player.setPrivateCommunication("You don't have enough resources in the warehouse" + s, CommunicationMessage.INSUFFICIENT_RESOURCES);
            return false;
        }
        if (!player.getPlayerBoard().checkResourcesStrongbox(exchangeResources.getStrongbox())) {
            player.setPrivateCommunication("You don't have enough resources in the strongbox" + s, CommunicationMessage.INSUFFICIENT_RESOURCES);
            return false;
        }
        if (!player.getPlayerBoard().checkResourcesSpecialWarehouse(exchangeResources.getSpecialWarehouse())) {
            player.setPrivateCommunication("You don't have enough resources in the special warehouse" + s, CommunicationMessage.INSUFFICIENT_RESOURCES);
            return false;
        }
        return true;
    }

    public void payResources(HumanPlayer player, ExchangeResources exchangeResources) {
        player.getPlayerBoard().payResourcesStrongbox(exchangeResources.getStrongbox());
        player.getPlayerBoard().payResourcesSpecialWarehouse(exchangeResources.getSpecialWarehouse());
        player.getPlayerBoard().payResourcesWarehouse(exchangeResources.getWarehouse());
    }

}
