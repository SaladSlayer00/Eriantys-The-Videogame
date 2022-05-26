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
import it.polimi.ingsw.view.gui.scenes.GameModeSceneController;
import it.polimi.ingsw.view.gui.scenes.PlayersNumberSceneController;
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

    }

    @Override
    public void askAssistant(String nickname, List<Assistant> availableAssistants) {

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
        playersNumSC.setRangeForPlayers(2, 4); //here is kept the possibility to play in 4 players
        Platform.runLater(() -> SceneController.changeRootPane(playersNumSC, "players_number_scene.fxml"));
    }

    //is this thing right???????
    //TODO (but it's actually a TO CHECK)
    @Override
    public void askGameMode(String nickname, List<modeEnum> gameMode){
        GameModeSceneController gameModeSC = new GameModeSceneController();
        gameModeSC.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeRootPane(gameModeSC, "game_mode_scene.fxml"));
    }

    @Override
    public void askInitType(String nickname, List<Type> teams) {

    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void showLoginResult(boolean nicknameAccepter, boolean connectionResult, String nickname) {

    }

    @Override
    public void errorCommunicationAndExit(String nickname) {

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

    @Override
    public void showWinMessage(String winner) {

    }

    @Override
    public void showDrawMessage() {

    }

    @Override
    public void showTable(Gameboard gameboard, List<Dashboard> dashboards) {

    }

    @Override
    public void showAssistant(int index) {

    }
}
