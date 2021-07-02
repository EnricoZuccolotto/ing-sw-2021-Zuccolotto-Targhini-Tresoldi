package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.communication.CommunicationMessage;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.BotActions;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.model.tools.ExchangeResources;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * This class contains the Command Line Interface handlers and actions.
 */
public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private int playerNumber;
    private final ArrayList<CompressedPlayerBoard> boards;
    boolean local;

    private TurnState turnState;
    private String nickname;
    private boolean winner;
    private Decks decks;
    private Market market;
    private FaithPath faithPath;
    private ClientManager clientManager;

    /**
     * Default constructor.
     */
    public Cli() {
        out = System.out;
        boards = new ArrayList<>(4);
        for(int i = 0; i < 4; i++){
            boards.add(null);
        }
        turnState = TurnState.END;
        init();
    }


    @Override
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Reads the line of the cli.
     *
     * @return what the player's written on the cli.
     */
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

        String string = "0 for local" + "\n" +
                "1 for online: ";
        try {
            int choice = validateInput(0, 1, null, string);
            if (choice == 0)
                initLocal();
            else askServerInfo();
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    /**
     * Starts a local game.
     */
    public void initLocal() {
        GameController gameController = new GameController(true);
        gameController.setLocalView(this);
        clientManager = new ClientManager(this, gameController);
        this.addObserver(clientManager);
        local = true;
        askUsername();
    }

    /**
     * Asks the server info.
     */
    public void askServerInfo() throws ExecutionException {
        clientManager = new ClientManager(this);
        this.addObserver(clientManager);
        local = false;
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
            if(local) this.nickname = nickname; // If local game nickname is always accepted.
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

    @Override
    public void askPlayersNumber(int number) {
        notifyObserver(obs -> obs.PlayersNumber(number));
    }

    /**
     * Asks the player to join a game or to set a new one.
     */
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
        switch (turnState) {
            case FIRST_TURN:
                askFirstAction();
                break;
            case SECOND_TURN:
                askSecondAction();
                break;
            case NOT_IN_TURN:
                out.println("This is not your turn, wait other players....");
                break;
            default:
                askWhichAction();
        }

    }

    /**
     * Asks to the player which action he wants to perform.
     */
    private void askWhichAction() {
        if (turnState.equals(TurnState.END)) {
            return;
        }
        int action;
        Action act;
        boolean exit = false;
        showPlayerBoard(boards.get(playerNumber));
        ArrayList<Action> possibilities = turnState.possibleActions();
        if (boards.get(playerNumber).getPlayerBoard().getLeaderCardsNumber() == 0) {
            possibilities.remove(Action.ACTIVE_LEADER);
            possibilities.remove(Action.FOLD_LEADER);
        }
        do {
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
                        exit=askActiveLeader();
                        break;
                    case FOLD_LEADER:
                        exit=askFoldLeader();
                        break;
                    case END_TURN:
                        notifyObserver(ViewObserver::endTurn);
                        exit = true;
                        break;
                    case GET_RESOURCES_FROM_MARKET:
                        exit = askGetMarket();
                        break;
                    case SORTING_TEMPORARY_STORAGE:
                        exit = askSortingMarket();
                        break;
                    case SORTING_WAREHOUSES:
                        exit = askWhichSortingAction();
                        break;
                    case BUY_DEVELOPMENT_CARD:
                        exit = askGetProduction();
                        break;
                    case USE_PRODUCTIONS:
                        exit = askUseProduction();
                        break;
                }
            } catch (ExecutionException e) {
                out.println("Error");
            }
        } while (!exit);
        clearCli();
    }

    /**
     * Asks to the player which production he wants to use.
     */
    private boolean askUseProduction() {
        ArrayList<String> s= new ArrayList<>();
        int index;
        boolean exit=true;
        s.add("Normal_Production");
        s.add("Base_Production");
        s.add("Special_Production");
        s.add("Exit");
        String question="Which production would you like to active? ";
        for (String st : s) {
            out.println(s.indexOf(st) + ". " + st);
        }
        try {
            index= validateInput(0, 3, null, question);
            switch (index){
                case 0: exit=askUseNormalProduction();
                    break;
                case 1: exit=askUseBaseProduction();
                    break;
                case 2: exit=askUseSpecialProduction();
                    break;
                case 3: return false;
            }
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return exit;
    }

    @Override
    public void askFirstAction() {
        int firstCard, secondCard;
        clearCli();
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
    public boolean askGetMarket() {
        int choice, index;
        String question="What do you want from the market? Select 1 for a row or 2 for a column (3 to exit): ";
        try {
            choice=validateInput(1,3,null,question);
            if(choice==1){
                question="Please Select a row between 0 and 2: ";
                index=validateInput(0,2,null,question);
                notifyObserver(obs -> obs.getMarket(choice, index));
            } else if(choice==2) {
                question="Please Select a column between 0 and 3: ";
                index=validateInput(0,3,null,question);
                notifyObserver(obs -> obs.getMarket(choice, index));
            } else { return false; }
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    @Override
    public boolean askSortingMarket() {
        Resources choice, resource;
        int row;
        boolean white = false;
        ArrayList<Resources> list = new ArrayList<>(Arrays.asList(Resources.values()));
        list.removeAll(boards.get(playerNumber).getTemporaryResourceStorage());
        String question="Which resource do you want to sort? Choose the resource: ";
        try {
            choice=validateResources(question, list);
            if(choice.equals(Resources.WHITE)){
                question="Choose which resource to transform the white resource into: ";
                list.clear();
                list.addAll(Arrays.asList(Resources.values()));
                list.removeAll(boards.get(playerNumber).getPlayerBoard().getSubstitutableResources());
                choice = validateResources(question, list);
                white = true;
            }
            question = "Select a row in the warehouse between 1 and 3, select 4 to discard it or select 0 for the special warehouse (with leader card only) and 5 to exit: ";
            row = validateInput(0, 5, null, question);
            if (row == 5) {
                return false;
            }

            if (white) {
                resource = Resources.WHITE;
            } else resource = choice;

            Resources finalChoice = choice, resources = resource;

            notifyObserver(obs -> obs.sortingMarket(finalChoice, row, boards.get(playerNumber).getTemporaryResourceStorage().indexOf(resources)));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    /**
     * Asks what you want to sort (warehouse-to-warehouse or special-to-normal warehouse)
     * @return {@code false} if user cancelled the action, {@code true} otherwise.
     */
    public boolean askWhichSortingAction() {
        int choice;
        String question = "What do you want to move? \n1.Switch rows \n2.Move resource between warehouses\n3.Exit\n";
        try {
            choice = validateInput(1, 4, null, question);
            if (choice == 1) {
                askSwitchRows();
            } else if (choice == 2) {
                return askMoveBetweenWarehouses();
            } else {
                return false;
            }
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    /**
     * Asks to the player which rows he wants to switch.
     */
    public boolean askMoveBetweenWarehouses() {
        Resources resources;
        int position, newPosition;
        String question;
        try {
            question = "From where do you want to get the Resource?\n0.Special warehouse\n1.First row\n2.Second row\n3.Third row\n";
            position = validateInput(0, 3, null, question);
            question = "Where do you want to put the Resource?\n0.Special warehouse\n1.First row\n2.Second row\n3.Third row\n";
            newPosition = validateInput(0, 3, null, question);

            ArrayList<Resources> jumpList = new ArrayList<>();
            jumpList.add(Resources.WHATEVER);
            jumpList.add(Resources.WHITE);
            jumpList.add(Resources.FAITH);
            if (position == 0) {
                for (int i = 0; i < 4; i++)
                    if (boards.get(0).getPlayerBoard().getExtraResources().get(i) == 0)
                        jumpList.add(Resources.transform(i));
            } else {
                ArrayList<Resources> jumpList2 = new ArrayList<>();
                jumpList2.add(Resources.COIN);
                jumpList2.add(Resources.SERVANT);
                jumpList2.add(Resources.SHIELD);
                jumpList2.add(Resources.STONE);
                int n = 0;
                switch (position) {
                    case 1:
                        n = 0;
                        break;
                    case 2:
                        n = 1;
                        break;
                    case 3:
                        n = 3;
                        break;
                }
                if (!boards.get(0).getPlayerBoard().getResourceWarehouse(n).equals(Resources.WHITE))
                    jumpList2.remove(boards.get(0).getPlayerBoard().getResourceWarehouse(n));
                jumpList.addAll(jumpList2);
            }
            if (jumpList.size() == 7) {
                out.println("You don't have resources in this warehouse");
                return false;
            }
            if (jumpList.size() == 6) {
                ArrayList<Resources> resources1 = (ArrayList<Resources>) Arrays.stream(Resources.values()).collect(Collectors.toList());
                resources1.removeAll(jumpList);
                resources = resources1.get(0);
            } else {
                question = "which resource do you want to move?";
                resources = validateResources(question, jumpList);
            }
            notifyObserver(obs -> obs.moveBetweenWarehouses(resources, position, newPosition));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    @Override
    public void askSwitchRows() {
        int row1, row2;
        ArrayList<Integer> jumpList = new ArrayList<>();
        String question = "Select the first row between 1 and 3: ";
        try {
            row1 = validateInput(1, 3, null, question);
            jumpList.add(row1);
            question = "Select second row (except " + row1 + "): ";
            row2 = validateInput(1, 3, jumpList, question);
            notifyObserver(obs -> obs.switchRows(row1, row2));
        } catch (ExecutionException e) {
            out.println("Error");
        }
    }

    @Override
    public boolean askGetProduction() {
        ArrayList<Colors> col= new ArrayList<>(Arrays.asList(Colors.values()));
        ArrayList<Integer> jump= new ArrayList<>();
        ArrayList<Integer> pos;
        int[] a;
        int color1=-1, level=-1, index;
        boolean flag=false;
        String question;
        try{
            while (!flag) {
                question = "Choose the level of the card you would like to buy (0 to exit): ";
                level = validateInput(0, 3, null, question);
                if (level==0) { return false; }
                out.println("\nThis are the possible card color: ");
                for (Colors color: col) {
                    out.println(col.indexOf(color) + ". " + color);
                }
                question="Select the color of the card you want to buy: ";
                color1 = validateInput(0, col.size(), jump, question);
                if (decks.getDeck(col.get(color1), level).DeckLength() == 0) {
                    out.println("The deck is empty, choose another one: ");
                } else if (!(boards.get(playerNumber).getPlayerBoard().checkResources(decks.getDeck(col.get(color1), level).getFirstCard().getCostCard()))) {
                    out.println("You cannot buy this card, choose another one: ");
                } else {
                    flag = true;
                }
            }
            a = decks.getDeck(col.get(color1), level).getFirstCard().getCostCard();
            pos = SelectResources(a, true);
            if (pos == null) {
                return false;
            }

            question = "Choose where to place your new card, select a number between 0 and 2 : ";
            index = validateInput(0, 2, null, question);

            int finalColor = color1;
            int finalLevel = level;
            int finalIndex = index;
            notifyObserver(obs -> obs.getProduction(finalColor, finalLevel, pos, finalIndex, a));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }


    @Override
    public boolean askUseBaseProduction() {
        int choice=0, temp=0, choice2=-1;
        boolean flag=false, finish=false;
        Resources obt, res = Resources.WHITE, val=Resources.WHITE;
        ArrayList<String> s= new ArrayList<>();
        s.add("Warehouse");
        s.add("Strongbox");
        s.add("SpecialWarehouse");
        s.add("Exit");
        String question;
        ArrayList<Resources> pass = new ArrayList<>();
        ArrayList<Resources> obtain = new ArrayList<>();
        ArrayList<Integer> value= new ArrayList<>();
        obtain.add(Resources.FAITH);
        obtain.add(Resources.WHITE);
        obtain.add(Resources.WHATEVER);
        out.println("You have to choose two resources from your strongbox or your warehouses.");
        try {
            while (!finish){
                while (!flag) {
                    out.println("\nPossible choices: ");
                    for (String st: s) {
                        out.println(s.indexOf(st) + ". " + st);
                    }
                    question= "Choose a number: ";
                    choice = validateInput(0, 3, null, question);
                    if(choice==3) { return false; }
                    question = "Choose which resource you want to use?: ";
                    res = validateResources(question, obtain);
                    if (res.equals(val) && choice == choice2) {
                        temp++;
                    }
                    if (boards.get(playerNumber).getPlayerBoard().getResources(choice, temp).contains(res)) {
                        flag = true;
                        val = res;
                    } else {
                        out.println("You don't have this resource here. ");
                        temp = 0;
                    }
                }
                pass.add(res);
                value.add(choice);
                if(pass.size()==2){
                    finish=true;
                } else
                    { out.println("Now, choose where you want to select the other resources");
                        flag=false;
                        choice2=value.get(0);
                    }
            }
            question="Which resource do you want to obtain? ";
            obt=validateResources(question, obtain);
            notifyObserver(obs -> obs.useBaseProduction(value, pass, obt));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    @Override
    public boolean askUseSpecialProduction() {
        int choice, index=-1;
        boolean flag=false;
        Resources res=Resources.WHITE, resource;
        ArrayList<Resources> obtain = new ArrayList<>();
        obtain.add(Resources.FAITH);
        obtain.add(Resources.WHITE);
        obtain.add(Resources.WHATEVER);
        String question="Choose the leader card you want to active its special production (2 to exit): ";
        try {
            while (!flag) {
                index= validateInput(0, 2, null, question);
                if (index == 2) {
                    return false;
                } else if (boards.get(playerNumber).getPlayerBoard().getLeaderCard(index).getAdvantage().equals(Advantages.PROD) && boards.get(playerNumber).getPlayerBoard().getLeaderCard(index).getUncovered()) {
                    flag = true;
                } else {
                    out.println("You cannot choose this card, try another one.");
                }
            }
            for(int i=0;i<4;i++){
                if (boards.get(playerNumber).getPlayerBoard().getLeaderCard(index).getEffect().get(i) != 0) {
                    res = Resources.transform(i);
                }
            }
            out.println("For this production, you have to use a " +res +".");
            question="Where do you want to take this resource? Select 0 for warehouse, 1 for strongbox or 2 for special warehouse: ";
            choice= validateInput(0,2,null, question);
            question="Which resource you want to obtain? ";
            resource= validateResources(question, obtain);
            int finalIndex = index;
            Resources finalRes = res;
            notifyObserver(obs -> obs.useSpecialProduction(finalIndex, choice, resource, finalRes));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    @Override
    public boolean askUseNormalProduction() {
        int index;
        DevelopmentCard d;
        ArrayList<Integer> jump= new ArrayList<>();
        ArrayList<Integer> pos;
        int[] a;
        for(int i=0; i<3; i++){
            try {
                if (boards.get(playerNumber).getPlayerBoard().getProductionSpaces().get(i).getNumbCard() == 0) {
                    jump.add(i);
                }
            } catch (IndexOutOfBoundsException e) {
                jump.add(i);
            }
        }
        String question="Choose the index of the card you want to activate production of(from 0 to 2, 3 to exit) : ";
        try{
            index=validateInput(0, 3, jump, question );
            if(index==3) { return false; }
            d = boards.get(playerNumber).getPlayerBoard().getProductionSpaces().get(index).getTop();
            a = d.getCostProduction();
            pos=SelectResources(a, false);
            if(pos==null) { return false; }
            notifyObserver(obs -> obs.useNormalProduction(index, pos, a));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    @Override
    public boolean askFoldLeader() {
        int foldCard;
        int numCards = boards.get(playerNumber).getPlayerBoard().getLeaderCardsNumber() - 1;
        String question = "Which card do you want to discard? Select 1, between 0 and " + numCards + " (" + (1 + numCards) + ") to exit:";
        try {
            foldCard = validateInput(0, numCards+1, null, question);
            if(foldCard==numCards+1) { return false; }
            notifyObserver(obs -> obs.foldLeader(foldCard));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;
    }

    @Override
    public boolean askActiveLeader() {
        int activeCard;
        String question = "Which card do you want to active? Select 1, between 0 and 1 (2 to exit):" ;
        try {
            activeCard = validateInput(0, 2, null, question);
            if(activeCard==2) { return false; }
            notifyObserver(obs -> obs.activeLeader(activeCard));
        } catch (ExecutionException e) {
            out.println("Error");
        }
        return true;

    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {
        clearCli();
        if (accepted) {
            if (nick) {
                out.println("your username is accepted, welcome " + name);
                this.nickname = name;
                if(!local){
                    askJoinOrSet();
                } else {
                    askPlayersNumber(1);
                }
            } else {
                out.println("Your username is already taken, try another one");
                askUsername();
            }
        } else {
            if (nick) {
                out.println("The lobby is full");
                System.exit(2);
            } else {
                out.println("Could not find the server");
                System.exit(1);
            }
        }
    }

    @Override
    public void showLobby(ArrayList<String> players) {
        clearCli();
        out.println("Players connected: " + players + "\n Waiting for other players...");

    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {
        out.println(communication);
        switch (type) {
            case ILLEGAL_ACTION:
                askWhichAction();
                break;
            case ILLEGAL_LOBBY_ACTION:
                askJoinOrSet();
                break;
            case END_GAME:
                winner = Integer.parseInt(communication) == 0;
                showEndGame();
                break;
            case BOT_ACTION:
                out.println(BotActions.valueOf(communication).toSentence());
        }


    }

    @Override
    public void showError(String error) {
        clearCli();
        out.print(ColorsCLI.RED);
        out.println(error);
        out.print(ColorsCLI.CLEAR);
    }

    @Override
    public void showPlayerBoard(CompressedPlayerBoard playerBoard) {
        out.println(decks);
        out.println(market);
        out.print(faithPath);
        out.print(" cards: " + faithPath.getCardsState(0, playerNumber) + ", " + faithPath.getCardsState(1, playerNumber) + ", " + faithPath.getCardsState(2, playerNumber));
        out.print("\n");
        if (playerBoard.getName().equals(nickname)) {
            this.playerNumber = playerBoard.getPlayerNumber();
            this.boards.set(playerNumber, playerBoard);
            out.println(playerBoard.toString(true));
        } else if (!turnState.equals(TurnState.FIRST_TURN)) {
            out.println("\n");
            this.boards.set(playerBoard.getPlayerNumber(), playerBoard);
            out.println("Another player is playing...");
            out.println(playerBoard.toString(false));
        }
    }


    @Override
    public void showFaithPath(FaithPath faithPath) {
        this.faithPath = faithPath;
    }

    @Override
    public void showDecks(Decks decks) {
        this.decks = decks;

    }

    @Override
    public void showMarket(Market market) {
        this.market = market;
    }

    /**
     * Shows the End Game Screen
     */
    private void showEndGame() {
        clearCli();
        int gameSize = 0;
        ArrayList<CompressedPlayerBoard> notNull = new ArrayList<>();
        for(CompressedPlayerBoard board : boards){
            if(board != null) {
                gameSize++;
                notNull.add(board);
            }

        }
        out.println("THE GAME HAS ENDED");
        if (gameSize > 1) {
            ArrayList<CompressedPlayerBoard> sorted = (ArrayList<CompressedPlayerBoard>) notNull.stream().sorted(Comparator.comparingInt(CompressedPlayerBoard::getPlayerNumber).reversed()).collect(Collectors.toList());
            for (int i = 0; i < sorted.size(); i++)
                out.println((i + 1) + ". " + sorted.get(i).getName() + "  score:" + sorted.get(i).getPlayerBoard().getVP());
        } else {
            if (winner)
                out.println("Winner");
            else
                out.println("Loser");
            out.println(boards.get(0).getName() + "  score:" + boards.get(0).getPlayerBoard().getVP());
            System.exit(0);
        }


    }

    /**
     * Asks a question and validate the answer.
     *
     * @param jumpList Resources that cannot be chosen.
     * @param question Question to answer.
     * @return The answer to the question(Resources).
     */
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
            } catch (IllegalArgumentException e) {
                out.println("Invalid input! Please try again.\n");
            }
        } while (jumpList.contains(resources));

        return resources;
    }

    /**
     * Asks a question and validate the answer.
     *
     * @param jumpList Resources that cannot be chosen.
     * @param question Question to answer.
     * @param maxValue Maximum value.
     * @param minValue Minimum value.
     * @return The answer to the question(Resources).
     */
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

    /**
     * Asks the player to select from where he wants to pay the resources.
     *
     * @param a                  Cost of the action.
     * @param doYouWantADiscount is the player board discounted?
     * @return The position of each resource to pay.
     */
    private ArrayList<Integer> SelectResources(int[] a, boolean doYouWantADiscount) throws ExecutionException {
        ArrayList<String> s = new ArrayList<>();
        ArrayList<Integer> pos = new ArrayList<>();

        ExchangeResources stuffThatIOwn = boards.get(playerNumber).getPlayerBoard().getExchangeResources();
        boolean exit = false;
        do {
            pos.clear();
            s.clear();
            int[][] forValidation = new int[3][4];
            int count = 0, select;
            String question;
            s.add("Warehouse");
            s.add("Strongbox");
            s.add("SpecialWarehouse");
            s.add("Exit");
            out.println("\nNow you have to choose where you want to take each resources from: ");
            for (String st : s) {
                out.println(s.indexOf(st) + ". " + st);
            }

            // Remove discounted resources
            if(doYouWantADiscount){
                PlayerBoard myBoard = null;
                for(CompressedPlayerBoard cpb : boards){
                    if(cpb.getName().equals(nickname)){
                        myBoard = cpb.getPlayerBoard();
                        break;
                    }
                }
                for(int i = 0; i < 4; i++){
                    if(myBoard != null){
                        if(myBoard.isResourceDiscounted(Resources.transform(i)) && a[i] > 0){
                            a[i] -= myBoard.getResourceDiscount(Resources.transform(i));
                            if(a[i] < 0) a[i] = 0;
                        }
                    }
                }
            }

            try {
                for (int i = 0; i < 4; i++) {
                    while (count != a[i]) {
                        question = "Pick a number for the resource  " + Resources.transform(i) + " (3 to exit): ";
                        select = validateInput(0, 3, null, question);
                        if(select==3) { return null;}
                        count++;
                        pos.add(select);
                        forValidation[select][i]++;
                    }
                    count = 0;
                }
            } catch (ExecutionException e) {
                out.println("Error");
            }

            // Validation
            for(int i = 0; i < 4; i++){
                if(stuffThatIOwn.getWarehouse()[i] < forValidation[0][i]){
                    exit = false;
                } else if(stuffThatIOwn.getStrongbox()[i] < forValidation[1][i]) {
                    exit = false;
                } else if(stuffThatIOwn.getSpecialWarehouse()[i] < forValidation[2][i]){
                    exit = false;
                } else {
                    exit = true;
                }
            }
            if(!exit){
                out.println("\nYou have tried to take resources that you don't have. Try again! ");
            }
        } while(!exit);
        return pos;
    }

    /**
     * Clear the screen of the cli.
     */
    public void clearCli() {
        out.println("\033[H\033[2J");
        out.flush();
    }

    @Override
    public ClientManager getClientManager() {
        return clientManager;
    }
}