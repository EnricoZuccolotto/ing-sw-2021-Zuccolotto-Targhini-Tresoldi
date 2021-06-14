package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.controller.LobbyException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.ExecutableMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.GameSaver;
import it.polimi.ingsw.view.NetworkLayerView;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.util.*;


public class GameController implements Serializable {

    private GameState gamestate;
    private GameBoard gameBoardInstance;
    private final LobbyController lobby;
    private final RoundController roundController;
    private final transient Map<String, Observer> viewMap; // Avoid to serialize this.
    private View localView;
    private final boolean local;

    public GameController(boolean local) {
        this.gamestate = GameState.LOBBY;
        this.lobby = new LobbyController();
        this.gameBoardInstance = new GameBoard();
        this.roundController = new RoundController(gameBoardInstance, this);
        viewMap = Collections.synchronizedMap(new HashMap<>());
        this.local = local;
    }
    public GameBoard getInstance(){
        return gameBoardInstance;
    }

    public void addPlayer(String name, Observer view, boolean inkwell) {
        gameBoardInstance.addPlayer(new HumanPlayer(name, inkwell));
        setViewObservers(view);
    }

    private void setViewObservers(Observer view){
        gameBoardInstance.addObserver(view);
        gameBoardInstance.getMarket().addObserver(view);
        gameBoardInstance.getFaithPath().addObserver(view);
        gameBoardInstance.getDecks().addObserver(view);
    }

    public void addView(String name, NetworkLayerView view) {
        viewMap.put(name, view);
    }

    public void StartGame() {
        roundController.init();
        for (HumanPlayer player : gameBoardInstance.getPlayers())
            for (HumanPlayer player1 : gameBoardInstance.getPlayers())
                player.addObserver(viewMap.get(player1.getName()));

        gameBoardInstance.setPublicCommunication("The game is starting", CommunicationMessage.STARTING_GAME);
        gameBoardInstance.init(gameBoardInstance);
        gamestate = GameState.GAMESTARTED;
        roundController.handle_firstTurn();
    }


    public synchronized void onMessage(Message message) {
        if(local && message.getMessageType().equals(MessageType.LOGIN)){
            if(localView != null){
                if(message.getMessageType().equals(MessageType.LOGIN)){
                    viewMap.put(message.getPlayerName(), localView.getClientManager());
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
                        }
                        StartGame();
                    }

                    break;
                }
                break;
            }
            case GAMESTARTED: {
                executableMessages(message);
                if (roundController.isWinner())
                    endGame();
            }
        }
    }

    private void executableMessages(Message message) {
        try {
            ExecutableMessage currentMessage = (ExecutableMessage) message;
            currentMessage.execute(this);
        } catch (ClassCastException ex) {
            SocketClient.LOGGER.warning("Invalid message: " + ex.getMessage());
        }
    }

    public void buildInvalidResponse(String name) {
        if (this.getGameState().equals(GameState.LOBBY)) {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("The game is not started yet.", CommunicationMessage.ILLEGAL_ACTION);
        } else {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("You cannot do this action in this state " + roundController.getTurnState(), CommunicationMessage.ILLEGAL_ACTION);
        }
    }

    private void sendLobby() {
        for (Observer view : viewMap.values()) {
            NetworkLayerView view1 = (NetworkLayerView) view;
            view1.showLobby(lobby.getPlayers());
        }
    }

    public GameState getGameState() {
        return gamestate;
    }

    public void setGameState(GameState gamestate) {
        this.gamestate = gamestate;
    }

    public boolean validateAction(Action action) {
        return roundController.getTurnState().isPossible(action);
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
        gameBoardInstance.setPublicCommunication(roundController.getWinnerPlayer() + "", CommunicationMessage.END_GAME);

        // Now that the game has ended, we delete the saved game file since an ended game cannot be restarted.
        GameSaver.deleteSavedGame();
    }

    public RoundController getRoundController(){
        return roundController;
    }

    public LobbyController getLobby() {
        return lobby;
    }

    public void setLocalView(View view){
        this.localView = view;
    }

    private void restoreGame(GameController savedGameController){
        gamestate = savedGameController.gamestate;
        gameBoardInstance = savedGameController.gameBoardInstance;

        roundController.restoreRoundController(savedGameController.getRoundController(), gameBoardInstance, this);

        // Add observers
        for(HumanPlayer player : gameBoardInstance.getPlayers()){
            Observer playerView = viewMap.get(player.getName());
            setViewObservers(playerView);
        }
        for (HumanPlayer player : gameBoardInstance.getPlayers())
            for (HumanPlayer player1 : gameBoardInstance.getPlayers())
                player.addObserver(viewMap.get(player1.getName()));
    }
}
