package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.tools.CardParser;

import java.util.ArrayList;
import java.util.Random;

public class RoundController {

    private GameBoard gameBoardInstance;
    private int turncount;
    private Player playerInTurn;
    private TurnState turnstate;
    private ArrayList<HumanPlayer> players;


    public RoundController(GameBoard gameBoardInstance) {
        this.turncount=0;
        this.gameBoardInstance = gameBoardInstance;
        this.players= gameBoardInstance.getPlayers();
        playerInTurn= gameBoardInstance.getPlayers().get((new Random()).nextInt(gameBoardInstance.getPlayers().size()));
        this.turnstate=TurnState.FIRST_LEADER_ACTION;
    }
    public void handle_getMarket(){
    }
    public void handle_useProduction(){
    }
    public void handle_getProduction(){
    }
    public void handle_activeLeader(){
    }
    public void handle_foldLeader(){
    }
    void nextTurn() {
        turncount++;
        playerInTurn = players.get(turncount % players.size());
        turnstate = TurnState.FIRST_LEADER_ACTION;
    }
    void firstTurn(){

        ArrayList<LeaderCard> leaderCards=CardParser.parseLeadCards();

    }
    public TurnState getTurnstate() {
        return turnstate;
    }
}
