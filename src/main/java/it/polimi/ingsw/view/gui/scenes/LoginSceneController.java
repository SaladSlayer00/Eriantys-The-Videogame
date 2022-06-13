package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;



public class LoginSceneController extends ViewObservable implements BasicSceneController {

    @FXML
    private TextField nickname;

    @FXML
    private Button joinTheMatchButton;

    @FXML
    private Button backToMainButton;

    @FXML
    public void initialize(){
        joinTheMatchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinTheMatchButtonClicked);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClicked);
    }

    //handles the clicks on the button to join the match
    private void onJoinTheMatchButtonClicked(Event mouseEvent){

        joinTheMatchButton.setDisable(true);
        String nicknameChosen = nickname.getText();
        new Thread(() -> notifyObserver(observers -> observers.onUpdateNickname(nicknameChosen))).start();
    }


    private void onBackToMainButtonClicked(Event mouseEvent){

        joinTheMatchButton.setDisable(true);
        backToMainButton.setDisable(true);

        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, mouseEvent, "menu_scene.fxml");
    }
}
