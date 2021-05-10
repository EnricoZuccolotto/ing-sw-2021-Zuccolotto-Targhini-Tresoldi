package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientManager;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cli extends ViewObservable implements View {

    private final PrintStream out;


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
            out.println("Your username is already taken, try another one");
            askUsername();
        } else out.println("your username is accepted, welcome " + name);

    }

    @Override
    public void showError(String error) {
        clearCli();
        out.println(error);
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
        out.print("\033[H\033[2J");
        out.flush();
    }
}