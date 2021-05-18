package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private int playerNumber;
    private TurnState turnState;
    private CompressedPlayerBoard playerBoard;
    /**
     * Default constructor.
     */
    public Cli() {
        out = System.out;
        ClientManager clientManager = new ClientManager(this);
        this.addObserver(clientManager);
        init();
    }

    public String readLine() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new ReadingThread());
        Thread inputThread = new Thread(futureTask);
        inputThread.start();

        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    /**
     * Starts the command-line interface.
     */
    public void init() {

        out.println("Welcome master of the Renaissance");

        try {
            askServerInfo();
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    public void askServerInfo() throws ExecutionException {
        String address, port;
        String defaultAddress = "127.0.0.1";
        String defaultPort = "12222";
        boolean validInput;

        out.println("Please specify the following settings. The default value is shown between brackets.");

        do {
            out.print("Enter the server address [" + defaultAddress + "]: ");

            address = readLine();

            if (address.equals("")) {
                address = defaultAddress;
                validInput = true;
            } else if (ClientManager.isValidIpAddress(address)) {
                validInput = true;
            } else {
                out.println("Invalid address!");
                clearCli();
                validInput = false;
            }
        } while (!validInput);

        do {
            out.print("Enter the server port [" + defaultPort + "]: ");
            port = readLine();

            if (port.equals("")) {
                port = defaultPort;
                validInput = true;
            } else {
                if (ClientManager.isValidPort(port)) {
                    validInput = true;
                } else {
                    out.println("Invalid port!");
                    validInput = false;
                }
            }
        } while (!validInput);

        String finalAddress = address;
        String finalPort = port;

        notifyObserver(obs -> obs.ServerInfo(finalAddress, finalPort));
    }


    @Override
    public void askUsername() {
        out.print("Enter your nickname: ");
        try {
            String nickname = readLine();
            notifyObserver(obs -> obs.Nickname(nickname));
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 1 or 4 players): ";

        try {
            playerNumber = validateInput(1, 4, null, question);
            notifyObserver(obs -> obs.PlayersNumber(playerNumber));
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    public void askJoinOrSet() {
        clearCli();
        String join = "JOIN";
        String set = "SET";
        out.println("Do you want to JOIN a game or to SET a game?(You can choose between JOIN or SET)");
        try {
            String s = readLine();
            if (s.equals(join) || s.equals("join") || s.equals("j") || s.equals("J"))
                joinLobby();
            else if (s.equals(set) || s.equals("set") || s.equals("s") || s.equals("S"))
                askPlayersNumber();
            else {
                clearCli();
                askJoinOrSet();
            }
        } catch (ExecutionException e) {
            out.println("Error");
        }

    }

    @Override
    public void joinLobby() {
        notifyObserver(ViewObserver::addPlayerLobby);
    }

    @Override
    public void askAction(TurnState state) {
        this.turnState = state;
        out.println(state);
        switch (turnState) {
            case FIRST_TURN:
                askFirstAction();
                break;
            case SECOND_TURN:
                askSecondAction();
                break;
            default:
                askWhichAction();
        }

    }

    private void askWhichAction() {
        int action;
        Action act;
        ArrayList<Action> possibilities = TurnState.possibleActions(turnState);
        if (playerBoard.getPlayerBoard().getLeaderCardsNumber() == 0) {
            possibilities.remove(Action.ACTIVE_LEADER);
            possibilities.remove(Action.FOLD_LEADER);
        }
        out.println("\nPossible actions:");
        for (Action action1 : possibilities) {
            out.println(possibilities.indexOf(action1) + ". " + action1);
        }
        String question = "Select the action you want to perform:(the number)";
        try {
            action = validateInput(0, possibilities.size() - 1, null, question);
            act = possibilities.get(action);
            switch (act) {
                case ACTIVE_LEADER:
                    askActiveLeader();
                    break;
                case FOLD_LEADER:
                    askFoldLeader();
                    break;
                case END_TURN:
                    notifyObserver(ViewObserver::endTurn);
                    break;
                case GET_RESOURCES_FROM_MARKET:
                    askGetMarket();
                    break;
                case SORTING_WAREHOUSE:
                    askSortingMarket();
                    break;
                case SHIFT_WAREHOUSE:
                    askSwitchRows();
                    break;
                case BUY_DEVELOPMENT_CARD:
                    askGetProduction();
                    break;
                case USE_PRODUCTIONS:
                    askUseProduction();
                    break;
            }
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    private void askUseProduction() {

    }

    @Override
    public void askFirstAction() {
        int firstCard, secondCard;
        String question = "Which cards do you want to discard? Select 1, between 0 and 3:";
        try {
            firstCard = validateInput(0, 3, null, question);
            ArrayList<Integer> card = new ArrayList<>(1);
            card.add(firstCard);
            question = "Which cards do you want to discard? Select 1, between 0 and 3(except " + firstCard + "):";
            secondCard = validateInput(0, 3, card, question);
            notifyObserver(obs -> obs.firstAction(firstCard, secondCard));
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    @Override
    public void askSecondAction() {
        ArrayList<Resources> jumpList = new ArrayList<>();
        jumpList.add(Resources.FAITH);
        jumpList.add(Resources.WHITE);
        jumpList.add(Resources.WHATEVER);
        Resources resources;
        ArrayList<Resources> resourcesToSend = new ArrayList<>();
        String question = "Choose 1 Resource between COIN,SHIELD,SERVANT,STONE:";

        if (playerNumber == 0)
            out.println("You are the first player.We are waiting other players to finish their setup...");
        else
            try {
                out.println("You are the" + playerNumber + " player.\n");
                resources = validateResources(question, jumpList);
                resourcesToSend.add(resources);
                if (playerNumber == 3) {
                    resources = validateResources(question, jumpList);
                    resourcesToSend.add(resources);
                }
                notifyObserver(obs -> obs.secondAction(resourcesToSend));
            } catch (ExecutionException e) {
                out.println("Error");
            }
    }


    @Override
    public void askGetMarket() {

    }

    @Override
    public void askSortingMarket() {

    }

    @Override
    public void askSwitchRows() {

    }

    @Override
    public void askGetProduction() {

    }

    @Override
    public void askUseBaseProduction() {

    }

    @Override
    public void askUseSpecialProduction() {

    }

    @Override
    public void askUseNormalProduction() {

    }

    @Override
    public void askFoldLeader() {
        int foldCard;
        int numCards = playerBoard.getPlayerBoard().getLeaderCardsNumber() - 1;
        String question = "Which cards do you want to discard? Select 1, between 0 and " + numCards + ":";
        try {
            foldCard = validateInput(0, numCards, null, question);
            notifyObserver(obs -> obs.foldLeader(foldCard));
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    @Override
    public void askActiveLeader() {

    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {
        clearCli();
        if (accepted)
            out.println("your connection is established, " + name);
        else {
            out.println("Could not find the server");
            System.exit(1);
        }
        if (!nick) {
            out.println("Your username is already taken, try another one or the lobby is full");
            askUsername();
        } else {
            out.println("your username is accepted, welcome " + name);
            askJoinOrSet();
        }

    }

    @Override
    public void showLobby(ArrayList<String> players) {
        clearCli();
        out.println("Players connected: " + players + "\n Waiting for other players...");

    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {
        clearCli();
        out.println(type);
        out.println(communication);
        if (type.equals(CommunicationMessage.ILLEGAL_LOBBY_ACTION))
            askJoinOrSet();
        if (type.equals(CommunicationMessage.PLAYER_NUMBER))
            this.playerNumber = Integer.parseInt(communication);
        if (type.equals(CommunicationMessage.ILLEGAL_ACTION))
            askWhichAction();
    }

    @Override
    public void showError(String error) {
        clearCli();
        out.println(error);
    }

    @Override
    public void showPlayerBoard(CompressedPlayerBoard playerBoard) {
        clearCli();
        this.playerBoard = playerBoard;
        out.println(playerBoard);
    }

    @Override
    public void showFaithPath(FaithPath faithPath) {
        out.println(faithPath);
    }

    @Override
    public void showDecks(Decks decks) {
        out.println(decks);
    }

    @Override
    public void showMarket(Market market) {
        out.println(market);
    }


    private Resources validateResources(String question, List<Resources> jumpList) throws ExecutionException {
        Resources resources = Resources.WHATEVER;
        if (jumpList == null) {
            jumpList = List.of();
        }
        do {
            try {
                out.print(question);
                resources = Resources.valueOf(readLine());

                if (jumpList.contains(resources)) {
                    out.println("This resource cannot be selected! Please try again." + jumpList + " cannot be selected \n");
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input! Please try again.\n");
            }
        } while (jumpList.contains(resources));

        return resources;
    }
    private int validateInput(int minValue, int maxValue, List<Integer> jumpList, String question) throws ExecutionException {
        int number = minValue - 1;

        // A null jumpList will be transformed in a empty list.
        if (jumpList == null) {
            jumpList = List.of();
        }

        do {
            try {
                out.print(question);
                number = Integer.parseInt(readLine());

                if (number < minValue || number > maxValue) {
                    out.println("Invalid number! Please try again.\n");
                } else if (jumpList.contains(number)) {
                    out.println("This number cannot be selected! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue || jumpList.contains(number));

        return number;
    }

    public void clearCli() {
        out.flush();
    }
}