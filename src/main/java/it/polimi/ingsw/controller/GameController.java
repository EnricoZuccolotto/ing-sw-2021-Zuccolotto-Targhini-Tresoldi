package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.LobbyException;
import it.polimi.ingsw.model.communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.PlayerDisconnectionState;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.MORLogger;
import it.polimi.ingsw.network.messages.ExecutableMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.GameSaver;
import it.polimi.ingsw.view.NetworkLayerView;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the main controller logic.
 */
public class GameController implements Serializable {

    private GameState gamestate;
    private GameBoard gameBoardInstance;
    private final LobbyController lobby;
    private final RoundController roundController;
    private final transient Map<String, Observer> viewMap; // Avoid to serialize this.
    private View localView;
    private final boolean local;

    /**
     * Default constructor
     * @param local {@code true} if the game is local, {@code false} otherwise.
     */
    public GameController(boolean local) {
        this.gamestate = GameState.LOBBY;
        this.lobby = new LobbyController();
        this.gameBoardInstance = new GameBoard();
        this.roundController = new RoundController(gameBoardInstance, this);
        viewMap = Collections.synchronizedMap(new HashMap<>());
        this.local = local;
    }

    /**
     * Returns the current Game
     * @return The active GameBoard.
     */
    public GameBoard getInstance(){
        return gameBoardInstance;
    }

    /**
     * Adds a player to the game
     * @param name Player's name
     * @param view Player's view (either local or network-layered)
     * @param inkwell {@code true} if this is the inkwell player, {@code false} otherwise.
     */
    public void addPlayer(String name, Observer view, boolean inkwell) {
        gameBoardInstance.addPlayer(new HumanPlayer(name, inkwell));
        setViewObservers(view);
    }

    /**
     * Handles Observer additions for a particular view.
     * @param view The view you want to add to the Observable classes.
     */
    private void setViewObservers(Observer view){
        gameBoardInstance.addObserver(view);
        gameBoardInstance.getMarket().addObserver(view);
        gameBoardInstance.getFaithPath().addObserver(view);
        gameBoardInstance.getDecks().addObserver(view);
    }

    /**
     * Adds a view in the global view map
     * @param name Player's name
     * @param view Player's view handler.
     */
    public void addView(String name, Observer view) {
        viewMap.put(name, view);
    }

    /**
     * Handles game startup
     */
    public void StartGame() {
        roundController.init();
        for (HumanPlayer player : gameBoardInstance.getPlayers())
            for (HumanPlayer player1 : gameBoardInstance.getPlayers())
                player.addObserver(viewMap.get(player1.getName()));

        gameBoardInstance.setPublicCommunication("The game is starting", CommunicationMessage.STARTING_GAME);
        gameBoardInstance.init(gameBoardInstance);
        gamestate = GameState.GAME_STARTED;
        roundController.handle_firstTurn();
    }

    /**
     * Handles received messages.
     * @param message Message to execute.
     */
    public synchronized void onMessage(Message message) {
        if(local && message.getMessageType().equals(MessageType.LOGIN)){
            if(localView != null){
                if(message.getMessageType().equals(MessageType.LOGIN)){
                    addView(message.getPlayerName(), localView.getClientManager());
                    addPlayer(message.getPlayerName(), localView.getClientManager(), true);
                    StartGame();
                }
            } else {
                System.out.println("Error");
                System.exit(1);
            }
            return;
        }
        switch (gamestate) {
            case LOBBY: {
                try {
                    executableMessages(message);
                } catch (LobbyException e) {
                    NetworkLayerView view = (NetworkLayerView) viewMap.get(message.getPlayerName());
                    view.showCommunication((e.getLobbyError()).ordinal() + "", CommunicationMessage.ILLEGAL_LOBBY_ACTION);
                    break;
                }
                sendLobby();
                if (lobby.isFull()) {
                    ArrayList<String> lobbyPlayers = lobby.getPlayers();
                    // Now check that a saved game exists.
                    GameController restoredGameController = GameSaver.loadGame();
                    if(restoredGameController != null && restoredGameController.gameBoardInstance.getPlayersNicknames().containsAll(lobbyPlayers)){
                        // A game with the same players exists, restore the game.
                        restoreGame(restoredGameController);
                        gameBoardInstance.setPublicCommunication("The game is starting", CommunicationMessage.STARTING_GAME);
                        gameBoardInstance.sendGameUpdateToAllPlayers();
                        // Start a new turn
                        roundController.goToNextTurn();
                    } else {
                        // Create a new game.
                        Collections.shuffle(lobbyPlayers);
                        for (String string : lobbyPlayers) {
                            addPlayer(string, viewMap.get(string), lobbyPlayers.get(0).equals(string));
                            gameBoardInstance.getPlayer(string).setPlayerNumber(lobbyPlayers.indexOf(string));
                        }
                        StartGame();
                    }

                    break;
                }
                break;
            }
            case GAME_SETUP:
            case GAME_STARTED: {
                executableMessages(message);
                if (roundController.isWinner())
                    endGame();
            }
        }
    }

