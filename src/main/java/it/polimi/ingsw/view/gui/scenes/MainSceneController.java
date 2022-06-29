package it.polimi.ingsw.view.gui.scenes;

/**
 * MainSceneController class handles the display of the scene that shows the player the main menu at the beginning of the game
 */

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainSceneController extends ViewObservable implements BasicSceneController{

    @FXML
    private AnchorPane rootAPane;

    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;


    @FXML
    public void initialize(){
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayButtonClicked);
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onQuitButtonClicked);
    }

    private void onPlayButtonClicked(Event event){
        SceneController.changeRootPane(observers, event, "connect_scene.fxml");
    }
    private void onQuitButtonClicked(Event event){
        System.exit(0);
    }

}

