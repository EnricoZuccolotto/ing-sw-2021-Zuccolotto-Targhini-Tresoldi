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
      roundController.handle_firstTurn();

    }

    // FIXME: Move this code to handle messages. This code will be moved as soon as the messages are implemented.


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
            case END_TURN:
                if(validateAction(Action.END_TURN))
                    roundController.handle_endTurn();
                else buildInvalidResponse();
            case FOLD_LEADER:
                if(validateAction(Action.LD_FOLD))
                    roundController.handle_foldLeader((FoldLeaderMessage) message);
                else buildInvalidResponse();
            case FIRST_ACTION:
                if(validateAction(Action.FIRST_ACTION))
                    roundController.handle_firstAction((FirstActionMessage) message);
                else buildInvalidResponse();
        }
    }

    public void buildInvalidResponse() {

    }


    private boolean validateAction(Action action){
        return TurnState.isPossible(roundController.getTurnState(), action);
    }
    void firstTurn(){

    }
    public void endGame(){}
    public GameState getGameState(){
        return gamestate;
    }
}
