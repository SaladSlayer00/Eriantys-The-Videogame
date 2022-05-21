package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;

public class Gui extends ViewObservable implements View {

    private static final String ERROR_STR = "ERRRO";
    private static final String MENU_STR_FXML = "menu_scene.fxml";

    @Override
    public void askNickname(){
        Platform.runLater(() -> SceneController.changeRootPlane(observers, "login_scene.fxml"));
    }

    @Override
    public void askPlayerNumber(){
        //TODO
    }
}