    /**
     * Tries to execute the received message
     * @param message Received message
     */
    private void executableMessages(Message message) {
        try {
            ExecutableMessage currentMessage = (ExecutableMessage) message;
            currentMessage.execute(this);
        } catch (ClassCastException ex) {
            MORLogger.LOGGER.warning("Invalid message: " + ex.getMessage());
        }
    }

    /**
     * Creates an error communication.
     * @param name Player's name
     */
    public void buildInvalidResponse(String name) {
        if (this.getGameState().equals(GameState.LOBBY)) {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("The game is not started yet.", CommunicationMessage.ILLEGAL_ACTION);
        } else {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("You cannot do this action in this state " + roundController.getTurnState(), CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    /**
     * Sends the lobby updates to every player.
     */
    public void sendLobby() {
        for (Observer view : viewMap.values()) {
            NetworkLayerView view1 = (NetworkLayerView) view;
            view1.showLobby(lobby.getPlayers());
        }
    }

    /**
     * Returns the game state.
     * @return The current game state.
     */
    public GameState getGameState() {
        return gamestate;
    }

    /**
     * Sets the GameState.
     * @param gamestate The state you want to set.
     */
    public void setGameState(GameState gamestate) {
        this.gamestate = gamestate;
    }

    /**
     * Asks the round controller if an action is valid
     * @param action Action to ask
     * @return {@code true} if the action can be executed, {@code false} otherwise.
     */
    public boolean validateAction(Action action) {
        return roundController.getTurnState().isPossible(action);
    }

    /**
     * Handles the game end.
     */
    public void endGame() {

        for (HumanPlayer player : gameBoardInstance.getPlayers()) {
            int VP = 0;
            VP += player.getPlayerBoard().getVictoryPointsCards();
            VP += gameBoardInstance.get_PV(gameBoardInstance.getPlayers().indexOf(player));
            VP += Math.floorDiv(player.getPlayerBoard().getNumberResources(), 5);
            player.getPlayerBoard().setVP(VP);
            player.sendUpdateToPlayer();
        }
        gamestate = GameState.END;
        gameBoardInstance.setPublicCommunication(roundController.getWinnerPlayer() + "", CommunicationMessage.END_GAME);

        // Now that the game has ended, we delete the saved game file since an ended game cannot be restarted.
        GameSaver.deleteSavedGame();
    }

    /**
     * Returns the active round controller
     * @return The current {@code RoundController}
     */
    public RoundController getRoundController(){
        return roundController;
    }

    /**
     * Returns the current lobby.
     * @return The active lobby controller.
     */
    public LobbyController getLobby() {
        return lobby;
    }

    /**
     * This method allows to access the viewMap from outside.
     * @param nickname The user's nickname
     * @return The user's view.
     */
    public Observer getViewFromMap(String nickname) {return viewMap.get(nickname); }

    /**
     * In the case of a local game, set the user's view.
     * @param view The view you want to associate with the user.
     */
    public void setLocalView(View view){
        this.localView = view;
    }

    /**
     * Restores a game from disk
     * @param savedGameController The disk-obtained {@code RoundController}
     */
    private void restoreGame(GameController savedGameController){
        gamestate = savedGameController.gamestate;
        gameBoardInstance = savedGameController.gameBoardInstance;

        roundController.restoreRoundController(savedGameController.getRoundController(), gameBoardInstance, this);


        for(HumanPlayer player : gameBoardInstance.getPlayers()){
            // The game starts if all players reconnected, we set them active if they previously disconnected.
            if(!player.getPlayerState().equals(PlayerDisconnectionState.TERMINAL)){
                player.setPlayerState(PlayerDisconnectionState.ACTIVE);
            }
            // Add observers
            Observer playerView = viewMap.get(player.getName());
            setViewObservers(playerView);
        }
        for (HumanPlayer player : gameBoardInstance.getPlayers())
            for (HumanPlayer player1 : gameBoardInstance.getPlayers())
                player.addObserver(viewMap.get(player1.getName()));
    }

    /**
     * Checks if the game is local.
     * @return {@code true} if the game is local, {@code false} otherwise.
     */
    public boolean isLocal() {
        return local;
    }
}
