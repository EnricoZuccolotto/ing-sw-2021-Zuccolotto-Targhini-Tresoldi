package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;


public class RoundController {

    private final GameBoard gameBoardInstance;
    private int turnCount;
    private int firstPlayer;
    private HumanPlayer playerInTurn;
    private TurnState turnState;
    private final ActionController actionController;
    private final ArrayList<HumanPlayer> players;
    private final ArrayList<Integer>  productions;
    private GameState gameState;
    private int winnerPlayer;
    private boolean Winner;


    public RoundController(GameBoard gameBoardInstance) {
        this.turnCount =0;
        this.gameBoardInstance = gameBoardInstance;
        this.players= gameBoardInstance.getPlayers();
        this.turnState=TurnState.FIRST_TURN;
        this.productions=new ArrayList<>(4);
        this.actionController = new ActionController();
        this.Winner=false;
        // FIXME: Example, should be the player with the inkwell
        this.winnerPlayer=-2;
    }
    public void init(HumanPlayer playerInTurn){
        if(players.size()==1)
            gameState=GameState.SINGLEPLAYER;
        else
            gameState=GameState.MULTIPLAYER;
        this.playerInTurn = playerInTurn;
        this.firstPlayer=players.indexOf(playerInTurn);
    }
    public void setPlayerInTurn(HumanPlayer player){
        this.playerInTurn=player;
    }

