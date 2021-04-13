package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.*;


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

    // FIXME: Move this code to handle messages. This code will be moved as soon as the messages are implemented.
    public void OnMessage(Action action){
        switch (action) {
            case LD_FOLD:
                 leaderFoldCheck(action);

            case END_TURN:
                endTurnCheck(action);

            default:    // this must never be reached in a normal Game!
                System.out.println("GAME STATE ERROR FOR THIS MESSAGE");
        }

    }

    public void onMessage(Message message){
        switch(message.getMessageType()){
            case MARKET_REQUEST:
                if(validateAction(Action.STD_GETMARKET))
                    roundController.handle_getMarket((MarketRequestMessage) message);
                else buildInvalidResponse();
            case SHIFT_WAREHOUSE:
                if(validateAction(Action.SHIFT_WAREHOUSE))
                    roundController.handle_shiftWarehouse((ShiftWarehouseMessage) message);
                else buildInvalidResponse();
            case SET_RESOURCE:
                if(validateAction(Action.SORTING_WAREHOUSE))
                    roundController.handle_sortingWarehouse((SetResourceMessage) message);
                else buildInvalidResponse();
            case DISCARD_RESOURCE:
                if(validateAction(Action.SORTING_WAREHOUSE))
                    roundController.handle_discardResource((DiscardResourceMessage) message);
                else buildInvalidResponse();
            case GET_PRODUCTIONCARD:
                if(validateAction(Action.STD_GETPRODUCTION))
                    roundController.handle_getProduction((GetProductionCardMessage) message);
                else buildInvalidResponse();
            case USE_BASE_PRODUCTION:
                if(validateAction(Action.STD_USEPRODUCTION))
                    roundController.handle_useBaseProduction((UseProductionBaseMessage) message);
                else buildInvalidResponse();
            case USE_NORMAL_PRODUCTION:
                if(validateAction(Action.STD_USEPRODUCTION))
                    roundController.handle_useNormalProduction((UseProductionNormalMessage) message);
                else buildInvalidResponse();
            case USE_SPECIAL_PRODUCTION:
                if(validateAction(Action.STD_USEPRODUCTION))
                    roundController.handle_useSpecialProduction((UseProductionSpecialMessage) message);
                else buildInvalidResponse();
        }
    }

    public void buildInvalidResponse() {

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
    private void getProductionCheck(Action action){
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
    private boolean validateAction(Action action){
        return TurnState.isPossible(roundController.getTurnstate(), action);
    }
    void firstTurn(){

    }
    public void endGame(){}
    public GameState getGameState(){
        return gamestate;
    }
}
