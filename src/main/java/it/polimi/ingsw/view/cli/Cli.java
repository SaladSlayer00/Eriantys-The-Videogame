package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.playerBoard.Row;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.Player;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import static it.polimi.ingsw.view.cli.Constants.ISLAND;

/**
 * Command line interface for the game, it allows the visualizaton of the game components and
 * interacts with the player to receive input and show the server's comunication.
 * Notifies the observers to update the server side when the player is acting.
 */
public class Cli extends ViewObservable implements View {
    private Gameboard gameboard;
    private List<Dashboard> dashboards = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private final PrintStream out;
    private Thread inputThread;
    private static final String STR_INPUT_CANCELED = "User input canceled.";
    private int clouds = 0;


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

    /**
     * Asks the player for a nickname.
     */
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

    /**
     * Asks the player to input the gameMode.
     *
     * @param nickname the player's nickname
     * @param gameModes the modes of the game
     */
    @Override
    public void askGameMode(String nickname, List<modeEnum> gameModes) {
        modeEnum game;
        String question = "Please " + nickname + " choose the game mode: ";
        game = modeInput(gameModes, question);
        notifyObserver(obs -> obs.OnUpdateGameMode(game));
    }

    /**
     * Asks the player to input the number of players.
     */
    @Override
    public void askPlayersNumber() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 2, 3 or 4 players): ";

        playerNumber = numberInput(question);
        notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
    }

    /**
     * Asks the player a deck to choose from the available ones.
     *
     * @param nickname the player's nickname
     * @param availableDecks the decks available for choosing
     */
    @Override
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        clearCli();
        Mage mage;
        if (availableDecks.size() > 1) {
            String question = "Please "+ nickname+", select a mage from the list!";

            out.println("Please, enter the name in LOWERCASE and confirm with ENTER.");
                mage = mageInput(availableDecks, question);
                notifyObserver(obs -> obs.OnUpdateInitDeck(mage));
        }
        else if(availableDecks.size() ==1){
            out.println(nickname + ", you're the last player, your mage is: " + availableDecks.get(0).getText());
            notifyObserver(obs -> obs.OnUpdateInitDeck(availableDecks.get(0)));
        }
        else{
            showErrorAndExit("no mages found in the request.");
        }


    }

    /**
     * Asks the player to choose a tower color.
     *
     * @param nickname the player's nickname
     * @param availableTeams
     */
    @Override
    public void askInitType(String nickname, List<Type> availableTeams) {
        clearCli();
        Type team;
        if (availableTeams.size() > 1) {
            String question = "Please "+ nickname + ", select a team from the list!";
            out.println("Please, enter the name in LOWERCASE and confirm with ENTER.");
            team = teamInput(availableTeams, question);
            notifyObserver(obs -> obs.OnUpdateInitTower(team));
        }
        else if(availableTeams.size() ==1){
            out.println(nickname + ", you're the last player, your team is: "+ availableTeams.get(0).getText());
            notifyObserver(obs -> obs.OnUpdateInitTower(availableTeams.get(0)));
        }
        else{
            showErrorAndExit("no teams found in the request.");
        }


    }

    /**
     * Asks the player if they're ready to start the game.
     *
     * @param nickname player's nickname
     * @param answer player's answer
     */
    @Override
    public void askStart(String nickname, String answer){
        clearCli();
        String inputWord;
        if(answer.equals("START")) {
            String question = "Please " + nickname + ", SAY YES OR NO";
            inputWord = answerInput(question);
            notifyObserver(obs -> obs.OnStartAnswer(inputWord));

        }
        else{
            showErrorAndExit("wrong message format.");
        }

    }

    /**
     * Asks the player to input a cloud index to choose it.
     *
     * @param nickname the player's nickname
     * @param availableClouds the list of available clouds
     */
    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds){
        clearCli();
        //showTable();
        int index;
        String question = "Please "+ nickname + ", select a cloud from the list!";
        if (availableClouds.size() > 1) {
            out.println("Please, enter the cloud's index and press ENTER.");
                index = cloudInput(availableClouds, question);
                notifyObserver(obs -> obs.OnUpdatePickCloud(index));
        }
        else if(availableClouds.size() ==1){
            out.println(nickname + ", you're the last player, your cloud is: "+availableClouds.get(0).getIndex());
            notifyObserver(obs -> obs.OnUpdatePickCloud(availableClouds.get(0).getIndex()));
        }
        else if(availableClouds.size()==0 && clouds == 1){
            index = intInput(question);
            clouds = 0;
            notifyObserver(obs -> obs.OnUpdateGetFromCloud(index));
        }
        else{
            showErrorAndExit("no clouds found in the request.");
        }

    }

    /**
     * Asks the player to choose an assistant for the turn.
     *
     * @param nickname the player's nickname
     * @param unavailableAssistants
     */
    @Override
    public void askAssistant(String nickname, List<Assistant> unavailableAssistants){
        clearCli();
        //showTable();
        Assistant assistant;
        if (!unavailableAssistants.equals(null)) {
            String question = "Please "+ nickname + ", select an assistant from the list!";
            out.println("Please, enter the assistant's index and press ENTER.");
            assistant = assistantInput(unavailableAssistants, question);
            out.println("Chosen Assistant: "+assistant.getNumOrder() + "\n");
            notifyObserver(obs -> obs.OnUpdateAssistant(assistant));
        }
        else{
            showErrorAndExit("no assistants found in the request.");
        }

    }

    /**
     * Asks the player to input an expert card to activate.
     */
    public void askExpert() {
            String answer;
            ExpertDeck c = null;
            String question = "Select a card typing the name: \n";
            do {
                out.print(question);
                out.print("Choose between: ");
                for (ExpertDeck s : gameboard.getExperts()) {
                    out.print(s.getText() + "\n");
                }

                try {
                    answer = readLine();
                    c = ExpertDeck.valueOf(answer.toUpperCase());
                    if (!gameboard.getExperts().contains(c)) {
                        out.println("Invalid expert! Please try again.");
                    }
                } catch (IllegalArgumentException | ExecutionException e) {
                    out.println("Invalid expert! Please try again.");
                }
            } while (!gameboard.getExperts().contains(c));


            if (!(c == null)) {
                ExpertDeck finalAnswer = c;
                notifyObserver(obs -> obs.OnUpdateExpert(finalAnswer));
            }
    }

    /**
     * Asks the player to choose a color, can send messages to activate expert cards.
     */
    @Override
    public void askColor() {
        Color color = null;
        String in;
        List<Color> colors = new ArrayList<Color>();
        for(Row r : dashboards.get(0).getRows()){
            colors.add(r.getName());
        }
        do{
            out.print("Choose a color: ");

            try {
                in = readLine();
                color = Color.valueOf(in.toUpperCase());

                if (!colors.contains(color)) {
                    out.println("Invalid color! Please try again.");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid color! Please try again.");
            }
        } while (!colors.contains(color));

        Color finalColor = color;
        if(gameboard.getToReset().contains(ExpertDeck.BANKER)){
            notifyObserver(obs->obs.OnUpdateEffectBanker(finalColor));
        }
        else if(gameboard.getToReset().contains(ExpertDeck.SELLER))
            notifyObserver(obs -> obs.OnUpdateEffectSeller(finalColor));
        else if(gameboard.getToReset().contains(ExpertDeck.MUSICIAN))
            notifyObserver(obs->obs.OnUpdateEffectMusician(finalColor));
        else if(gameboard.getToReset().contains(ExpertDeck.JOKER))
            notifyObserver(obs->obs.OnUpdateEffectJoker(finalColor));
        else if(gameboard.getToReset().contains(ExpertDeck.TAVERNER))
            notifyObserver(obs->obs.OnUpdateEffectTaverner(finalColor));
        else
            notifyObserver(obs->obs.OnUpdateEffectBarbarian(finalColor));

    }

    /**
     * Asks the player to choose a move considering the game state shown by the islands and students.
     *
     * @param students the list of available students
     * @param islands the list of islands
     */
    @Override
    public void askMoves(List<Student> students, List<Island> islands) {
        clearCli();
        Color student;
        String location;
        if(gameboard.getToReset().size()>0 && gameboard.getToReset().contains(ExpertDeck.HERALD)){
            String question = "Please choose the island to calculate influence on.\n";
            int island = islandInput(question, islands);
            notifyObserver(obs -> obs.OnUpdateEffectHerald(island));
            return;
        }
        else if(gameboard.getToReset().size()>0 && gameboard.getToReset().contains(ExpertDeck.HERBALIST)){
            String question = "Please choose the island to ban.\n";
            int island = islandInput(question, islands);
            notifyObserver(obs -> obs.OnUpdateEffectHerbalist(island));
            return;
        }
        else if(gameboard.getToReset().size()>0 && gameboard.getToReset().contains(ExpertDeck.TAVERNER)){
            String question = "Please choose the island to put the student on.\n";
            int island = islandInput(question, islands);
            notifyObserver(obs -> obs.OnUpdateEffectTaverner(island));
            return;
        }
        else if(gameboard.getExperts().size()>0){
            out.println("Would you like to play an expert card? Type yes to see them.");
            String answer = null;
            try {
                answer = readLine();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(answer.equalsIgnoreCase("yes")){
                askExpert();
                return;
            }
        }
        if (!(students.size()==0)) {
            String question = "Please, choose a student to move! Enter in LOWERCASE its color";
            student = studentInput(question, students);
            out.println("Please, choose where do you want to move your students! "+ student.getText());
            question = "Please, enter ISLAND or ROW and press ENTER.";

                location = locationInput(question);
                if("ISLAND".equalsIgnoreCase(location)){
                    out.println("ISLAND");
                    askIslandMoves(student, islands);
                }
                else if("ROW".equalsIgnoreCase(location)){
                    out.println("ROW");
                    notifyObserver(obs -> obs.OnUpdateMoveOnBoard(student,student));
                }
        }
        else{
            showErrorAndExit("no students found in the hall.");
        }
    }

    /**
     * Asks the player the island to move the student on.
     *
     * @param student the color of the selected student
     * @param islands the list of islands
     */
    @Override
    public void askIslandMoves(Color student, List<Island> islands){
        clearCli();
        String question = "Please, choose where do you want to move your student!";
        int location;
        location = islandInput(question, islands);
        out.println("LOC: "+location);
        notifyObserver(obs -> obs.OnUpdateMoveOnIsland(student,location, islands));

    }

    /**
     * Asks the player to choose mother nature's moves.
     *
     * @param nickname the player's nickname
     * @param possibleMoves
     */
    public void askMotherMoves(String nickname, int possibleMoves) {
        clearCli();
        int number;
        number = motherInput(possibleMoves);
        out.println("MOVES: "+number);
        notifyObserver(obs -> obs.OnUpdateMoveMother(number, new Assistant(0, possibleMoves)));
        clouds = 1;
    }

    /**
     * Clears the cli.
     */
    public void clearCli() {
        out.print(ColorCli.CLEAR);
        out.flush();
    }

    //INPUT METHODS

    /**
     * Gets the gameMode from input.
     *
     * @param modeEnums the modes available
     * @param question question asked to the player
     * @return the selected modeEnum
     */
    public modeEnum modeInput(List<modeEnum> modeEnums, String question){
        modeEnum mode = null;
        String in;
        String modeStr = modeEnums.stream()
                .map(modeEnum::getText)
                .collect(Collectors.joining(", "));

        do {
            out.print(question);
            out.print("Choose between " + modeStr + ": ");

            try {
                in = readLine();
                mode = modeEnum.valueOf(in.toUpperCase());

                if (!modeEnums.contains(mode)) {
                    out.println("Invalid mode! Please try again.");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid mode! Please try again.");
            }
        } while (!modeEnums.contains(mode));

        return mode;
    }

    /**
     * Gets the moves for mother nature from input.
     *
     * @param possibleMoves the moves available
     * @return the selevted moves number
     */
    public int motherInput(int possibleMoves){
        int number = 0;
        do{
            try {
                out.print("Please choose a number of mother nature moves between 1 and "+ possibleMoves);
                number = Integer.parseInt(readLine());

                if (number < 1 || number > possibleMoves) {
                    out.println("Invalid number! Please try again.\n");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid mode! Please try again.");
            }
        } while (number < 1 || number > possibleMoves);

        return number;
    }

    public int intInput(String question) {
        int number = -1;
        do {
            try {
                out.println(question);
                number = Integer.parseInt(readLine());
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid number! Please try again.");
            }
        }while(number == -1);

        return number;
    }

    /**
     * Gets a number from input.
     *
     * @param question the question asked to the player
     * @return the selected number
     */
    public int numberInput(String question){
        int number = 0;
        do{

            try {
                out.print(question);
                number = Integer.parseInt(readLine());

                if (number < 2 || number > 4) {
                    out.println("Invalid number! Please try again.\n");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid number! Please try again.");
            }
        } while (number < 2 || number > 4);

        return number;

    }

    /**
     * Gets the mage from input.
     *
     * @param available the available mages
     * @param question the question to ask the player
     * @return the selected mage
     */
    public Mage mageInput(List<Mage> available, String question){
        Mage mage = null;
        String in;
        String modeStr = available.stream()
                .map(Mage::getText)
                .collect(Collectors.joining(", "));

        do {
            out.print(question);
            out.print("Choose between " + modeStr + ": ");

            try {
                in = readLine();
                mage = Mage.valueOf(in.toUpperCase());

                if (!available.contains(mage)) {
                    out.println("Invalid mage! Please try again.");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid mage! Please try again.");
            }
        } while (!available.contains(mage));

        return mage;

    }

    /**
     * Gets the team from input.
     *
     * @param available the available towers
     * @param question the question to ask the player
     * @return the selected tower
     */
    public Type teamInput(List<Type> available, String question){
        Type team = null;
        String in;
        String modeStr = available.stream()
                .map(Type::getText)
                .collect(Collectors.joining(", "));

        do {
            out.print(question);
            out.print("Choose between "+ modeStr + ": ");
            try {
                in = readLine();
                team = Type.valueOf(in.toUpperCase());

                if (!available.contains(team)) {
                    out.println("Invalid mage! Please try again.");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid mage! Please try again.");
            }
        } while (!available.contains(team));

        return team;

    }

    /**
     * Gets an answer from input.
     *
     * @param question the question to ask the player
     * @return the selected answer
     */
    public String answerInput(String question){
        String answer = null;
        do{

            try {
                out.print(question);
                answer = readLine();

                if(gameboard!= null && (gameboard.getToReset().contains(ExpertDeck.MUSICIAN) || gameboard.getToReset().contains(ExpertDeck.JOKER))){
                    return answer;
                }

                if (answer.toUpperCase().equals("NO")) {
                    out.println("Ok! You can type YES when you're ready! \n");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid argument! Please try again.");
            }
        } while (answer.equals(null) || answer.toUpperCase().equals("NO"));

        return answer;
    }

    /**
     * Gets the cloud from input.
     *
     * @param available the list of available clouds
     * @param question the question to ask the player
     * @return the selected cloud index
     */
    public int cloudInput(List<Cloud> available, String question){
        clearCli();
        showTable();
        int number = -1;
        do{

            try {
                out.print(question);
                out.print("Choose between \n");
                for(Cloud c : available){
                    out.print(c.getIndex() + "\n");
                }
                number = Integer.parseInt(readLine());

                if (number < 0 || number > available.size()) {
                    out.println("Invalid number! Please try again.\n");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid mode! Please try again.");
            }
        } while (number < 0 || number > available.size());

        return number;
    }

    /**
     * Gets the assistant from input.
     *
     * @param unavailable list of available assistant
     * @param question the question to ask the player
     * @return the selected assistant
     */
    public Assistant assistantInput(List<Assistant> unavailable, String question){
        clearCli();
        showTable();
        int index;
        Assistant assistant = null;
        int taken=0;
        do{

            try {
                taken = 0;
                out.print(question);
                out.print("Choose except ");
                for(Assistant a : unavailable){
                    out.print(a.getNumOrder() + "\n");
                }
                Scanner myInput = new Scanner( System.in );

                index = myInput.nextInt();
                assistant = new Assistant(index, 0);

                for(Assistant a : unavailable) {
                    if(a.getNumOrder() == index) {
                        out.println("Invalid assitant! Please try again.\n");
                        taken = 1;
                    }
                }
            } catch (IllegalArgumentException e) {
                out.println("Invalid mode! Please try again.");
            }
        } while ( taken == 1 || assistant.getNumOrder()>10||assistant.getNumOrder()<1);

        return assistant;
    }

    /**
     * Gets the student from input.
     *
     * @param question the question to ask the player
     * @param students the list of available students
     * @return the selected color
     */
    public Color studentInput(String question, List<Student> students){
        clearCli();
        showTable();
        Color color = null;
        String in;
        List<Color> colors = new ArrayList<Color>();
        for(Student s : students){
            colors.add(s.getColor());
        }
        do {
            out.print(question);
            out.print("Choose between: ");
            for(Student s : students){
                out.print(s.getColor().getText() + "\n");
            }

            try {
                in = readLine();
                color = Color.valueOf(in.toUpperCase());

                if (!colors.contains(color)) {
                    out.println("Invalid student! Please try again.");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid student! Please try again.");
            }
        } while (!colors.contains(color));

        return color;
    }

    /**
     * Gets the location from input.
     *
     * @param question the question to ask the player
     * @return the location between row and island
     */
    public String locationInput(String question){
        clearCli();
        showTable();
        String answer = null;
        do{

            try {
                out.print(question);
                answer = readLine();

                if (!answer.equalsIgnoreCase("ROW") && !answer.equalsIgnoreCase("ISLAND")) {
                    out.println("Invalid input! Please try again! \n");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid input! Please try again.");
            }
            } while (!answer.equalsIgnoreCase("ROW") && !answer.equalsIgnoreCase("ISLAND"));

        return answer;
    }

    /**
     * Gets the index of the island from input.
     *
     * @param question the question to ask the player
     * @param islands the list of islands
     * @return the index of selected island
     */
    public int islandInput(String question, List<Island> islands){
        clearCli();
        showTable();
        int index = -1;
        do {
            out.print(question);
            out.print("Choose between: ");
            for(int i=0;i<islands.size();i++){
                out.print(i + " ");
            }

            try {
                index = Integer.parseInt(readLine());

                if (index < 0 || index > islands.size()-1) {
                    out.println("Invalid number! Please try again.");
                }
            } catch (IllegalArgumentException | ExecutionException e) {
                out.println("Invalid number! Please try again.");
            }
        } while (index<0||index > islands.size()-1);

       return index;
    }

    /**
     * Shows a generic message on the terminal.
     *
     * @param genericMessage the text message to show
     */
    public void showGenericMessage(String genericMessage) {
        out.println(genericMessage);
    }

    /**
     * Shows the message with the winner name in it.
     *
     * @param winner nickname of winner
     */
    public void showWinMessage(String winner) {
        out.println("Game finished: " + winner + " WINS!");
        System.exit(0);
    }

    /**
     * Shows the error message on the terminal.
     *
     * @param error the name of the error
     */
    public void showErrorAndExit(String error) {
        inputThread.interrupt();
        out.println("\nERROR: " + error);
        out.println("EXIT.");

        System.exit(1);
    }

    /**
     * Updates the table parameters with the ones received from the server message.
     *
     * @param gameboard the instance of the gameoard
     * @param dashboards the instance of the dashboards
     * @param players the player list
     */
    @Override
    public void updateTable(Gameboard gameboard, List<Dashboard> dashboards,List<Player> players){
        this.gameboard=gameboard;
        this.dashboards=dashboards;
        this.players = players;
    }

    /**
     * Shows the islands on the terminal.
     */
    public void showIsland(){
        List<String> islands = buildIslands(gameboard);
        for(int i=0;i<islands.size();i++){
            out.print("\rIsland: "+i+"\n");
            out.print(islands.get(i));
        }
    }

    /**
     * Shows the gameboard on the terminal.
     */
    public void showTable(){
        clearCli();
        showDashboards(dashboards);
        out.print("\n");
        out.print("\n");
        showIsland();
        out.println("Mother nature on: "+gameboard.getMotherNature());

    }

    /**
     * Shows the dashboards on the terminal.
     *
     * @param dashboards the list of the dashboards
     */
    public void showDashboards(List<Dashboard> dashboards){
        String leftAlignFormat = "| %-10s | %-4d | %-5b%n";
        String format = "| %-10s | %-4d | %-5s%n";
        for(int i = 0; i< dashboards.size();i++) {

            System.out.format("+-----------------+-----------+%n");
            System.out.format(format, "PLAYER", i, "");
            System.out.format(format, "TOWERS", dashboards.get(i).getNumTowers(), "" );
            System.out.format("+-----------------+-----------+%n");
            System.out.format(format, "HALL", dashboards.get(i).getHall().size(), "");
            System.out.format(leftAlignFormat, "RED", dashboards.get(i).getRow("red").getNumOfStudents(), dashboards.get(i).getRow("red").checkProfessor());
            System.out.format(leftAlignFormat, "GREEN", dashboards.get(i).getRow("green").getNumOfStudents(), dashboards.get(i).getRow("green").checkProfessor());
            System.out.format(leftAlignFormat, "PINK", dashboards.get(i).getRow("pink").getNumOfStudents(), dashboards.get(i).getRow("pink").checkProfessor());
            System.out.format(leftAlignFormat, "YELLOW", dashboards.get(i).getRow("yellow").getNumOfStudents(), dashboards.get(i).getRow("yellow").checkProfessor());
            System.out.format(leftAlignFormat, "BLUE", dashboards.get(i).getRow("blue").getNumOfStudents(), dashboards.get(i).getRow("blue").checkProfessor());
            System.out.format("+-----------------+-----------+%n");

        }
    }

    /**
     * Method to build the island shapes.
     *
     * @param gameboard the gameboard
     * @return the list of strings with the built islands
     */
    public List<String> buildIslands(Gameboard gameboard) {
        List<Island> gameIslands = gameboard.getIslands();
        List<String> islands = new ArrayList<>();
        for(int j = 0; j<gameIslands.size();j++) {
            String tower = "0" ;
            if(gameIslands.get(j).getTower()){
                tower = "1";
                try {
                    if(gameIslands.get(j).getTeam().equals(Type.BLACK)){
                        tower = "\033[0;30m" + 1 + "\033[0m";
                    }
                    else if(gameIslands.get(j).getTeam().equals(Type.GREY)){
                        tower = "\033[0;37m" + 1 + "\033[0m";
                    }
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            }

            String[] rows = ISLAND.split("\n");
            for (int i = 0; i < rows.length; i++) {
                String[] temp = rows[i].split("x");
                if((i==0)||(i==4)){
                    rows[i] = rows[i]  + "\n";
                }
                else if (i == rows.length - 2) {
                    rows[i] = "\033[33m" + temp[0] + gameIslands.get(j).getStudents().get(Color.YELLOW).size() + temp[1]+"\033[0m" + temp[2] + tower + temp[3] + "\n";
                } else if(i==1) {
                    rows[i] = "\033[38;5;28m"+temp[0] + gameIslands.get(j).getStudents().get(Color.GREEN).size() + temp[1] +"\033[0m" + "\033[0;31m"+temp[2] +gameIslands.get(j).getStudents().get(Color.RED).size() + temp[3] +"\033[0m"+ "\n";
                }
                else{
                    rows[i] = "\033[35m"+temp[0] + gameIslands.get(j).getStudents().get(Color.PINK).size() + temp[1] +"\033[0m"+ "\033[0;34m"+temp[2] +gameIslands.get(j).getStudents().get(Color.BLUE).size() + temp[3] + "\033[0m"+"\n";
                }
            }
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < rows.length; i++) {
                sb.append(rows[i]);
            }
            String island = sb.toString();
            islands.add(island);

        }
        return islands;
    }

    /**
     * Shows the login result on the terminal.
     * On login fail, the program is terminated immediatly.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clearCli();

        if (nicknameAccepted && connectionSuccessful) {
            out.println("Hi, " + nickname + "! You connected to the server.");
        } else if (connectionSuccessful) {
            askNickname();
        } else if (nicknameAccepted) {
            out.println("Max players reached. Connection refused.");
            out.println("EXIT.");

            System.exit(1);
        } else {
            showErrorAndExit("Could not contact server.");
        }
    }

    /**
     * Signals if there's an error and disconnects the player.
     *
     * @param nickname the player's nickname
     */
    @Override
    public void errorCommunicationAndExit(String nickname) {
        inputThread.interrupt();
        out.println("\nERROR: " + nickname);
        out.println("EXIT.");

        System.exit(1);
    }


    /**
     * Shows the match infos.
     *
     * @param chosen players chosen
     * @param actual active players
     */
    @Override
    public void showMatchInfo(int chosen, int actual) {
        out.println("MATCH INFO1");
    }

    /**
     * Shows the match infos (second version)
     * @param activePlayers list of active player
     * @param activePlayerNickname list of nickname of active player
     */
    @Override
    public void showMatchInfo(List<String> activePlayers, String activePlayerNickname) {
        out.println("SHOW INFO2");
    }

    /**
     * Shows the players in the lobby.
     *
     * @param nicknameList list of nicknames for players
     * @param numPlayers the number of chosen players
     */
    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        out.println("LOBBY:");
        for (String nick : nicknameList) {
            out.println(nick);
        }
        out.println("Current players in lobby: " + nicknameList.size() + " / " + numPlayers);
    }



}
