package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.*;
import javafx.application.Platform;

import java.util.List;

public class Gui extends ViewObservable implements View {

    private static final String ERROR_STR = "ERRROR";
    private static final String MENU_STR_FXML = "menu_scene.fxml";

    @Override
    public void askNickname(){
        Platform.runLater(() -> SceneController.changeRootPane(observers, "login_scene.fxml"));
    }

    @Override
    public void askStart(String nickname, String answer) {

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
        AssistantChoiceSceneController aCSController = new AssistantChoiceSceneController();
        aCSController.addAllObservers(observers);
        aCSController.setAssistantDeck(availableAssistants);
        Platform.runLater(() -> SceneController.changeRootPane(aCSController, "assistant_choice_scene.fxml"));
    }

    @Override
    public void askMoves(List<Student> students, List<Island> islands) {

    }

    @Override
    public void askIslandMoves(Color student, List<Island> islands) {

    }

    @Override
    public void askMotherMoves(String nickname, int possibleSteps) {

    }

    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds) {

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
    public void updateTable(Gameboard gameboard, List<Dashboard> dashboards){

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

}
