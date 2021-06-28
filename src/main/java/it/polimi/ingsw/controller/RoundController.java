package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.PlayerDisconnectionState;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.utils.GameSaver;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * This class handles all possible turn actions.
 */
public class RoundController implements Serializable {

    private ActionController actionController;
    private GameBoard gameBoardInstance;

    private HumanPlayer playerInTurn;
    private ArrayList<HumanPlayer> players;
    private ArrayList<Integer> productions;
    private transient final Object productionLock;

    private int turnCount;
    private TurnState turnState;
    private GameState gameState;

    private int winnerPlayer;
    private boolean Winner;

    private GameController gameController;

    /**
     * Create a new Round controller with 0 player.
     *
     * @param gameController    Game controller that's controlling this game
     * @param gameBoardInstance Instance of the game of this game controller.
     */
    public RoundController(GameBoard gameBoardInstance, GameController gameController) {
        this.turnCount = 0;
        this.gameBoardInstance = gameBoardInstance;
        this.players = gameBoardInstance.getPlayers();
        this.turnState = TurnState.FIRST_TURN;
        this.productionLock = new Object();
        synchronized (productionLock) {
            this.productions = new ArrayList<>(4);
        }
        this.actionController = new ActionController();
        this.gameController = gameController;


        this.Winner = false;
        this.winnerPlayer = -2;
    }

    /**
     * Restore the round controller.
     *
     * @param gameController     Game controller that was controlling this game
     * @param gameBoard          Instance of the game of this game controller.
     * @param restoredController Restored round controller.
     */
    public void restoreRoundController(RoundController restoredController, GameBoard gameBoard, GameController gameController) {
        this.actionController = restoredController.actionController;
        this.gameBoardInstance = gameBoard;

        this.playerInTurn = restoredController.playerInTurn;
        this.players = restoredController.players;
        synchronized (productionLock) {
            this.productions = restoredController.productions;
        }

        this.turnCount = restoredController.turnCount;
        this.turnState = restoredController.turnState;
        this.gameState = restoredController.gameState;

        this.gameController = gameController;
    }

    /**
     * Initialize the current game.Sets the game mode to multiplayer or singleplayer depending from the number of players.Sets the player in turn(the first player).
     */
    public void init() {
        if (players.size() == 1)
            gameState = GameState.SINGLEPLAYER;
        else
            gameState = GameState.MULTIPLAYER;
        this.playerInTurn = gameBoardInstance.getPlayers().get(0);
    }

