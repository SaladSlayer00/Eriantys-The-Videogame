package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.GameModeSceneController;
import it.polimi.ingsw.view.gui.scenes.PlayersNumberSceneController;
import javafx.application.Platform;

import java.util.List;

public class Gui extends ViewObservable implements View {

    private static final String ERROR_STR = "ERRRO";
    private static final String MENU_STR_FXML = "menu_scene.fxml";

    @Override
    public void askNickname(){
        Platform.runLater(() -> SceneController.changeRootPlane(observers, "login_scene.fxml"));
    }

    @Override
    public void askPlayersNumber(){
        PlayersNumberSceneController playersNumSC = new PlayersNumberSceneController();
        playersNumSC.addAllObservers(observers);
        playersNumSC.setRangeForPlayers(2, 4); //here is kept the possibility to play in 4 players
        Platform.runLater(() -> SceneController.changeRootPlane(playersNumSC, "players_number_scene.fxml"));
    }

    @Override
    public void askGameMode(String nickname, List<modeEnum> gameMode){
        GameModeSceneController gameModeSC = new GameModeSceneController();
        gameModeSC.addAllObservers(observers);

    }
}
