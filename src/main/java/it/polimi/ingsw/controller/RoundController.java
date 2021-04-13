package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class RoundController {

    private GameBoard gameBoardInstance;
    private ActionController actionController;
    private int turncount;
    private HumanPlayer playerInTurn;
    private TurnState turnState;
    private ArrayList<HumanPlayer> players;
    private int leaderActions;


    public RoundController(GameBoard gameBoardInstance) {
        this.turncount=0;
        this.gameBoardInstance = gameBoardInstance;
        this.players= gameBoardInstance.getPlayers();
        this.turnState=TurnState.FIRST_LEADER_ACTION;
        this.leaderActions=0;
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
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
        // TODO: Send back the message
        turnState = nextState(Action.STD_GETMARKET);
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
                // Messaggio che indica la fine delle risorse, chiama nextState()
            }
            if(message.getResource() == Resources.FAITH){
                actionController.addFaithPoint(gameBoardInstance, playerInTurn);
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
                if(player.getName() != playerInTurn.getName()){
                    actionController.addFaithPoint(gameBoardInstance, player);
                }
            }
        } else {
            ErrorMessage reply = new ErrorMessage(message.getPlayerName(), "This is not your turn!");
        }
    }

    public void handle_useProduction(){
        turnState=nextState(Action.STD_USEPRODUCTION);
    }
    public void handle_getProduction(){
        turnState=nextState(Action.STD_GETPRODUCTION);
    }
    public void handle_activeLeader(){
        if (leaderActions == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState=TurnState.NORMAL_ACTION;
            throw new IllegalActionException();
        }
        leaderActions++;
        turnState=nextState(Action.LD_LEADERACTION);
    }
    public void handle_foldLeader(){
        if (leaderActions == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState=TurnState.NORMAL_ACTION;
            throw new IllegalActionException();
        }
        leaderActions++;

        turnState=nextState(Action.LD_FOLD);
    }

    void handle_firstTurn() throws InstantiationException {
        ArrayList<LeaderCard> leaderCards= CardParser.parseLeadCards();
        if(leaderCards==null)
            throw new InstantiationException("Failed to load leader cards");
        else
        for(HumanPlayer player:players) {
            for (int i=0;i<4;i++)
            {
            player.getPlayerBoard().addLeaderCard(leaderCards.get(0));
            leaderCards.remove(0);
        }}
    }
    public void handle_endTurn() {
        turnState=TurnState.FIRST_LEADER_ACTION;
        nextTurn();
    }
    void nextTurn() {
        turncount++;
        leaderActions=0;
        playerInTurn = players.get(turncount % players.size());
    }

    public TurnState getTurnstate() {
        return turnState;
    }

    public HumanPlayer getPlayerInTurn() {
        return playerInTurn;
    }

    public TurnState nextState(Action action) {
        switch (turnState) {
            case FIRST_LEADER_ACTION:
            {
                if(action.equals(Action.STD_GETMARKET))
                    return TurnState.WAREHOUSE_ACTION;
                else if(action.equals(Action.STD_GETPRODUCTION)||action.equals(Action.STD_USEPRODUCTION))
                {
                    if (leaderActions == playerInTurn.getPlayerBoard().getLeaderCardsNumber())
                        nextTurn();
                    return TurnState.FIRST_LEADER_ACTION;
                }
                else if(leaderActions == playerInTurn.getPlayerBoard().getLeaderCardsNumber())
                    return TurnState.NORMAL_ACTION;
            }
            case NORMAL_ACTION:
            {
                if(action.equals(Action.SORTING_WAREHOUSE))
                    return TurnState.WAREHOUSE_ACTION;
                else {
                    if (leaderActions == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
                        nextTurn();
                        return TurnState.FIRST_LEADER_ACTION;

                    } else
                        return TurnState.LAST_LEADER_ACTION;
                }

            }
            case WAREHOUSE_ACTION:
            case LAST_LEADER_ACTION:
            {
                if (leaderActions == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
                    nextTurn();
                    return TurnState.FIRST_LEADER_ACTION;

                } else
                    return TurnState.LAST_LEADER_ACTION;
            }
            }
          return TurnState.FIRST_LEADER_ACTION;
        }
    }

