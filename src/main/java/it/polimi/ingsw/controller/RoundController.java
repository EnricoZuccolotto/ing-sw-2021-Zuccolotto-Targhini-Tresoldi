package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.playerboard.IllegalActionException;
import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;


public class RoundController {

    private final ActionController actionController;
    private final GameBoard gameBoardInstance;

    private HumanPlayer playerInTurn;
    private final ArrayList<HumanPlayer> players;
    private final ArrayList<Integer> productions;

    private int turnCount;
    private TurnState turnState;
    private GameState gameState;

    private int winnerPlayer;
    private boolean Winner;


    public RoundController(GameBoard gameBoardInstance) {
        this.turnCount = 0;
        this.gameBoardInstance = gameBoardInstance;
        this.players = gameBoardInstance.getPlayers();
        this.turnState = TurnState.FIRST_TURN;
        this.productions = new ArrayList<>(4);
        this.actionController = new ActionController();

        this.Winner = false;
        this.winnerPlayer = -2;
    }

    public void init() {
        if (players.size() == 1)
            gameState = GameState.SINGLEPLAYER;
        else
            gameState = GameState.MULTIPLAYER;
        this.playerInTurn = gameBoardInstance.getPlayers().get(0);
    }

    public void setPlayerInTurn(HumanPlayer player) {
        this.playerInTurn = player;
    }

