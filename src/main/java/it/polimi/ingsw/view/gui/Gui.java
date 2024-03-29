package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.*;
import javafx.application.Platform;
import it.polimi.ingsw.model.Player;
import java.util.List;

/**
 * Gui class that handles the graphic parts of the game
 */
public class Gui extends ViewObservable implements View {

    private static final String ERROR_STR = "ERRROR";
    private static final String MENU_STR_FXML = "menu_scene.fxml";
    //helper
    private static List<Cloud> clouds;
    //update scene
    private GameBoardSceneController gameBoardSceneController = null;
    private Gameboard updateGameBoard;
    private List<Dashboard> updateDashBoards;
    private List<Player> updatePlayers;
    private  String playerName;
    private  int atomicExpert  = 0 ;
    private String cloudChoice = "firstPick";

    /**
     * asks the nickname to the player
     */
    @Override
    public void askNickname(){
        LoginSceneController loginSceneController = new LoginSceneController(this);
        loginSceneController.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeRootPane(loginSceneController, "login_scene.fxml"));
    }

    /**
     * asks the player whether they wants to start a match
     * @param nickname is the player's nickname
     * @param answer is the player's answer
     */
    @Override
    public void askStart(String nickname, String answer) {
        GetStudentFromCardController getStudentFromCardController= null;
        try{
            getStudentFromCardController = (GetStudentFromCardController) SceneController.getSceneController();
            Platform.runLater(()->SceneController.showingContinuePopUp(observers,gameBoardSceneController));
        }catch(ClassCastException e){
            Platform.runLater(() -> SceneController.changeRootPane(observers, "start_scene.fxml"));
        }

    }

    /**
     * asks the player which deck they wants to use during the game
     * @param nickname is the player's nickname
     * @param availableDecks is the decks available for choosing
     */
    @Override
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        DeckChoiceSceneController dCSController = new DeckChoiceSceneController();
        dCSController.addAllObservers(observers);
        dCSController.setAvailableDecks(availableDecks);
        setPlayerName(nickname);
        Platform.runLater(() -> SceneController.changeRootPane(dCSController, "deckChoice_scene.fxml"));
    }

    /**
     * asks the player the assistant they wants to play
     * @param nickname is the player's nickname
     * @param availableAssistants is the assistants available for choosing
     */
    @Override
    public void askAssistant(String nickname, List<Assistant> availableAssistants) {
        String mode = "noReadOnly";
        Platform.runLater(() -> SceneController.showingAssistantPopup(availableAssistants,observers,gameBoardSceneController,mode,playerName));
    }

    /**
     * asks the player which moves they wants to make
     * @param students is the list of available students
     * @param islands is the list of islands
     */
    @Override
    public void askMoves(List<Student> students, List<Island> islands) {
        boolean alreadyExist = false;
        cloudChoice = "get";
        Gameboard actualGameBoard = updateGameBoard;
        GetStudentFromCardController getStudentFromCard = null;
        if (actualGameBoard.getToReset().size() > 0) {
            try{
               getStudentFromCard = (GetStudentFromCardController) SceneController.getSceneController();
               alreadyExist = true;
            }catch (ClassCastException e){
                getStudentFromCard = new GetStudentFromCardController(playerName,gameBoardSceneController);
                getStudentFromCard.addAllObservers(observers);
            }finally {
                List<Dashboard> listOfDashBoards = updateDashBoards;
                List<Player> listOfPlayers = updatePlayers;
                getStudentFromCard.setGameBoard(actualGameBoard, listOfDashBoards, listOfPlayers);
                GetStudentFromCardController finalGetStudentFromCard = getStudentFromCard;
                if (actualGameBoard.getToReset().contains(ExpertDeck.HERALD)) {
                    Platform.runLater(() -> SceneController.alertShown("Message", "Please choose the island to calculate influence on.\n"));
                    Platform.runLater(()-> finalGetStudentFromCard.setExpertDeck(ExpertDeck.HERALD));
                    Platform.runLater(()->finalGetStudentFromCard.setExpertImage());
                    Platform.runLater(()->finalGetStudentFromCard.setQuestion());
                    Platform.runLater(()->finalGetStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ISLAND));

                } else if (actualGameBoard.getToReset().contains(ExpertDeck.HERBALIST)) {
                    Platform.runLater(() -> SceneController.alertShown("Message", "Please choose the island to ban.\n"));
                    Platform.runLater(()-> finalGetStudentFromCard.setExpertDeck(ExpertDeck.HERBALIST));
                    Platform.runLater(()->finalGetStudentFromCard.setExpertImage());
                    Platform.runLater(()->finalGetStudentFromCard.setQuestion());
                    Platform.runLater(()->finalGetStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ISLAND));

                } else if (actualGameBoard.getToReset().contains((ExpertDeck.TAVERNER))) {
                    Platform.runLater(() -> SceneController.alertShown("Message", "Please choose the island to put the student on.\n"));
                    Platform.runLater(()-> finalGetStudentFromCard.setExpertDeck(ExpertDeck.TAVERNER));
                    Platform.runLater(()-> finalGetStudentFromCard.setExpertImage());
                    if (atomicExpert == 1) {
                        Platform.runLater(()-> finalGetStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ISLAND));
                        Platform.runLater(()-> finalGetStudentFromCard.setQuestion());
                        atomicExpert = 0;
                    }

                }
                Platform.runLater(() -> finalGetStudentFromCard.setDisabledItems());
                if(alreadyExist==false){
                    Platform.runLater(() -> SceneController.changeRootPane(finalGetStudentFromCard, "get_from_card_scene.fxml"));
                }
            }
            return;
            }
           askGameBoardMoves();
        }

    /** asks the player which moves they wants to do on the board
     * @param student is the color of the selected student
     * @param islands is the list of islands
     */
    @Override
    public void askIslandMoves(Color student, List<Island> islands) {}

    /**
     * asks the players how many moves they wants to make Mother Nature do
     * @param nickname is the player's nickname
     * @param possibleSteps is the possible steps mother nature can do
     */
    @Override
    public void askMotherMoves(String nickname, int possibleSteps) {
        GameBoardSceneController gBSC = getGameSceneController();
        gBSC.setSecondaryPhase(PhaseType.MOVE_MOTHER);
        Platform.runLater(()->gBSC.enabledGlowEffectIsland(possibleSteps));
        Platform.runLater(()-> {
            try {
                gBSC.updateAll();
            } catch (noTowerException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> SceneController.alertShown("Message:", "Please choose a number of mother nature moves between 1 and "+ possibleSteps));
    }

    /**
     * asks the player which cloud they wants to select
     * @param nickname is the player's nickname
     * @param availableClouds is the list of available clouds
     */
    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds) {
        Platform.runLater(()-> {
            try {
                gameBoardSceneController.updateAll();
            } catch (noTowerException ex) {
                ex.printStackTrace();
            }
        });
        if(cloudChoice.equals("firstPick")){
            Platform.runLater(()->SceneController.showingCloudsPopup(availableClouds,clouds,observers,cloudChoice,gameBoardSceneController));
            Platform.runLater(()-> {
                try {
                    getGameSceneController().updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
            cloudChoice = "get";
        }else if(cloudChoice.equals("get")){
            Platform.runLater(()->SceneController.showingCloudsPopup(availableClouds,clouds,observers,cloudChoice,gameBoardSceneController));
            Platform.runLater(()-> {
                try {
                    gameBoardSceneController.updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
            cloudChoice = "firstPick";
        }

        }

    /**
     * asks the number of players for the match
     */
    @Override
    public void askPlayersNumber(){
        PlayersNumberSceneController playersNumSC = new PlayersNumberSceneController();
        playersNumSC.addAllObservers(observers);
        playersNumSC.setRangeForPlayers(2, 3); //here is kept the possibility to play in 4 players
        Platform.runLater(() -> SceneController.changeRootPane(playersNumSC, "players_number_scene.fxml"));
    }

    /**
     * asks the game mode for the match
     * @param nickname is the player's nickname
     * @param gameMode is the list of available game modes
     */
    @Override
    public void askGameMode(String nickname, List<modeEnum> gameMode){
        GameModeSceneController gameModeSC = new GameModeSceneController();
        gameModeSC.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeRootPane(gameModeSC, "gameMode_scene.fxml"));
    }

    /**
     * asks the player the towers' color
     * @param nickname is the player's nickname
     * @param teams is the list of available teams
     */
    @Override
    public void askInitType(String nickname, List<Type> teams) {
        TowerChoiceSceneController tCSController = new TowerChoiceSceneController();
        tCSController.addAllObservers(observers);
        tCSController.setAvailableColors(teams);
        Platform.runLater(() -> SceneController.changeRootPane(tCSController, "tower_scene.fxml"));

    }

    /**
     * shows a message on the player's screen
     * @param genericMessage
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        Platform.runLater(() -> SceneController.alertShown("Message:", genericMessage));
    }

    @Override
    public void showLoginResult(boolean nicknameAccepter, boolean connectionResult, String nickname) {
        if(!nicknameAccepter || !connectionResult){
            if(!nicknameAccepter && connectionResult){
                Platform.runLater(() -> {
                    SceneController.alertShown(ERROR_STR,"This nickname has already been used!");
                    SceneController.changeRootPane(observers, "login_scene.fxml");
                });
            } else {
                Platform.runLater(() -> {
                    SceneController.alertShown(ERROR_STR, "Impossible to contact the server...");
                    SceneController.changeRootPane(observers, MENU_STR_FXML);
                });
            }
        }
    }

    @Override
    public void errorCommunicationAndExit(String nickname) {
        Platform.runLater(() -> {
            SceneController.alertShown(ERROR_STR, nickname);
            SceneController.changeRootPane(observers, MENU_STR_FXML);
        });
    }



    @Override
    public void showMatchInfo(int chosen, int actual) {}

    @Override
    public void showMatchInfo(List<String> activePlayers, String activePlayerNickname) {}

    /**
     * displays the message for the winning to the winner
     * @param winner is the nickname of winner
     */
    @Override
    public void showWinMessage(String winner) {
        Platform.runLater(() -> {
            SceneController.showingTheWinningPopup(winner,playerName);
            SceneController.changeRootPane(observers, MENU_STR_FXML);
        });
    }


    @Override
    public void updateTable(Gameboard gameboard, List<Dashboard> dashboards,List<Player> players){
        GameBoardSceneController gBSC;
        GetStudentFromCardController gSFC;
        try {
            gBSC = (GameBoardSceneController) SceneController.getSceneController();
            gBSC.setGameBoard(gameboard,dashboards,players);
            GameBoardSceneController finalGBSC1 = gBSC;
            gameBoardSceneController = gBSC;
            Platform.runLater(()-> {
                try {
                    finalGBSC1.updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
        }catch (ClassCastException ex){
            try {
                gSFC = (GetStudentFromCardController) SceneController.getSceneController();
                gSFC.setGameBoard(gameboard,dashboards,players);
            }catch (ClassCastException e){
                gBSC = new GameBoardSceneController(playerName);
                gBSC.addAllObservers(observers);
                gBSC.setGameBoard(gameboard,dashboards,players);
                GameBoardSceneController finalGBSC = gBSC;
                gameBoardSceneController = gBSC;
                updateGameBoard = gameboard;
                Platform.runLater(() -> SceneController.changeRootPane(finalGBSC, "gameboard_scene.fxml"));
                Platform.runLater(()-> {
                    try {
                        finalGBSC.updateAll();
                    } catch (noTowerException nTE) {
                        nTE.printStackTrace();
                    }
                });

            }

        }finally {
            clouds = gameboard.getClouds();
            updateGameBoard = gameboard;
            updateDashBoards = dashboards;
            updatePlayers = players;
        }


    }

    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        LobbySceneController lsc;
        try {
            lsc = (LobbySceneController) SceneController.getSceneController();
            lsc.setNicknames(nicknameList);
            lsc.setMaxPlayers(maxPlayers);
            Platform.runLater(lsc::updateValues);
        } catch (ClassCastException e) {
            lsc = new LobbySceneController();
            lsc.addAllObservers(observers);
            lsc.setNicknames(nicknameList);
            lsc.setMaxPlayers(maxPlayers);
            LobbySceneController finalLsc = lsc;
            Platform.runLater(() -> SceneController.changeRootPane(finalLsc, "lobby_scene.fxml"));
        }
    }

    @Override
    public void askExpert() {}

    /**
     * this method is called in the expert mode only and it is used for the expert cards that have students' paws on
     * them
     * asks the player to choose a paw on the card
     */
    @Override
    public void askColor() {
        boolean alreadyExist = false;
        GetStudentFromCardController getStudentFromCardController = null;
        try {
            getStudentFromCardController = (GetStudentFromCardController) SceneController.getSceneController();
            alreadyExist = true;
            if(atomicExpert==0){
                GetStudentFromCardController finalGetStudentFromCardController1 = getStudentFromCardController;
                Platform.runLater(() -> {
                    try {
                        finalGetStudentFromCardController1.updateAll();
                    } catch (noTowerException e) {
                        e.printStackTrace();
                    }
                });
            }
        }catch (ClassCastException e){
           getStudentFromCardController = new GetStudentFromCardController(playerName,gameBoardSceneController);
            getStudentFromCardController.addAllObservers(observers);
        }finally {
            Gameboard currentGameBoard = updateGameBoard ;
            List<Dashboard> listOfDashBoards = updateDashBoards;
            List<Player> listOfPlayers = updatePlayers;
            getStudentFromCardController.setGameBoard(currentGameBoard,listOfDashBoards,listOfPlayers);
            GetStudentFromCardController finalGetStudentFromCardController = getStudentFromCardController;
            if (currentGameBoard.getToReset().contains(ExpertDeck.MUSICIAN)) {
                Platform.runLater(()-> finalGetStudentFromCardController.setExpertDeck(ExpertDeck.MUSICIAN));
                Platform.runLater(()->finalGetStudentFromCardController.setExpertImage());
                if (atomicExpert == 0) {
                    Platform.runLater(()->finalGetStudentFromCardController.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_THE_HALL));
                    Platform.runLater(()->finalGetStudentFromCardController.setQuestion());
                    atomicExpert = 1;
                } else {
                    Platform.runLater(()->finalGetStudentFromCardController.setPhase(ExpertDeckPhaseType.SELECT_ROW));
                    Platform.runLater(()->finalGetStudentFromCardController.setQuestion());
                    atomicExpert = 0;
                }
            } else if (currentGameBoard.getToReset().contains(ExpertDeck.JOKER)) {
                Platform.runLater(()-> finalGetStudentFromCardController.setExpertDeck(ExpertDeck.JOKER));
                Platform.runLater(()->finalGetStudentFromCardController.setExpertImage());
                if (atomicExpert == 0) {
                    Platform.runLater(()->finalGetStudentFromCardController.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_THE_HALL));
                    Platform.runLater(()->finalGetStudentFromCardController.setQuestion());
                    atomicExpert = 1;
                } else {
                    Platform.runLater(()->finalGetStudentFromCardController.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT));
                    Platform.runLater(()->finalGetStudentFromCardController.setQuestion());
                    atomicExpert = 0;
                }
            } else if (currentGameBoard.getToReset().contains(ExpertDeck.TAVERNER)) {
                Platform.runLater(()-> finalGetStudentFromCardController.setExpertDeck(ExpertDeck.TAVERNER));
                Platform.runLater(()->finalGetStudentFromCardController.setExpertImage());
                if (atomicExpert == 0) {
                    Platform.runLater(()->finalGetStudentFromCardController.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT));
                    Platform.runLater(()->finalGetStudentFromCardController.setQuestion());
                    atomicExpert = 1;
                }
            } else if (currentGameBoard.getToReset().contains((ExpertDeck.BARBARIAN))) {
                Platform.runLater(()-> finalGetStudentFromCardController.setExpertDeck(ExpertDeck.BARBARIAN));
                Platform.runLater(()->finalGetStudentFromCardController.setExpertImage());
                Platform.runLater(()->finalGetStudentFromCardController.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT));
                Platform.runLater(()->finalGetStudentFromCardController.setQuestion());

            }else if(currentGameBoard.getToReset().contains(ExpertDeck.BANKER)) {
                Platform.runLater(()->SceneController.showingColorChoicePopUp(observers,gameBoardSceneController,ExpertDeck.BANKER));
                return;
            }else  if(currentGameBoard.getToReset().contains(ExpertDeck.SELLER)){
                Platform.runLater(()->SceneController.showingColorChoicePopUp(observers,gameBoardSceneController,ExpertDeck.SELLER));
                return;
            }
            Platform.runLater(() -> finalGetStudentFromCardController.setDisabledItems());

            if(alreadyExist==false){
                Platform.runLater(() -> SceneController.changeRootPane(finalGetStudentFromCardController, "get_from_card_scene.fxml"));
            }

        }

    }


    private void setPlayerName(String nickname){
        playerName = nickname;
    }
    /**
     * Returns the current GameBoardSceneController if the board is already on scene.
     * Otherwise returns a new GameBoardSceneController and the scene is changed to this one.
     *
     * @return the active GameBoardSceneController if any present, a new one otherwise.
     */
    public GameBoardSceneController getGameSceneController() {
        GameBoardSceneController gBSC;
        try {
            gBSC = (GameBoardSceneController) SceneController.getSceneController();
        } catch (ClassCastException e) {
            gBSC = new GameBoardSceneController(playerName);
            gBSC.addAllObservers(observers);
            GameBoardSceneController finalGBSC = gBSC;
            //Platform.runLater(() -> SceneController.changeRootPane(finalGBSC, "gameboard_scene.fxml"));
        }
        return gBSC;
    }

    public void setPlayerNickname(String playerNickname){
        playerName = playerNickname;
    }


    /**
     * asks the moves the player wants to do on the gameboard
     */
    private void askGameBoardMoves(){
        GameBoardSceneController gBSC;
        try {
            gBSC = (GameBoardSceneController) SceneController.getSceneController();
            gBSC.setGameBoard(updateGameBoard,updateDashBoards,updatePlayers);
            gBSC.setMainPhase(PhaseType.YOUR_MOVE);
            gBSC.setSecondaryPhase(PhaseType.MOVE_STUDENT);
            GameBoardSceneController finalGBSC1 = gBSC;
            gameBoardSceneController = gBSC;
            Platform.runLater(()-> {
                try {
                    finalGBSC1.updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
        }catch (ClassCastException ex){
                gBSC = new GameBoardSceneController(playerName);
                gBSC.addAllObservers(observers);
                gBSC.setGameBoard(updateGameBoard,updateDashBoards,updatePlayers);
                GameBoardSceneController finalGBSC = gBSC;
                 Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose a student to move!"));
                gBSC.setMainPhase(PhaseType.YOUR_MOVE);
                gBSC.setSecondaryPhase(PhaseType.MOVE_STUDENT);
                Platform.runLater(() -> SceneController.changeRootPane(finalGBSC, "gameboard_scene.fxml"));
                Platform.runLater(()-> {
                    try {
                        finalGBSC.updateAll();
                    } catch (noTowerException nTE) {
                        nTE.printStackTrace();
                    }
                });
                gameBoardSceneController = gBSC;
            }
    }

}
