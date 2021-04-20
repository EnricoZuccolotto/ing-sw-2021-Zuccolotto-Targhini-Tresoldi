package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.*;
import  it.polimi.ingsw.view.*;
import it.polimi.ingsw.network.server.*;
import it.polimi.ingsw.view.NetworkLayerView;
import it.polimi.ingsw.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private GameState gamestate;
    //private  Server server;
    private GameBoard gameBoardInstance;
    private LobbyController lobby;
    private RoundController roundController;
    private Map<String, View> viewMap;

    public GameController() {
        //this.server = server;
        this.gamestate = GameState.LOBBY;
        this.lobby = new LobbyController();
        this.gameBoardInstance = new GameBoard();
        this.roundController = new RoundController(gameBoardInstance);
        viewMap = Collections.synchronizedMap(new HashMap<>());

    }
    public GameBoard getInstance(){
        return gameBoardInstance;
    }
    public void doAction(){}
    public void addPlayer(String name, View view){
        // TODO: Handle local single player
        addView(name, (NetworkLayerView) view);
        gameBoardInstance.addPlayer(new HumanPlayer(name,false));
    }
    public void addView(String name, NetworkLayerView view){
        viewMap.put(name, view);
        gameBoardInstance.addObserver(view);
    }
    public void StartGame(){
        roundController.init(gameBoardInstance.getPlayers().get(0));
      gameBoardInstance.init(gameBoardInstance);
      gamestate=roundController.getGameState();
      roundController.handle_firstTurn();

    }

    // FIXME: Move this code to handle messages. This code will be moved as soon as the messages are implemented.


    public void onMessage(Message message){
        // TODO: Special warehouse actions
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
                if(validateAction(Action.LD_ACTION))
                    roundController.handle_foldLeader((LeaderMessage) message);
                else buildInvalidResponse();
            case ACTIVE_LEADER:
                if(validateAction(Action.LD_ACTION))
                    roundController.handle_activeLeader((LeaderMessage) message);
                else buildInvalidResponse();
            case FIRST_ACTION:
                if(validateAction(Action.FIRST_ACTION))
                    roundController.handle_firstAction((FirstActionMessage) message);
                else buildInvalidResponse();
            case SECOND_ACTION:
                if(validateAction(Action.FIRST_ACTION))
                    roundController.handle_secondAction((SecondActionMessage) message);
                else buildInvalidResponse();
                if(roundController.isWinner())
                    endGame();
        }
    }

    public void buildInvalidResponse() {

    }


    private boolean validateAction(Action action){
        return TurnState.isPossible(roundController.getTurnState(), action);
    }

    void firstTurn(){

    }

    public GameState getGameState(){
        return gamestate;}
    public void endGame(){
        for (HumanPlayer player:gameBoardInstance.getPlayers())
        {
            int VP=0;
            VP+=player.getPlayerBoard().getVictoryPointsCards();
            VP+=gameBoardInstance.get_PV(gameBoardInstance.getPlayers().indexOf(player));
            VP+=Math.floorDiv(player.getPlayerBoard().getNumberResources(),5);
            player.getPlayerBoard().setVP(VP);
        }
       gamestate=GameState.END;
    }

}
