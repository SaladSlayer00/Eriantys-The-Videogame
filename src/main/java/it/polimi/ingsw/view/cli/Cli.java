package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;
    private static final String STR_INPUT_CANCELED = "User input canceled.";

    public Cli(){
        out =System.out;
    }

    /**
     * Reads a line from the standard input.
     *
     * @return the string read from the input.
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    public String readLine() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());
        inputThread = new Thread(futureTask);
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
        out.println("" +
                " \n" +
                " _______  _______ _________ _______  _       _________          _______ \n" +
                "(  ____ \\(  ____ )\\__   __/(  ___  )( (    /|\\__   __/|\\     /|(  ____ \\\n" +
                "| (    \\/| (    )|   ) (   | (   ) ||  \\  ( |   ) (   ( \\   / )| (    \\/\n" +
                "| (__    | (____)|   | |   | (___) ||   \\ | |   | |    \\ (_) / | (_____ \n" +
                "|  __)   |     __)   | |   |  ___  || (\\ \\) |   | |     \\   /  (_____  )\n" +
                "| (      | (\\ (      | |   | (   ) || | \\   |   | |      ) (         ) |\n" +
                "| (____/\\| ) \\ \\_____) (___| )   ( || )  \\  |   | |      | |   /\\____) |\n" +
                "(_______/|/   \\__/\\_______/|/     \\||/    )_)   )_(      \\_/   \\_______)\n" +
                "                                                                        \n");

        out.println("Welcome to Eriantys Board Game!");

        try {
            askServerInfo();
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    /**
     * Asks the server address and port to the user.
     *
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    public void askServerInfo() throws ExecutionException {
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput;

        out.println("Please specify the following settings. The default value is shown between brackets.");

        do {
            out.print("Enter the server address [" + defaultAddress + "]: ");

            String address = readLine();

            if (address.equals("")) {
                serverInfo.put("address", defaultAddress);
                validInput = true;
            } else if (ClientController.isValidIpAddress(address)) {
                serverInfo.put("address", address);
                validInput = true;
            } else {
                out.println("Invalid address!");
                clearCli();
                validInput = false;
            }
        } while (!validInput);

        do {
            out.print("Enter the server port [" + defaultPort + "]: ");
            String port = readLine();

            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                if (ClientController.isValidPort(port)) {
                    serverInfo.put("port", port);
                    validInput = true;
                } else {
                    out.println("Invalid port!");
                    validInput = false;
                }
            }
        } while (!validInput);

        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
    }

    @Override
    public void askNickname() {
        out.print("Enter your nickname: ");
        try {
            String nickname = readLine();
            notifyObserver(obs -> obs.onUpdateNickname(nickname));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    @Override
    public void askGameMode() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 2 or 3 players): ";

        try {
            playerNumber = numberInput(2, 3, null, question);
            notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 2 or 3 players): ";

        try {
            playerNumber = numberInput(2, 3, null, question);
            notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

}
