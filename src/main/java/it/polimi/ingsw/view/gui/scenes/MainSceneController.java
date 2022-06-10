package it.polimi.ingsw.view.gui.scenes;

//this is the main menu of the game for starting a match or just quit (quite stupid but still...)

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainSceneController extends ViewObservable implements BasicSceneController{

    @FXML
    private AnchorPane rootAPane;

    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;

    @FXML
    public void initialization(){
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayButtonClicked);
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onQuitButtonClicked);
    }

    @FXML
    private void onPlayButtonClicked(Event event){
        SceneController.changeRootPane(observers, event, "connect_scene.fxml");
    }
    @FXML
    private void onQuitButtonClicked(Event event){
        System.exit(0);
    }


}