    public void handle_getMarket(MarketRequestMessage message){
        if (isYourTurn(message.getPlayerName())) {
            playerInTurn.setTemporaryResourceStorage(actionController.getMarket(gameBoardInstance, playerInTurn, message.getRowIndex(), message.getColIndex()));

            nextState(Action.STD_GETMARKET);
        }
    }
    public void handle_shiftWarehouse(ShiftWarehouseMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            actionController.shiftWarehouseRows(playerInTurn, message.getStartingPos(), message.getNewRowPos());
        }
    }
    public void handle_sortingWarehouse(SetResourceMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (message.getResource() == null) {
                // Messaggio che indica la fine delle risorse,
                nextState(Action.SORTING_WAREHOUSE);
            }
            if (message.getResource() == Resources.FAITH) {
                handle_addFaithPoint(1, playerInTurn);
            }
            actionController.addResourceToWarehouse(playerInTurn, message.getResource(), message.getPosition(), message.getReceivedResourceIndex());
        }
    }
    public void handle_discardResource(DiscardResourceMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            movePlayersExceptSelected(1);
        }
    }

    public void handle_useBaseProduction(UseProductionBaseMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (productions.contains(3))
                playerInTurn.setPrivateCommunication("You already used this production", CommunicationMessage.CARD_ALREADY_USED);
            else {
                if (actionController.useBaseProduction(playerInTurn, 2, message.getOutput(), message.getExchangeResources())) {
                    productions.add(3);
                    nextState(Action.STD_USEPRODUCTION);
                }
            }
        }

    }
    public void handle_useNormalProduction(UseProductionNormalMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (productions.contains(message.getIndex()))
                playerInTurn.setPrivateCommunication("You already used this production", CommunicationMessage.CARD_ALREADY_USED);
            else {
                if (actionController.useNormalProduction(playerInTurn, message.getIndex(), message.getExchangeResources())) {
                    productions.add(message.getIndex());
                    int faith = playerInTurn.getPlayerBoard().getProductionResult(message.getIndex())[Resources.FAITH.ordinal()];
                    handle_addFaithPoint(faith, playerInTurn);
                    nextState(Action.STD_USEPRODUCTION);
                }
            }
        }

    }

    public void handle_useSpecialProduction(UseProductionSpecialMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (productions.contains(message.getIndex() + 4))
                playerInTurn.setPrivateCommunication("You already used this production", CommunicationMessage.CARD_ALREADY_USED);
            else {
                if (actionController.useSpecialProduction(playerInTurn, message.getOutput(), message.getIndex(), message.getExchangeResources())) {
                    productions.add(message.getIndex() + 4);
                    handle_addFaithPoint(1, playerInTurn);
                    nextState(Action.STD_USEPRODUCTION);
                }
            }
        }
    }

    public void handle_getProduction(GetProductionCardMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            try {
                if (actionController.getProduction(message.getColor(), message.getLevel(), gameBoardInstance, message.getIndex(), playerInTurn, message.getExchangeResources()))
                    nextState(Action.STD_GETPRODUCTION);
            } catch (WinnerException e) {
                winnerPlayer = players.indexOf(playerInTurn);
                if (gameState == GameState.SINGLEPLAYER) {
                    nextTurn();
                }
            }
        }
    }

    public void handle_activeLeader(LeaderMessage message) {
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState = TurnState.NORMAL_ACTION;
            playerInTurn.setPrivateCommunication("You cannot do leader action", CommunicationMessage.CARD_ALREADY_USED);
        } else if (isYourTurn(message.getPlayerName())) {
            if (actionController.activateLeader(message.getIndex(), playerInTurn))
                nextState(Action.LD_ACTION);
        }
    }

    public void handle_foldLeader(LeaderMessage message) {
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState = TurnState.NORMAL_ACTION;
            playerInTurn.setPrivateCommunication("You cannot do leader action", CommunicationMessage.CARD_ALREADY_USED);
        } else if (isYourTurn(message.getPlayerName())) {
            if (actionController.foldLeader(message.getIndex(), playerInTurn)) {
                handle_addFaithPoint(1, playerInTurn);
                nextState(Action.LD_ACTION);
            }
        }
    }

    public void handle_firstAction(FirstActionMessage message) {
        boolean flag = false;
        synchronized (productions) {
            for (HumanPlayer player : players) {
                if (player.getName().equals(message.getPlayerName())) {
                    flag = true;
                    int n = players.indexOf(player);
                    if (productions.contains(n))
                        flag = false;
                    else {
                        if (actionController.firstAction(message.getIndex1(), message.getIndex2(), player)) {
                            productions.add(n);
                            nextState(Action.END_TURN);
                            break;
                        }
                    }
                }
            }
            if (!flag)
                gameBoardInstance.getPlayer(message.getPlayerName()).setPrivateCommunication("This is not your turn", CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    public void handle_secondAction(SecondActionMessage message){
        boolean flag=false;
        synchronized (productions){
            for (HumanPlayer player : players) {
                if (player.getName().equals(message.getPlayerName())) {
                    flag = true;
                    int n=players.indexOf(player);
                    if (productions.contains(n))
                        flag = false;

                    else {
                        if (actionController.secondAction(message.getResources(), n, player)) {
                            if (n >= 2)
                                actionController.addFaithPoint(gameBoardInstance, player, 1);
                            productions.add(n);
                            nextState(Action.END_TURN);
                            break;
                        }
                    }
                }
            }
            if (!flag)
                gameBoardInstance.getPlayer(message.getPlayerName()).setPrivateCommunication("You cannot do this action", CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    public void handle_addFaithPoint(int quantities, HumanPlayer player) {
        try {
            actionController.addFaithPoint(gameBoardInstance, player, quantities);
        } catch (WinnerException e) {
            winnerPlayer = players.indexOf(player);
            if (gameState == GameState.SINGLEPLAYER) {
                nextTurn();
            }
        }

    }


    public void handle_firstTurn()  {
        ArrayList<LeaderCard> leaderCards = CardParser.parseLeadCards();
        if(leaderCards == null)
            throw new IllegalActionException();
        else
        for(HumanPlayer player:players) {
            for (int i=0;i<4;i++) {
                player.getPlayerBoard().addLeaderCard(leaderCards.get(0));
                leaderCards.remove(0);
            }
        }
    }

    public void handle_endTurn() {
        int quantities = playerInTurn.getTemporaryResourceStorage().size();
        movePlayersExceptSelected(quantities);
        nextTurn();
    }

    public void movePlayersExceptSelected(int quantities) {
        if (gameState.equals(GameState.MULTIPLAYER)) {
            for (HumanPlayer player : players)
                if (!player.getName().equals(playerInTurn.getName()))
                    handle_addFaithPoint(quantities, player);
        } else if (gameState.equals(GameState.SINGLEPLAYER))
            handle_addFaithPoint(quantities, null);
    }

    void nextTurn() {
        int playersNumber = players.size();
        turnCount++;
        productions.clear();
        checkWinner(playersNumber);
        if (gameState.equals(GameState.SINGLEPLAYER))
            handle_Bot();
        nextState(Action.END_TURN);
        playerInTurn = players.get((firstPlayer + turnCount) % players.size());
    }
    private void checkWinner(int playersNumber){
        switch (gameState){
            case MULTIPLAYER:{
                if(players.get(turnCount%playersNumber).getPlayerBoard().getInkwell()&&winnerPlayer>=0) {
                    Winner = true;
                    turnState=TurnState.END;
                }break;}
            case SINGLEPLAYER:{
                if(winnerPlayer==0||winnerPlayer==-1)
                {
                    Winner = true;
                    turnState=TurnState.END;
                }break;}

        }
    }
    private void handle_Bot(){
        if(!Winner){
        try{
            gameBoardInstance.getBot().doAction();
        }
        catch (WinnerException e){
            winnerPlayer=-1;
            Winner = true;
            turnState=TurnState.END;
        }}
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public HumanPlayer getPlayerInTurn() {
        return playerInTurn;
    }

    public boolean isWinner() {
        return Winner;
    }

    private boolean isYourTurn(String name) {
        if (playerInTurn.getName().equals(name))
            return true;
        else {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("This is not your turn", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }
    }

    public void nextState(Action action) {

        switch (turnState) {
            case FIRST_TURN: {
                if (players.size() == productions.size()) {
                    if (gameState == GameState.MULTIPLAYER) {
                        turnState = TurnState.SECOND_TURN;
                        productions.clear();
                        productions.add(0);
                    }
                    else if(gameState.equals(GameState.SINGLEPLAYER)){
                        turnState =TurnState.FIRST_LEADER_ACTION;
                    }
                }
                break;
            }
            case SECOND_TURN:
            {
                if (players.size() == productions.size()) {
                    productions.clear();
                    turnState = TurnState.FIRST_LEADER_ACTION;
                }
                break;
            }
            case FIRST_LEADER_ACTION:
            {
                if(action.equals(Action.LD_ACTION)){
                if(0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
                    turnState = TurnState.NORMAL_ACTION;
                    break;
                }
                break;
                }
                turnState = TurnState.NORMAL_ACTION;
            }
            case NORMAL_ACTION:
            {
                if(action.equals(Action.STD_USEPRODUCTION))
                {
                    turnState = TurnState.PRODUCTION_ACTIONS;
                }
                else if(action.equals(Action.STD_GETMARKET)) {
                    turnState = TurnState.WAREHOUSE_ACTION;
                    break;
                }
                else {
                        turnState = TurnState.LAST_LEADER_ACTION;
                    }
                }

            case WAREHOUSE_ACTION:
            case PRODUCTION_ACTIONS: {
            if(action.equals(Action.STD_USEPRODUCTION)){

                if(productions.size()==playerInTurn.getPlayerBoard().getProductionNumber())
                    turnState=TurnState.LAST_LEADER_ACTION;
            }
            else turnState=TurnState.LAST_LEADER_ACTION;
            }
            case LAST_LEADER_ACTION:
            {
                if(action.equals(Action.END_TURN)) {
                    turnState = TurnState.FIRST_LEADER_ACTION;
                    break;
                }
                if (0== playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
                    nextTurn();
                    turnState= TurnState.FIRST_LEADER_ACTION;
                }
                break;
                }

            }
            }

    public GameState getGameState() {
        return gameState;
    }

    public int getWinnerPlayer() {
        return winnerPlayer;
    }
}


