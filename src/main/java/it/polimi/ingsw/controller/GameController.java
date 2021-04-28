package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.NetworkLayerView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.network.messages.LobbySetMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private GameState gamestate;
    //private  Server server;
    private final GameBoard gameBoardInstance;
    private final LobbyController lobby;
    private final RoundController roundController;
    private final Map<String, View> viewMap;

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

    public void addPlayer(String name, View view){
        // TODO: Handle local single player
        addView(name, (NetworkLayerView) view);
        gameBoardInstance.addPlayer(new HumanPlayer(name, false));
    }
    public void addView(String name, NetworkLayerView view){
        viewMap.put(name, view);
        gameBoardInstance.addObserver(view);
        gameBoardInstance.getMarket().addObserver(view);
        gameBoardInstance.getFaithPath().addObserver(view);
        // TODO: Get player from username and add observer.
    }
    public void StartGame(){
        roundController.init(gameBoardInstance.getPlayers().get(0));
        gameBoardInstance.init(gameBoardInstance);
        gamestate = GameState.GAMESTARTED;
        roundController.handle_firstTurn();

    }


    public void onMessage(Message message) {
        // TODO: Special warehouse actions
        switch (gamestate) {
            case LOBBY: {
                switch (message.getMessageType()){
                    case SET_GAME: {
                        lobby.handle_setLobby((LobbySetMessage) message);
                    }
                    case JOIN_GAME:{
                        lobby.handle_addinLobby((LobbyJoinMessage) message);
                    }
                }
                if(lobby.isFull()){
                    for(String string: lobby.getPlayer()){
                        addPlayer(string, view);
                    }
                }
            }
            case GAMESTARTED: {
            switch (message.getMessageType()) {
                case MARKET_REQUEST: {
                    if (validateAction(Action.STD_GETMARKET))
                        roundController.handle_getMarket((MarketRequestMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case SHIFT_WAREHOUSE: {
                    if (validateAction(Action.SHIFT_WAREHOUSE))
                        roundController.handle_shiftWarehouse((ShiftWarehouseMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case SET_RESOURCE: {
                    if (validateAction(Action.SORTING_WAREHOUSE))
                        roundController.handle_sortingWarehouse((SetResourceMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case DISCARD_RESOURCE: {
                    if (validateAction(Action.SORTING_WAREHOUSE))
                        roundController.handle_discardResource((DiscardResourceMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case GET_PRODUCTIONCARD: {
                    if (validateAction(Action.STD_GETPRODUCTION))
                        roundController.handle_getProduction((GetProductionCardMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case USE_BASE_PRODUCTION: {
                    if (validateAction(Action.STD_USEPRODUCTION))
                        roundController.handle_useBaseProduction((UseProductionBaseMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case USE_NORMAL_PRODUCTION: {
                    if (validateAction(Action.STD_USEPRODUCTION))
                        roundController.handle_useNormalProduction((UseProductionNormalMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case USE_SPECIAL_PRODUCTION: {
                    if (validateAction(Action.STD_USEPRODUCTION))
                        roundController.handle_useSpecialProduction((UseProductionSpecialMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case END_TURN: {
                    if (validateAction(Action.END_TURN))
                        roundController.handle_endTurn();
                    else buildInvalidResponse();
                    break;
                }
                case FOLD_LEADER: {
                    if (validateAction(Action.LD_ACTION))
                        roundController.handle_foldLeader((LeaderMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case ACTIVE_LEADER: {
                    if (validateAction(Action.LD_ACTION))
                        roundController.handle_activeLeader((LeaderMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case FIRST_ACTION: {
                    if (validateAction(Action.FIRST_ACTION))
                        roundController.handle_firstAction((FirstActionMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case SECOND_ACTION: {
                    if (validateAction(Action.FIRST_ACTION))
                        roundController.handle_secondAction((SecondActionMessage) message);
                    else buildInvalidResponse();
                    break;
                }
                case JOIN_GAME:
                case SET_GAME: {
                    buildInvalidResponse();
                    break;
                }
            }
            }
        }
        if (roundController.isWinner())
            endGame();
    }

    public void buildInvalidResponse() {

    }


    private boolean validateAction(Action action){
        return TurnState.isPossible(roundController.getTurnState(), action);
    }


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
