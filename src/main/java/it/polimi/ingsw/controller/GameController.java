package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.*;
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
        addView(name, (NetworkLayerView) view);
    }

    public void addView(String name, NetworkLayerView view) {
        viewMap.put(name, view);
        gameBoardInstance.addObserver(view);
        gameBoardInstance.getMarket().addObserver(view);
        gameBoardInstance.getFaithPath().addObserver(view);
        gameBoardInstance.getDecks().addObserver(view);
        gameBoardInstance.getPlayer(name).addObserver(view);
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
                switch (message.getMessageType()) {
                    case SET_GAME: {
                        lobby.handle_setLobby((LobbySetMessage) message);
                        break;
                    }
                    case JOIN_GAME: {
                        lobby.handle_addInLobby((LobbyJoinMessage) message);
                    }
                    if (lobby.isFull()) {
                        for (String string : lobby.getPlayers()) {
                            addPlayer(string, viewMap.get(string), lobby.getPlayers().get(0).equals(string));
                        }
                        break;
                    }
                }

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
        gameBoardInstance.getPlayer(name).setPrivateCommunication("You cannot do this action in this state " + roundController.getTurnState(), CommunicationMessage.ILLEGAL_ACTION);
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

}