    public void handle_getMarket(MarketRequestMessage message) {
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
    public void handle_shiftWarehouse(ShiftWarehouseMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            actionController.shiftWarehouseRows(playerInTurn, message.getStartingPos(), message.getNewRowPos());
        }
        nextState(Action.SHIFT_WAREHOUSE);
    }
    public void handle_sortingWarehouse(SetResourceMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            if (message.getResource().ordinal() >= 4) {
                playerInTurn.setPrivateCommunication("The resource you sent is not SHIELD,STONE,SERVANT,COIN.", CommunicationMessage.ILLEGAL_ACTION);
                return;
            }
            try {
                if (actionController.addResourceToWarehouse(playerInTurn, message.getResource(), message.getPosition(), message.getReceivedResourceIndex()))
                    nextState(Action.SORTING_WAREHOUSE);
            } catch (IllegalDecoratorException e) {
                playerInTurn.setPrivateCommunication("You don't have a special warehouse", CommunicationMessage.ILLEGAL_ACTION);

            }
        }
    }
    public void handle_discardResource(DiscardResourceMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            movePlayersExceptSelected(1);
        }
    }

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

    public void handle_getProduction(GetProductionCardMessage message) {
        if (isYourTurn(message.getPlayerName())) {
            try {
                if (actionController.getProduction(message.getColor(), message.getLevel(), gameBoardInstance, message.getIndex(), playerInTurn, message.getExchangeResources()))
                    nextState(Action.BUY_DEVELOPMENT_CARD);
            } catch (WinnerException e) {
                winnerPlayer = players.indexOf(playerInTurn);
                if (gameState == GameState.SINGLEPLAYER) {
                    nextTurn();
                }
            }
        }
    }

    public void handle_activeLeader(LeaderMessage message) {
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber()) {
            turnState = TurnState.NORMAL_ACTION;
            playerInTurn.setPrivateCommunication("You cannot do leader action", CommunicationMessage.ILLEGAL_ACTION);
        } else if (isYourTurn(message.getPlayerName())) {
            if (actionController.activateLeader(message.getIndex(), playerInTurn))
                nextState(Action.ACTIVE_LEADER);
        }
    }

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
    }

    public void handle_firstAction(FirstActionMessage message) {
        boolean flag = false;
        synchronized (productions) {
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

    public void handle_secondAction(SecondActionMessage message){
        boolean flag=false;
        synchronized (productions){
            for (HumanPlayer player : players) {
                if (player.getName().equals(message.getPlayerName())) {
                    flag = true;
                    int n=players.indexOf(player);
                    if (productions.contains(n))
                        flag = false;

                    else {
                        if (actionController.secondAction(message.getResources(), n, player)) {
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

    public void handle_addFaithPoint(int quantities, HumanPlayer player) {
        try {
            actionController.addFaithPoint(gameBoardInstance, player, quantities);
        } catch (WinnerException e) {
            winnerPlayer = players.indexOf(player);
            if (gameState == GameState.SINGLEPLAYER) {
                nextTurn();
            }
        }

    }


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
            player.setPrivateCommunication("" + players.indexOf(player), CommunicationMessage.PLAYER_NUMBER);
        }
    }

    public void handle_endTurn() {
        nextTurn();
    }

    public void movePlayersExceptSelected(int quantities) {
        if (gameState.equals(GameState.MULTIPLAYER)) {
            for (HumanPlayer player : players)
                if (!player.getName().equals(playerInTurn.getName()))
                    handle_addFaithPoint(quantities, player);
        } else if (gameState.equals(GameState.SINGLEPLAYER))
            handle_addFaithPoint(quantities, null);
    }

    void nextTurn() {
        //clearing checks
        int playersNumber = players.size();
        turnCount++;
        productions.clear();
        int quantities = playerInTurn.getTemporaryResourceStorage().size();
        movePlayersExceptSelected(quantities);

        checkWinner(playersNumber);
        if (gameState.equals(GameState.SINGLEPLAYER))
            handle_Bot();
        else
            playerInTurn.setState(TurnState.NOT_IN_TURN);

        playerInTurn = players.get((turnCount) % players.size());
        firstState();
        playerInTurn.setState(turnState);
    }

    private void checkWinner(int playersNumber){
        switch (gameState){
            case MULTIPLAYER:{
                if(players.get(turnCount%playersNumber).getPlayerBoard().getInkwell()&&winnerPlayer>=0) {
                    Winner = true;
                    turnState=TurnState.END;
                }break;}
            case SINGLEPLAYER:{
                if(winnerPlayer==0||winnerPlayer==-1)
                {
                    Winner = true;
                    turnState=TurnState.END;
                }break;}

        }
    }
    private void handle_Bot(){
        if(!Winner){
        try{
            gameBoardInstance.getBot().doAction();
        }
        catch (WinnerException e){
            winnerPlayer=-1;
            Winner = true;
            turnState=TurnState.END;
        }}
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public HumanPlayer getPlayerInTurn() {
        return playerInTurn;
    }

    public boolean isWinner() {
        return Winner;
    }

    private boolean isYourTurn(String name) {
        if (playerInTurn.getName().equals(name))
            return true;
        else {
            gameBoardInstance.getPlayer(name).setPrivateCommunication("This is not your turn", CommunicationMessage.ILLEGAL_ACTION);
            return false;
        }
    }

    private void setStateToAll(TurnState state) {
        for (HumanPlayer player : players) {
            player.setState(state);
        }
    }

    private void firstState() {
        if (!turnState.equals(TurnState.END))
            turnState = TurnState.FIRST_LEADER_ACTION;
        if (0 == playerInTurn.getPlayerBoard().getLeaderCardsNumber())
            turnState = TurnState.NORMAL_ACTION;
    }

    public void nextState(Action action) {
        boolean flag = (!turnState.equals(TurnState.FIRST_TURN) && !turnState.equals(TurnState.SECOND_TURN));
        if (!action.equals(Action.SHIFT_WAREHOUSE))
            switch (turnState) {
                case FIRST_TURN: {
                    if (players.size() == productions.size()) {
                        if (gameState == GameState.MULTIPLAYER) {
                            turnState = TurnState.SECOND_TURN;
                            productions.clear();
                            productions.add(0);
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
                    if (action.equals(Action.SORTING_WAREHOUSE)) {
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

    public GameState getGameState() {
        return gameState;
    }

    public ActionController getActionController() {
        return actionController;
    }

    public int getWinnerPlayer() {
        return winnerPlayer;
    }
}


