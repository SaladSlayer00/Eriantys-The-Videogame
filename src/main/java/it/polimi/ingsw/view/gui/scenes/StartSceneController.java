package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * StartSceneController class handles the starting scene of the game
 */
public class StartSceneController extends ViewObservable implements BasicSceneController {
    @FXML
    private Button startButton;

    /**
     * this method initializes the class setting all the
     * various parameter to display the scene on the player's screen in the proper way
     */
    @FXML
    public void initialize(){
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStartButtonClicked);
    }

    /**
     * this method handles the clicks on the button that gives the start to the game
     * @param event is the input given by the player's mouse
     */
    private void onStartButtonClicked(Event event){
        startButton.setDisable(true);
        new Thread(() -> notifyObserver(obs -> obs.OnStartAnswer("yes"))).start();
    }

}
