package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class JoinTheMatchSceneController extends ViewObservable implements BasicSceneController {

    @FXML
    private TextField nickField;

    @FXML
    private Button joinTheMatchButton;

    @FXML
    private Button backToMainButton;

    @FXML
    public void initialize(){
        joinTheMatchButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onJoinTheMatchButtonClicked);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClicked);
    }

    //handles the click on the win button
    private void onJoinTheMatchButtonClicked(Event mouseEvent){
        joinTheMatchButton.setDisable(true);
        String nick = nickField.getText();

        new Thread(() -> notifyObserver(observer -> observer.onUpdateNickname(nick))).start();
    }

    //handles click on the back to mai button
    private void onBackToMainButtonClicked(Event mouseEvent){
        backToMainButton.setDisable(true);
        joinTheMatchButton.setDisable(true);

        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, mouseEvent, "menu_scene.fxml");
    }

}
