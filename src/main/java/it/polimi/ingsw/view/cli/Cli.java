package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;
    private static final String STR_INPUT_CANCELED = "User input canceled.";

    public Cli() {
        out = System.out;
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
                "| (__    | (____)|    | |   | (___) ||   \\ | |   | |    \\ (_) / | (_____ \n" +
                "|  __)   |     __)    | |   |  ___  || (\\ \\) |   | |     \\   /  (_____  )\n" +
                "| (      | (\\ (      | |   | (   ) || | \\   |   | |      ) (         ) |\n" +
                "| (___/\\| ) \\ \\____) (___| )   ( || )  \\  |   | |      | |   /\\____) |\n" +
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
    public void askGameMode(String nickname, List<modeEnum> gameModes) {
        modeEnum game;
        String question = "Please " + nickname + " choose the game mode (You can choose between EASY and EXPERT): ";

        try {
            game = modeInput(gameModes, null, question);
            notifyObserver(obs -> obs.OnUpdateGameMode(game));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }


    //metodo chiamato quando il gamecontroller richiede dichiedere il numero di players
    @Override
    public void askPlayersNumber() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 2, 3 or 4 players): ";

        try {
            playerNumber = numberInput(2, 3, 4, null, question);
            notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    @Override
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        clearCli();
        Mage mage;
        if (availableDecks.size() > 1) {
            out.println("Please "+ nickname+", select a mage from the list!");
            printMagesAvailable(availableDecks);
            out.println("Please, enter the name in CAPS and confirm with ENTER.");
            try {
                mage = mageInput(availableDecks);
                //Mage.choose(mage);

                notifyObserver(obs -> obs.OnUpdateInitDeck(mage));
            }catch(ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
        else if(availableDecks.size() ==1){
            out.println(nickname + ", you're the last player, your mage is: ");
            printMagesAvailable(availableDecks);
            notifyObserver(obs -> obs.OnUpdateInitDeck(availableDecks.get(0)));
        }
        else{
            showErrorAndExit("no mages found in the request.");
        }


    }

    @Override
    public void askInitType(String nickname, List<Type> availableTeams) {
        clearCli();
        Type team;
        if (availableTeams.size() > 1) {
            out.println("Please "+ nickname + ", select a team from the list!");
            printTeamsAvailable(availableTeams);
            out.println("Please, enter the name in CAPS and confirm with ENTER.");
            try {
                team = teamInput(availableTeams);
                //Type.choose(team);

                notifyObserver(obs -> obs.OnUpdateInitTower(team));
            }catch(ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
        else if(availableTeams.size() ==1){
            out.println(nickname + ", you're the last player, your team is: ");
            printTeamsAvailable(availableTeams);
            notifyObserver(obs -> obs.OnUpdateInitTower(availableTeams.get(0)));
        }
        else{
            showErrorAndExit("no teams found in the request.");
        }


    }

    @Override
    public void askStart(String nickname, String answer){
        clearCli();
        String input;
        if(answer.equals(null)) {
            try {
                out.println("Please "+ nickname + ", select a team from the list!");
                input = answerInput();
                notifyObserver(obs -> obs.OnStartAnswer(input));
            } catch (ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
        else{
            showErrorAndExit("wrong message format.");
        }

    }

    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds){
        clearCli();
        int index;
        if (availableClouds.size() > 1) {
            out.println("Please "+ nickname + ", select a cloud from the list!");
            printCloudsAvailable(availableClouds);
            out.println("Please, enter the cloud's index and press ENTER.");
            try {
                index = cloudInput(availableClouds);


                notifyObserver(obs -> obs.OnUpdatePickCloud(index));
            }catch(ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
        else if(availableClouds.size() ==1){
            out.println(nickname + ", you're the last player, your cloud is: ");
            printCloudsAvailable(availableClouds);
            notifyObserver(obs -> obs.OnUpdatePickCloud(availableClouds.get(0).getIndex()));
        }
        else{
            showErrorAndExit("no clouds found in the request.");
        }
    }

    @Override
    public void AskAssistant(String nickname, List<Assistant> availableAssistants){
        clearCli();
        Assistant assistant;
        if (!availableAssistants.equals(null)) {
            out.println("Please "+ nickname + ", select an assistant from the list!");
            printAssistantsAvailable(availableAssistants);
            out.println("Please, enter the assistant's index and press ENTER.");
            try {
                assistant = assistantInput(availableAssistants);
                notifyObserver(obs -> obs.OnUpdateAssistant(assistant));
            }catch(ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
        else{
            showErrorAndExit("no assistants found in the request.");
        }

    }

    @Override
    public void askMoves(List<Student> students){
        clearCli();
        Student student;
        String location;
        if (!(students.size()==0)) {
            out.println("Please, choose a student to move!");
            printStudents(students);
            try {
                student = studentInput();
            }catch(ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
            out.println("Please, choose where do you want to move your students!");
            out.println("Please, enter the ISLAND or ROW and press ENTER.");
            try {
                location = locationInput();
                if("ISLAND".equals(location.toUpperCase())){
                    askIslandMoves(student);
                }
                else if("ROW".equals(location.toUpperCase())){
                    askRowMoves(student);
                }
            }catch(ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
        else{
            showErrorAndExit("no students found in the hall.");
        }
    }

    @Override
    public void askIslandMoves(Student student){
        out.println("Please, choose where do you want to move your student!");
        printStudents(student);
        int location;
        try {
            location = islandInput();
            notifyObserver(obs -> obs.OnUpdateMoveOnIsland(student.getColor(),location));
        }catch(ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }

    }

    @Override
    public void askRowMoves(Student student){
        out.println("Please, choose where do you want to move your student!");
        printStudents(student);
        Color location;
        try {
            location = rowInput();
            notifyObserver(obs -> obs.OnUpdateMoveOnBoard(student.getColor(),location));
        }catch(ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

}