    /**
     * Handle the request from the player to get a row/column from the market.
     * Clear the resources received from the market :
     * -if the player cannot exchange the white, it removes them.
     * -if the player can exchange the white with only one resource, it will exchange all of the white.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_getMarket(MarketRequestMessage message) {
        // FIXME: Handle these tests
        playerInTurn.getPlayerBoard().addStrongboxResource(Resources.STONE, 10);
        playerInTurn.getPlayerBoard().addStrongboxResource(Resources.SHIELD, 10);
        playerInTurn.getPlayerBoard().addStrongboxResource(Resources.SERVANT, 10);
        playerInTurn.getPlayerBoard().addStrongboxResource(Resources.COIN, 10);


        if (isYourTurn(message.getPlayerName())) {
            ArrayList<Resources> list;
            try {
                list = actionController.getMarket(gameBoardInstance, message.getRowIndex(), message.getColIndex());
            } catch (IllegalActionException e) {
                playerInTurn.setPrivateCommunication("Indexes must be between 0 and 3", CommunicationMessage.ILLEGAL_ACTION);
                return;
            }
            // remove faith element and add to the faith path
            if (list.contains(Resources.FAITH)) {
                handle_addFaithPoint(1, playerInTurn);
                list.remove(Resources.FAITH);
            }

            if (list.contains(Resources.WHITE)) {
                // TODO: there may be a bug in the white marble decorator
                int flag = (int) playerInTurn.getPlayerBoard().getSubstitutes().stream().filter(n -> n).count();
                //exchange white resource with another resource if there is only one exchangeable
                if (flag == 1) {
                    Resources resource = Resources.transform(playerInTurn.getPlayerBoard().getSubstitutes().indexOf(true));
                    for (Resources resources : list)
                        if (resources.equals(Resources.WHITE))
                            list.set(list.indexOf(Resources.WHITE), resource);
                }
                //remove white resource cause with have no exchange
                else if (flag == 0) {
                    while (list.contains(Resources.WHITE))
                        list.remove(Resources.WHITE);
                }
            }
            playerInTurn.setTemporaryResourceStorage(list);
            nextState(Action.GET_RESOURCES_FROM_MARKET);
        }
    }

    /**
     * Handle the request from the player to switch rows.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_shiftWarehouse(ShiftWarehouseMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            actionController.shiftWarehouseRows(playerInTurn, message.getStartingPos(), message.getNewRowPos());
        }
        nextState(Action.SORTING_WAREHOUSES);
    }

    /**
     * Handle the request from the player to move a resource between the warehouses.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_moveBetweenWarehouse(moveBetweenWarehouseMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            actionController.moveResourceToWarehouse(playerInTurn, message.getResource(), message.getPosition(), message.getNewPosition());
        }
        nextState(Action.SORTING_WAREHOUSES);
    }

    /**
     * Handle the request from the player to move a resource from the temporary storage to the warehouses.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_sortingWarehouse(SetResourceMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (message.getResource().ordinal() >= 4) {
                playerInTurn.setPrivateCommunication("The resource you sent is not SHIELD,STONE,SERVANT,COIN.", CommunicationMessage.ILLEGAL_ACTION);
                return;
            }
            try {
                if (actionController.addResourceToWarehouse(playerInTurn, message.getResource(), message.getPosition(), message.getReceivedResourceIndex()))
                    nextState(Action.SORTING_TEMPORARY_STORAGE);
            } catch (IllegalDecoratorException e) {
                playerInTurn.setPrivateCommunication("You don't have a special warehouse", CommunicationMessage.ILLEGAL_ACTION);

            }
        }
    }

    /**
     * Handle the request from the player to discard a resource from the temporary storage.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_discardResource(DiscardResourceMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (actionController.discardResource(playerInTurn, message.getReceivedResourceIndex())) {
                movePlayersExceptSelected(1);
                nextState(Action.SORTING_TEMPORARY_STORAGE);
            }
        }
    }

    /**
     * Handle the request from the player to use the base production.
     * Checks if the player already used this production.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_useBaseProduction(UseProductionBaseMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (productions.contains(3))
                playerInTurn.setPrivateCommunication("You already used this production", CommunicationMessage.ILLEGAL_ACTION);
            else {
                if (actionController.useBaseProduction(playerInTurn, 2, message.getOutput(), message.getExchangeResources())) {
                    productions.add(3);
                    nextState(Action.USE_PRODUCTIONS);
                }
            }
        }
    }

    /**
     * Handle the request from the player to use the normal production.
     * Checks if the player already used this production.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_useNormalProduction(UseProductionNormalMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (productions.contains(message.getIndex()))
                playerInTurn.setPrivateCommunication("You already used this production", CommunicationMessage.ILLEGAL_ACTION);
            else {
                if (actionController.useNormalProduction(playerInTurn, message.getIndex(), message.getExchangeResources())) {
                    productions.add(message.getIndex());
                    int faith = playerInTurn.getPlayerBoard().getProductionResult(message.getIndex())[Resources.FAITH.ordinal()];
                    handle_addFaithPoint(faith, playerInTurn);
                    nextState(Action.USE_PRODUCTIONS);
                }
            }
        }

    }

    /**
     * Handle the request from the player to use the special production.
     * Checks if the player already used this production.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_useSpecialProduction(UseProductionSpecialMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (productions.contains(message.getIndex() + 4))
                playerInTurn.setPrivateCommunication("You already used this production", CommunicationMessage.ILLEGAL_ACTION);
            else {
                if (actionController.useSpecialProduction(playerInTurn, message.getOutput(), message.getIndex(), message.getExchangeResources())) {
                    productions.add(message.getIndex() + 4);
                    handle_addFaithPoint(1, playerInTurn);
                    nextState(Action.USE_PRODUCTIONS);
                }
            }
        }
    }

    /**
     * Handle the request from the player to get a new development card from the decks on the game board.
     * Checks if the player win the game.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_getProduction(GetProductionCardMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            try {
                if (actionController.getProduction(message.getColor(), message.getLevel(), gameBoardInstance, message.getIndex(), playerInTurn, message.getExchangeResources()))
                    nextState(Action.BUY_DEVELOPMENT_CARD);
            } catch (WinnerException e) {
                if (winnerPlayer == -2) {
                    winnerPlayer = players.indexOf(playerInTurn);
                    if (gameState == GameState.SINGLEPLAYER) {
                        nextTurn();
                    }
                }
            }
        }
    }

    /**
     * Handle the request from the player to active a leader card.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_activeLeader(LeaderMessage message) {
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState = TurnState.NORMAL_ACTION;
            playerInTurn.setPrivateCommunication("You cannot do leader action", CommunicationMessage.ILLEGAL_ACTION);
        } else if (isYourTurn(message.getPlayerName())) {
            if (actionController.activateLeader(message.getIndex(), playerInTurn))
                nextState(Action.ACTIVE_LEADER);
        }
        clearTemporaryStorage();

    }

    /**
     * Handle the request from the player to discard a leader card.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_foldLeader(LeaderMessage message) {
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState = TurnState.NORMAL_ACTION;
            playerInTurn.setPrivateCommunication("You cannot do leader action", CommunicationMessage.ILLEGAL_ACTION);
        } else if (isYourTurn(message.getPlayerName())) {
            if (actionController.foldLeader(message.getIndex(), playerInTurn)) {
                handle_addFaithPoint(1, playerInTurn);
                nextState(Action.ACTIVE_LEADER);
            }
        }
        clearTemporaryStorage();

    }

    /**
     * Handle the first action of a player.The player needs to discard 2 leader cards
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_firstAction(FirstActionMessage message) {
        boolean flag = false;
        synchronized (productionLock) {
            for (HumanPlayer player : players) {
                if (player.getName().equals(message.getPlayerName())) {
                    flag = true;
                    int n = players.indexOf(player);
                    if (productions.contains(n))
                        flag = false;
                    else {
                        if (actionController.firstAction(message.getIndex1(), message.getIndex2(), player)) {
                            productions.add(n);
                            nextState(Action.END_TURN);
                            break;
                        }
                    }
                }
            }
            if (!flag)
                gameBoardInstance.getPlayer(message.getPlayerName()).setPrivateCommunication("This is not your turn", CommunicationMessage.ILLEGAL_ACTION);

        }
    }

    /**
     * Handle the second action of a player.The player need to choose a number of resources.
     * Sets the private communication of the player in turn to an appropriate error message if they can't perform this action.
     *
     * @param message Message that contains all of the information needed to perform this action.
     */
    public void handle_secondAction(SecondActionMessage message) {
        boolean flag = false;
        synchronized (productionLock) {
            for (HumanPlayer player : players) {
                if (player.getName().equals(message.getPlayerName())) {
                    flag = true;
                    int n = players.indexOf(player);
                    if (productions.contains(n))
                        flag = false;

                    else {
                        if ((message.getResources() == null && player.getPlayerState().equals(PlayerDisconnectionState.TERMINAL)) || actionController.secondAction(message.getResources(), n, player)) {
                            if (n >= 2)
                                actionController.addFaithPoint(gameBoardInstance, player, 1);
                            productions.add(n);
                            nextState(Action.END_TURN);
                            break;
                        }
                    }
                }
            }
            if (!flag)
                gameBoardInstance.getPlayer(message.getPlayerName()).setPrivateCommunication("You cannot do this action", CommunicationMessage.ILLEGAL_ACTION);

        }
    }

