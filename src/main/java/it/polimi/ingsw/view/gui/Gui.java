package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enums.modeEnum;
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
}
