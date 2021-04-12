package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;

import java.util.ArrayList;
import java.util.Random;


public class GameController {
    private GameState gamestate;
    //private  Server server;
    private GameBoard gameBoardInstance;
    private LobbyController lobby;
    private RoundController roundController;

    public GameController() {
        //this.server = server;
        this.gamestate = GameState.LOBBY;
        this.lobby = new LobbyController();
        this.gameBoardInstance = new GameBoard();
        this.roundController = new RoundController(gameBoardInstance);
    }
    public GameBoard getInstance(){
        return gameBoardInstance;
    }
    public void doAction(){}
    public void addPlayer(String name){
        gameBoardInstance.addPlayer(new HumanPlayer(name,false));
    }
    public void StartGame(){
      gameBoardInstance.init(gameBoardInstance);
      firstTurn();

    }


    public void OnMessage(Action action){
        switch (action) {
            case LD_FOLD:
                 leaderFoldCheck(action);
            case LD_LEADERACTION:
                leaderActionCheck(action);
            case SHIFT_WAREHOUSE:
                shiftWarehouseCheck(action);
            case STD_GETPRODUCTION:
                getProductionCheck(action);
            case SORTING_WAREHOUSE:
                sortingWarehouseCheck(action);
            case STD_USEPRODUCTION:
                useProductionCheck(action);
            case STD_GETMARKET:
                getMarketCheck(action);
            case END_TURN:
                endTurnCheck(action);

            default:    // this must never be reached in a normal Game!
                System.out.println("GAME STATE ERROR FOR THIS MESSAGE");
        }

    }
    private void leaderFoldCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_foldLeader();
        //else return buildInvalidResponse();
    }
    private void leaderActionCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    private void shiftWarehouseCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    private void getProductionCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    private void sortingWarehouseCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    private void useProductionCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    private void getMarketCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    private void endTurnCheck(Action action){
        if(TurnState.isPossible(roundController.getTurnstate(),action))
            roundController.handle_activeLeader();
        //else return buildInvalidResponse();
    }
    void firstTurn(){

    }
    public void endGame(){}
    public GameState getGameState(){
        return gamestate;
    }
}