    /**
     * Adds a number of faith points to the player.
     * Checks if the player win.
     *
     * @param quantities Number of faith point to add.
     * @param player     Player of interest.
     */
    public void handle_addFaithPoint(int quantities, HumanPlayer player) {
        try {
            actionController.addFaithPoint(gameBoardInstance, player, quantities);
        } catch (WinnerException e) {
            if (winnerPlayer == -2) {
                    winnerPlayer = players.indexOf(player);
                if (gameState == GameState.SINGLEPLAYER) {
                    nextTurn();
                    if (player == null)
                        winnerPlayer = -1;
                }
            }
        }

    }

    /**
     * Deal out 4 random leader cards to each player
     */
    public void handle_firstTurn() {
        ArrayList<LeaderCard> leaderCards = CardParser.parseLeadCards();
        if (leaderCards == null)
            throw new IllegalActionException();
        else
            for (HumanPlayer player : players) {
                for (int i = 0; i < 4; i++) {
                    player.getPlayerBoard().addLeaderCard(leaderCards.get(0));
                    leaderCards.remove(0);
                }
            }
        for (HumanPlayer player : players) {
            player.sendUpdateToPlayer();
            player.setState(TurnState.FIRST_TURN);
        }
    }

    /**
     * End the turn of the player in turn.
     */
    public void handle_endTurn() {
        nextTurn();
    }

