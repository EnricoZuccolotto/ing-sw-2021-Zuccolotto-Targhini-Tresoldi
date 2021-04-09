package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;


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
        this.gameBoardInstance = GameBoard.getInstance();
        this.roundController = new RoundController(gameBoardInstance);
    }
    public GameBoard getInstance(){
        return gameBoardInstance;
    }
    public void doAction(){}
    public void addPlayer(){}
    public void StartGame(){}
    public void OnMessage(){}
    public void ChangeTurn(){}
    public void endGame(){}
    public GameState getGameState(){
        return gamestate;
    }
}
