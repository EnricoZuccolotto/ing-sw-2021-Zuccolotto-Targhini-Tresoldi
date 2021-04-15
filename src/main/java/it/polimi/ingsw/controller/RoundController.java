package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.CardAlreadyUsedException;
import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.network.messages.*;
import java.util.ArrayList;


public class RoundController {

    private final GameBoard gameBoardInstance;
    private int turnCount;
    private HumanPlayer playerInTurn;
    private TurnState turnState;
    private final ActionController actionController;
    private final ArrayList<HumanPlayer> players;
    private ArrayList<Integer> productions;


    public RoundController(GameBoard gameBoardInstance) {
        this.turnCount =0;
        this.gameBoardInstance = gameBoardInstance;
        this.players= gameBoardInstance.getPlayers();
        this.turnState=TurnState.FIRST_TURN;
        this.productions=new ArrayList<>(4);
        this.actionController = new ActionController();
        // FIXME: Example, should be the player with the inkwell
        this.playerInTurn = players.get(0);
    }
    public void setPlayerInTurn(HumanPlayer player){
        this.playerInTurn=player;
    }
    public void handle_getMarket(MarketRequestMessage message){


            if(playerInTurn.getName().equals(message.getPlayerName())){
                MarketReplyMessage reply = actionController.getMarket(gameBoardInstance, playerInTurn, message.getRowIndex(), message.getColIndex());
                nextState(Action.STD_GETMARKET);
            } else {
                ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
            }
    }
    public void handle_shiftWarehouse(ShiftWarehouseMessage message) {
        if(playerInTurn.getName().equals(message.getPlayerName())){
            actionController.shiftWarehouseRows(playerInTurn, message.getStartingPos(), message.getNewRowPos());
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
    }
    public void handle_sortingWarehouse(SetResourceMessage message) {
        if(playerInTurn.getName().equals(message.getPlayerName())){
            if(message.getResource() == null) {
                // Messaggio che indica la fine delle risorse,
              nextState(Action.SORTING_WAREHOUSE);
            }
            if(message.getResource() == Resources.FAITH){
                actionController.addFaithPoint(gameBoardInstance, playerInTurn,1);
            }
            Message reply = actionController.addResourceToWarehouse(playerInTurn, message.getResource(), message.getPosition(), message.getReceivedResourceIndex());
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        // TODO: Send message
    }
    public void handle_discardResource(DiscardResourceMessage message) {
        if(playerInTurn.getName().equals(message.getPlayerName())){
            for(HumanPlayer player : players){
                // Every player except the current one get a faith point
                if(!player.getName().equals(playerInTurn.getName())){
                    actionController.addFaithPoint(gameBoardInstance, player,1);
                }
            }
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
    }
    public void handle_useBaseProduction(UseProductionBaseMessage message){
        if(playerInTurn.getName().equals(message.getPlayerName())){
            if(productions.contains(3))
                throw new CardAlreadyUsedException();
            else
            actionController.useBaseProduction(playerInTurn,2,message.getOutput(), message.getResWar(), message.getResStr(), message.getResSpeWar());
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        productions.add(3);
       nextState(Action.STD_USEPRODUCTION);
    }
    public void handle_useNormalProduction(UseProductionNormalMessage message){
        if(playerInTurn.getName().equals(message.getPlayerName())){
            if(productions.contains(message.getIndex()))
                throw new CardAlreadyUsedException();
            else
                actionController.useNormalProduction(playerInTurn,message.getIndex(), message.getResWar(), message.getResStr(), message.getResSpeWar());
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        productions.add(message.getIndex());
        int faith=playerInTurn.getPlayerBoard().getProductionCost(message.getIndex())[Resources.FAITH.ordinal()];
        actionController.addFaithPoint(gameBoardInstance,playerInTurn,faith);
       nextState(Action.STD_USEPRODUCTION);
    }
    public void handle_useSpecialProduction(UseProductionSpecialMessage message){
        if(playerInTurn.getName().equals(message.getPlayerName())){
            if(productions.contains(message.getIndex()+4))
                throw new CardAlreadyUsedException();
            else
                actionController.useSpecialProduction(playerInTurn,message.getOutput(),message.getIndex(), message.getResWar(), message.getResStr(), message.getResSpeWar());
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        productions.add(message.getIndex()+4);
        actionController.addFaithPoint(gameBoardInstance,playerInTurn,1);
        nextState(Action.STD_USEPRODUCTION);
    }
    public void handle_getProduction(GetProductionCardMessage message){
        if(playerInTurn.getName().equals(message.getPlayerName())){
            actionController.getProduction(message.getColor(), message.getLevel(), gameBoardInstance,message.getIndex(),playerInTurn, message.getResWar(), message.getResStr(), message.getResSpeWar());
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
      nextState(Action.STD_GETPRODUCTION);
    }
    public void handle_activeLeader(){
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState=TurnState.NORMAL_ACTION;
            throw new IllegalActionException();
        }

       nextState(Action.LD_LEADERACTION);
    }
    public void handle_foldLeader(FoldLeaderMessage message){
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState=TurnState.NORMAL_ACTION;
            throw new IllegalActionException();
        }
        if(playerInTurn.getName().equals(message.getPlayerName())){
            actionController.foldLeader(message.getIndex(), playerInTurn);
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        actionController.addFaithPoint(gameBoardInstance,playerInTurn,1);
        nextState(Action.LD_FOLD);
    }
    public void handle_firstAction(FirstActionMessage message){
        if(playerInTurn.getName().equals(message.getPlayerName())){
            actionController.firstAction(message.getIndex1(),message.getIndex2(), playerInTurn);
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        nextTurn();

    }

    public void handle_firstTurn()  {
        ArrayList<LeaderCard> leaderCards= CardParser.parseLeadCards();
        if(leaderCards==null)
            throw new IllegalActionException();
        else
        for(HumanPlayer player:players) {
            for (int i=0;i<4;i++)
            {
            player.getPlayerBoard().addLeaderCard(leaderCards.get(0));
            leaderCards.remove(0);
        }}
    }
    public void handle_endTurn() {
        nextTurn();
    }
    void nextTurn() {
        int playersNumber=players.size();
        turnCount++;
        productions.clear();
        if(playersNumber==1)
            gameBoardInstance.getBot().doAction();
        nextState(Action.END_TURN);
        playerInTurn = players.get(turnCount % players.size());
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public HumanPlayer getPlayerInTurn() {
        return playerInTurn;
    }

    public void nextState(Action action) {

        switch (turnState) {
            case FIRST_TURN:
            {
                if (players.size() == turnCount)
                        turnState = TurnState.FIRST_LEADER_ACTION;

                break;
            }
            case FIRST_LEADER_ACTION:
            {
                if(action.equals(Action.LD_LEADERACTION)||action.equals(Action.LD_FOLD)){
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
        }