    /**
     * Adds a number of faith points to all of the players except the player in turn.
     *
     * @param quantities Number of faith point to add.
     */
    public void movePlayersExceptSelected(int quantities) {
        if (gameState.equals(GameState.MULTIPLAYER)) {
            for (HumanPlayer player : players)
                if (!player.getName().equals(playerInTurn.getName()))
                    handle_addFaithPoint(quantities, player);
        } else if (gameState.equals(GameState.SINGLEPLAYER))
            handle_addFaithPoint(quantities, null);

    }

    /**
     * Clears the temporary storage of the player in turn.
     */
    private void clearTemporaryStorage() {
        int quantities = playerInTurn.getTemporaryResourceStorage().size();
        movePlayersExceptSelected(quantities);
        playerInTurn.setTemporaryResourceStorage(new ArrayList<>());
    }

    /**
     * Sets the new player in turn.
     * Clears the temporary storage and the productions.
     * Move the bot player, if it's single player.
     * Saves the game.
     */
    public void nextTurn() {
        //clearing checks
        productions.clear();
        clearTemporaryStorage();

        if (gameState.equals(GameState.SINGLEPLAYER))
            handle_Bot();
        else
            playerInTurn.setState(TurnState.NOT_IN_TURN);

        if (gameController.getInstance().getActivePlayersCount() != 0)
            goToNextTurn();
        if(!gameController.isLocal())
            GameSaver.saveGame(gameController);
    }

    /**
     * Sets the State of the new player in turn.
     * Clears the temporary storage and the productions.
     * Move the bot player, if it's single player
     */
    public void goToNextTurn() {
        do {
            turnCount++;
            checkWinner(players.size());
            playerInTurn = players.get((turnCount) % players.size());
        } while (!playerInTurn.getPlayerState().equals(PlayerDisconnectionState.ACTIVE));
        firstState();
        playerInTurn.setState(turnState);
    }

    /**
     * Checks if there ia winner and ends the game.
     *
     * @param playersNumber Number of players.
     */
    private void checkWinner(int playersNumber) {
        switch (gameState) {
            case MULTIPLAYER: {
                if (players.get(turnCount % playersNumber).getPlayerBoard().getInkwell() && winnerPlayer >= 0) {
                    Winner = true;
                    turnState = TurnState.END;
                }
                break;
            }
            case SINGLEPLAYER: {
                if (winnerPlayer == 0 || winnerPlayer == -1) {
                    Winner = true;
                    turnState = TurnState.END;
                }
                break;
            }

        }
    }

    /**
     * Handles the bot actions.
     */
    private void handle_Bot() {
        if (!Winner) {
            try {
                gameBoardInstance.getBot().doAction();
            } catch (WinnerException e) {
                winnerPlayer = -1;
                Winner = true;
                turnState = TurnState.END;
            }
        }
    }

    /**
     * Checks if the player in turn correspond tot he player that sent the message.
     *
     * @param name Nickname of the player to check.
     * @return true if is your turn.
     */
    private boolean isYourTurn(String name) {
        if (playerInTurn.getName().equals(name))
            return true;
        else {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("This is not your turn", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }
    }

    /**
     * Sets the state to all the player.
     *
     * @param state State to set.
     */
    private void setStateToAll(TurnState state) {
        for (HumanPlayer player : players) {
            player.setState(state);
        }
    }

    /**
     * Sets the first state that the player in turn needs to do.
     */
    private void firstState() {
        if (!turnState.equals(TurnState.END))
            turnState = TurnState.FIRST_LEADER_ACTION;
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber())
            turnState = TurnState.NORMAL_ACTION;
    }

