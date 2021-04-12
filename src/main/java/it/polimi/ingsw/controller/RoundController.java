package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.tools.CardParser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class RoundController {

    private GameBoard gameBoardInstance;
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
    }
    public void setPlayerinTurn(HumanPlayer player){
        this.playerInTurn=player;
    }
    public void handle_getMarket(){
        turnState=nextState(Action.STD_GETMARKET);
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
    public void handle_sortingWarehouse() {
       //todo: se non ci sono risorse da mettere a posto invoca il metodo nextState
    }
    public void handle_shiftWarehouse() {

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

