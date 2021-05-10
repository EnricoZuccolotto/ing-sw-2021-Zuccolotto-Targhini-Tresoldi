package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.ExecutableMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.NetworkLayerView;
import it.polimi.ingsw.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private GameState gamestate;
    private final GameBoard gameBoardInstance;
    private final LobbyController lobby;
    private final RoundController roundController;
    private final Map<String, View> viewMap;

    public GameController() {
        this.gamestate = GameState.LOBBY;
        this.lobby = new LobbyController();
        this.gameBoardInstance = new GameBoard();
        this.roundController = new RoundController(gameBoardInstance);
        viewMap = Collections.synchronizedMap(new HashMap<>());
    }
    public GameBoard getInstance(){
        return gameBoardInstance;
    }

    public void addPlayer(String name, View view, boolean inkwell) {
        // TODO: Handle local single player
        gameBoardInstance.addPlayer(new HumanPlayer(name, inkwell));
        gameBoardInstance.addObserver((NetworkLayerView) view);
        gameBoardInstance.getMarket().addObserver((NetworkLayerView) view);
        gameBoardInstance.getFaithPath().addObserver((NetworkLayerView) view);
        gameBoardInstance.getDecks().addObserver((NetworkLayerView) view);
        gameBoardInstance.getPlayer(name).addObserver((NetworkLayerView) view);
    }

    public void addView(String name, NetworkLayerView view) {
        viewMap.put(name, view);

    }

    public void StartGame() {
        roundController.init();
        gameBoardInstance.init(gameBoardInstance);
        gamestate = GameState.GAMESTARTED;
        gameBoardInstance.setPublicCommunication("The game is starting", CommunicationMessage.PUBLIC);
        roundController.handle_firstTurn();
    }


    public synchronized void onMessage(Message message) {
        switch (gamestate) {
            case LOBBY: {
                    try {
                        ExecutableMessage currentMessage = (ExecutableMessage) message;
                        currentMessage.execute(this);
                    } catch (ClassCastException ex) {
                        // TODO: error, invalid executable message.
                    }
                    if (lobby.isFull()) {
                        for (String string : lobby.getPlayers()) {
                            addPlayer(string, viewMap.get(string), lobby.getPlayers().get(0).equals(string));
                        }
                        StartGame();
                        break;
                    }
                    break;
                }
            case GAMESTARTED: {
                // Catching a ClassCastException should be redundant, added for extra safety.
                // In theory messages received now should all be executable.
                try {
                    ExecutableMessage currentMessage = (ExecutableMessage) message;
                    currentMessage.execute(this);
                } catch (ClassCastException ex) {
                    // TODO: error, invalid executable message.
                }
                if (roundController.isWinner())
                    endGame();
            }
        }
    }

    public void buildInvalidResponse(String name) {
        if (this.getGameState().equals(GameState.LOBBY)) {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("The game is not started yet.", CommunicationMessage.ILLEGAL_ACTION);
        } else {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("You cannot do this action in this state " + roundController.getTurnState(), CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    public GameState getGameState() {
        return gamestate;
    }

    public boolean validateAction(Action action) {
        return TurnState.isPossible(roundController.getTurnState(), action);
    }


    public void endGame() {

        for (HumanPlayer player : gameBoardInstance.getPlayers()) {
            int VP = 0;
            VP += player.getPlayerBoard().getVictoryPointsCards();
            VP += gameBoardInstance.get_PV(gameBoardInstance.getPlayers().indexOf(player));
            VP += Math.floorDiv(player.getPlayerBoard().getNumberResources(), 5);
            player.getPlayerBoard().setVP(VP);
        }
        gamestate = GameState.END;
        gameBoardInstance.setPublicCommunication("THE END", CommunicationMessage.PUBLIC);

    }

    public RoundController getRoundController(){
        return roundController;
    }

    public LobbyController getLobby() {
        return lobby;
    }
}