    /**
     * Sets the next state that the player in turn needs to do.
     *
     * @param action Action the player performed.
     */
    public synchronized void nextState(Action action) {
        boolean flag = (!turnState.equals(TurnState.FIRST_TURN) && !turnState.equals(TurnState.SECOND_TURN));
        if (!action.equals(Action.SORTING_WAREHOUSES))
            switch (turnState) {
                case FIRST_TURN: {
                    if (players.size() == productions.size()) {
                        if (gameState == GameState.MULTIPLAYER) {
                            turnState = TurnState.SECOND_TURN;
                            productions.clear();
                            productions.add(0);
                            // For each disconnected player add corresponding actions
                            for(HumanPlayer player : players){
                                if(player.getPlayerState().equals(PlayerDisconnectionState.TERMINAL)){
                                    productions.add(0);
                                }
                            }

                            setStateToAll(TurnState.SECOND_TURN);
                        } else if (gameState.equals(GameState.SINGLEPLAYER)) {
                            turnState = TurnState.FIRST_LEADER_ACTION;
                            setStateToAll(TurnState.FIRST_LEADER_ACTION);
                        }
                    }
                    break;
                }
                case SECOND_TURN: {
                    if (players.size() == productions.size()) {
                        productions.clear();
                        turnState = TurnState.FIRST_LEADER_ACTION;
                        for (int i = 1; i < players.size(); i++)
                            players.get(i).setState(TurnState.NOT_IN_TURN);
                        playerInTurn.setState(TurnState.FIRST_LEADER_ACTION);
                    }
                    break;
                }
                case FIRST_LEADER_ACTION: {
                    if (action.equals(Action.ACTIVE_LEADER)) {
                        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
                            turnState = TurnState.NORMAL_ACTION;
                            break;
                        }
                        break;
                    }
                    turnState = TurnState.NORMAL_ACTION;
                }
                case NORMAL_ACTION: {
                    if (action.equals(Action.USE_PRODUCTIONS)) {
                        turnState = TurnState.PRODUCTION_ACTIONS;
                        break;
                    } else if (action.equals(Action.GET_RESOURCES_FROM_MARKET)) {
                        if (playerInTurn.getTemporaryResourceStorage().size() == 0) {
                            turnState = TurnState.LAST_LEADER_ACTION;
                        } else {
                            turnState = TurnState.WAREHOUSE_ACTION;
                        }
                        break;
                    } else {
                        turnState = TurnState.LAST_LEADER_ACTION;
                    }
                }
                case WAREHOUSE_ACTION: {
                    if (action.equals(Action.SORTING_TEMPORARY_STORAGE)) {
                        if (playerInTurn.getTemporaryResourceStorage().size() == 0) {
                            turnState = TurnState.LAST_LEADER_ACTION;
                            break;
                        }
                    } else {
                        if (action.equals(Action.ACTIVE_LEADER))
                            turnState = TurnState.LAST_LEADER_ACTION;
                        else break;
                    }
                }
                case PRODUCTION_ACTIONS: {

                    if (action.equals(Action.USE_PRODUCTIONS)) {
                        if (productions.size() == playerInTurn.getPlayerBoard().getProductionNumber()) {
                            turnState = TurnState.LAST_LEADER_ACTION;
                            break;
                        }
                    } else if (action.equals(Action.ACTIVE_LEADER))
                        turnState = TurnState.LAST_LEADER_ACTION;
                    else break;
                }
                case LAST_LEADER_ACTION: {
                    if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
                        nextTurn();
                    }
                    break;
                }

            }
        if (flag)
            playerInTurn.setState(turnState);

    }

    /**
     * Gets the turnState.
     *
     * @return the turnState.
     */
    public TurnState getTurnState() {
        return turnState;
    }

    /**
     * Gets the playerInTurn.
     *
     * @return the playerInTurn.
     */
    public HumanPlayer getPlayerInTurn() {
        return playerInTurn;
    }

    /**
     * Gets the winner.
     *
     * @return true if the game ended.
     */
    public boolean isWinner() {
        return Winner;
    }

    /**
     * Gets the gameState.
     *
     * @return the gameState.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Gets the actionController.
     *
     * @return the actionController.
     */
    public ActionController getActionController() {
        return actionController;
    }

    /**
     * Gets the winner player(-1 for the bot).
     *
     * @return the winner player.
     */
    public int getWinnerPlayer() {
        return winnerPlayer;
    }
}


