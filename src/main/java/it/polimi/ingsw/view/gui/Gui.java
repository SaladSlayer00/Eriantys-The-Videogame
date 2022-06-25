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

public class Gui extends ViewObservable implements View {

    private static final String ERROR_STR = "ERRROR";
    private static final String MENU_STR_FXML = "menu_scene.fxml";
    private static String playerName;
    private static List<Cloud> clouds;
    private static String cloudChoice ;
    private static int atomicExpert  = 0 ;
    private GameBoardSceneController gameBoardSceneController;

    @Override
    public void askNickname(){
        Platform.runLater(() -> SceneController.changeRootPane(observers, "login_scene.fxml"));
    }

    @Override
    public void askStart(String nickname, String answer) {
        setPlayerName(nickname);
        Platform.runLater(() -> SceneController.changeRootPane(observers, "start_scene.fxml"));
    }

    @Override
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        DeckChoiceSceneController dCSController = new DeckChoiceSceneController();
        dCSController.addAllObservers(observers);
        dCSController.setAvailableDecks(availableDecks);
        Platform.runLater(() -> SceneController.changeRootPane(dCSController, "deckChoice_scene.fxml"));
    }

    @Override
    public void askAssistant(String nickname, List<Assistant> availableAssistants) {
        Platform.runLater(() -> SceneController.showingAssistantPopup(availableAssistants,observers));
    }

    @Override
    public void askMoves(List<Student> students, List<Island> islands) {
        Gameboard actualGameBoard = gameBoardSceneController.getReducedGameBoard();
        if (actualGameBoard.getToReset().size() > 0) {
            GetStudentFromCard getStudentFromCard = new GetStudentFromCard(playerName);
            getStudentFromCard.addAllObservers(observers);
            List<Dashboard> listOfDashBoards = getGameSceneController().getReducedDashboards();
            List<Player> listOfPlayers = getGameSceneController().getListOfPlayer();
            getStudentFromCard.setGameBoard(actualGameBoard, listOfDashBoards, listOfPlayers);
            if (actualGameBoard.getToReset().contains(ExpertDeck.HERALD)) {
                Platform.runLater(() -> SceneController.alertShown("Message", "Please choose the island to calculate influence on.\n"));
                    getStudentFromCard.setExpertDeck(ExpertDeck.HERALD);
                    getStudentFromCard.setExpertImage();
                    getStudentFromCard.setQuestion();
                    getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ISLAND);

            } else if (actualGameBoard.getToReset().contains(ExpertDeck.HERBALIST)) {
                Platform.runLater(() -> SceneController.alertShown("Message", "Please choose the island to ban.\n"));
                getStudentFromCard.setExpertDeck(ExpertDeck.HERBALIST);
                getStudentFromCard.setExpertImage();
                getStudentFromCard.setQuestion();
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ISLAND);

            } else if (actualGameBoard.getToReset().contains((ExpertDeck.TAVERNER))) {
                Platform.runLater(() -> SceneController.alertShown("Message", "Please choose the island to put the student on.\n"));
                getStudentFromCard.setExpertDeck(ExpertDeck.TAVERNER);
                getStudentFromCard.setExpertImage();
                if (atomicExpert == 1) {
                    getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ISLAND);
                    getStudentFromCard.setQuestion();
                    atomicExpert = 0;
                }

            }
            //Platform.runLater(() -> SceneController.changeRootPane(getStudentFromCard, "get_from_card_scene.fxml"));
            return;
            }

            Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose a student to move!"));
            GameBoardSceneController gBSC = getGameSceneController();
            gBSC.setMainPhase(PhaseType.YOUR_MOVE);
            gBSC.setSecondaryPhase(PhaseType.MOVE_STUDENT);
            Platform.runLater(() -> {
                try {
                    gBSC.updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
        }
    @Override
    public void askIslandMoves(Color student, List<Island> islands) {

    }

    @Override
    public void askMotherMoves(String nickname, int possibleSteps) {
        GameBoardSceneController gBSC = getGameSceneController();
        gBSC.setSecondaryPhase(PhaseType.MOVE_MOTHER);
        Platform.runLater(()->gBSC.enabledGlowEffectIsland());
        Platform.runLater(()-> {
            try {
                gBSC.updateAll();
            } catch (noTowerException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> SceneController.alertShown("Message:", "Please choose a number of mother nature moves between 1 and "+ possibleSteps));
    }

    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds) {
        Platform.runLater(()-> {
            try {
                getGameSceneController().updateAll();
            } catch (noTowerException ex) {
                ex.printStackTrace();
            }
        });
        if(getGameSceneController().getReducedGameBoard().getEmptyClouds().size()==2){
            cloudChoice = "firstPick";
            Platform.runLater(()->SceneController.showingCloudsPopup(availableClouds,clouds,observers,cloudChoice));
            Platform.runLater(()-> {
                try {
                    getGameSceneController().updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
        }else if(cloudChoice.equals("secondPick")){
            Platform.runLater(()->SceneController.showingCloudsPopup(availableClouds,clouds,observers,cloudChoice));
            Platform.runLater(()-> {
                try {
                    getGameSceneController().updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });

        }else if(cloudChoice.equals("get")){
            Platform.runLater(()->SceneController.showingCloudsPopup(availableClouds,clouds,observers,cloudChoice));
            Platform.runLater(()-> {
                try {
                    getGameSceneController().updateAll();
                } catch (noTowerException e) {
                    e.printStackTrace();
                }
            });
        }


    }

    @Override
    public void askPlayersNumber(){
        PlayersNumberSceneController playersNumSC = new PlayersNumberSceneController();
        playersNumSC.addAllObservers(observers);
        playersNumSC.setRangeForPlayers(2, 3); //here is kept the possibility to play in 4 players
        Platform.runLater(() -> SceneController.changeRootPane(playersNumSC, "players_number_scene.fxml"));
    }

    //is this thing right???????
    //TODO (but it's actually a TO CHECK)
    @Override
    public void askGameMode(String nickname, List<modeEnum> gameMode){
        GameModeSceneController gameModeSC = new GameModeSceneController();
        gameModeSC.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeRootPane(gameModeSC, "gameMode_scene.fxml"));
    }

    @Override
    public void askInitType(String nickname, List<Type> teams) {
        TowerChoiceSceneController tCSController = new TowerChoiceSceneController();
        tCSController.addAllObservers(observers);
        tCSController.setAvailableColors(teams);
        Platform.runLater(() -> SceneController.changeRootPane(tCSController, "tower_scene.fxml"));

    }


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
    public void effectEnabled(String summoner) {

    }

    @Override
    public void showMatchInfo(int chosen, int actual) {

    }

    @Override
    public void showMatchInfo(List<String> activePlayers, String activePlayerNickname) {

    }

    @Override
    public void winCommunication(String winner) {

    }

    /*
    @Override
    public void winCommunication(String winner) {
        Platform.runLater(() -> {
            SceneController.alertShown("CONGRATULATION!", "You have won this match!");
        });
    }
    */

    @Override
    public void showWinMessage(String winner) {
        Platform.runLater(() -> {
            SceneController.showingTheWinningPopup(winner);
            SceneController.changeRootPane(observers, MENU_STR_FXML);
        });
    }


    @Override
    public void showAssistant(int index) {

    }

    @Override
    public void updateTable(Gameboard gameboard, List<Dashboard> dashboards,List<Player> players){
        GameBoardSceneController gBSC;
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
        } catch (ClassCastException e) {
            gBSC = new GameBoardSceneController(playerName);
            gBSC.addAllObservers(observers);
            gBSC.setGameBoard(gameboard,dashboards,players);
            GameBoardSceneController finalGBSC = gBSC;
            gameBoardSceneController = gBSC;
            Platform.runLater(() -> SceneController.changeRootPane(finalGBSC, "gameboard2_scene.fxml"));
            Platform.runLater(()-> {
                try {
                    finalGBSC.updateAll();
                } catch (noTowerException ex) {
                    e.printStackTrace();
                }
            });
        }
        clouds = gameboard.getClouds();
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
    public void askExpert() {
        /*
        Platform.runLater(()-> {
            try {
                getGameSceneController().updateAll();
            } catch (noTowerException ex) {
                ex.printStackTrace();
            }
        });
        Gameboard actualGameBoard = getGameSceneController().getReducedGameBoard();
        ExpertCardsSceneController eCSController = new ExpertCardsSceneController(actualGameBoard,getGameSceneController());
        eCSController.addAllObservers(observers);
        Platform.runLater(()->SceneController.changeRootPane(eCSController,"expert_choice.fxml"));

         */
    }

    @Override
    public void askColor() {

        GetStudentFromCard getStudentFromCard = new GetStudentFromCard(playerName);
        getStudentFromCard.addAllObservers(observers);
        Gameboard currentGameBoard = gameBoardSceneController.getReducedGameBoard();
        List<Dashboard> listOfDashBoards = gameBoardSceneController.getReducedDashboards();
        List<Player> listOfPlayers = gameBoardSceneController.getListOfPlayer();
        getStudentFromCard.setGameBoard(currentGameBoard,listOfDashBoards,listOfPlayers);
        if(currentGameBoard.getToReset().contains(ExpertDeck.MUSICIAN)) {
            getStudentFromCard.setExpertDeck(ExpertDeck.MUSICIAN);
            getStudentFromCard.setExpertImage();
            if (atomicExpert == 0) {
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_THE_HALL);
                getStudentFromCard.setQuestion();
                atomicExpert = 1;
            } else {
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ROW);
                getStudentFromCard.setQuestion();
                atomicExpert = 0;
            }
        }else if(currentGameBoard.getToReset().contains(ExpertDeck.JOKER)) {
            getStudentFromCard.setExpertDeck(ExpertDeck.JOKER);
            getStudentFromCard.setExpertImage();
            if (atomicExpert == 0) {
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_THE_HALL);
                getStudentFromCard.setQuestion();
                atomicExpert = 1;
            } else {
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT);
                getStudentFromCard.setQuestion();
                atomicExpert = 0;
            }
        } else if(currentGameBoard.getToReset().contains(ExpertDeck.TAVERNER)) {
            getStudentFromCard.setExpertDeck(ExpertDeck.TAVERNER);
            getStudentFromCard.setExpertImage();
            if (atomicExpert == 0){
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT);
                getStudentFromCard.setQuestion();
                atomicExpert = 1;
            }
        }
        else if(currentGameBoard.getToReset().contains((ExpertDeck.BARBARIAN))) {
            getStudentFromCard.setExpertDeck(ExpertDeck.BARBARIAN);
            getStudentFromCard.setExpertImage();
            if (atomicExpert == 0) {
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT);
                getStudentFromCard.setQuestion();
                atomicExpert = 1;
            } else {
                getStudentFromCard.setPhase(ExpertDeckPhaseType.SELECT_ROW);
                getStudentFromCard.setQuestion();
                atomicExpert = 0;
            }

        }

        Platform.runLater(()->SceneController.changeRootPane(getStudentFromCard,"get_from_card_scene.fxml"));

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
            //Platform.runLater(() -> SceneController.changeRootPane(finalGBSC, "gameboard2_scene.fxml"));
        }
        return gBSC;
    }

    public static void setCloudPhase(){
        if(cloudChoice.equals("firstPick")){
            cloudChoice = "secondPick";
        }else if(cloudChoice.equals("secondPick")){
            cloudChoice = "get";
        }else{
            cloudChoice = "get";
        }
    }
/*
    public static void setAtomicExpert(){
        if(atomicExpert==0){
            atomicExpert =1;
        }else if(atomicExpert ==1){
            atomicExpert = 0;
        }
    }

 */
}
