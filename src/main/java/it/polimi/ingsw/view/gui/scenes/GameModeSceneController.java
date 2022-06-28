package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
* GameModeSceneController class for the choice of the game mode (the first player logged has to make this choice)
 * @authors Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */
public class GameModeSceneController extends ViewObservable implements BasicSceneController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button backToMainButton;

    @FXML
    private RadioButton radioButtonEasyMode;

    @FXML
    private RadioButton radioButtonExpertMode;

    @FXML
    private ToggleGroup tG;

    /**
     * this method initializes the class setting all the various parameter to display the scene
     * on the player's screen in the proper way
     */
    @FXML
    public void initialize(){
        radioButtonEasyMode.setText("Easy Mode");
        radioButtonExpertMode.setText("Expert Mode");

        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClick);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClick);
    }


    /**
     * this method is to handle the event of clicking on the button that confirm the choice
     * of the mode with the mouse
     * @param event is the input given by the player's mouse
     */
    private void onConfirmButtonClick(Event event){
        confirmButton.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) tG.getSelectedToggle();
        //tbh i don't really know what to put here...
        //also not quite sure this is the right way to use the modeEnum...seems quite redundant (???)
        if(selectedRadioButton.equals(radioButtonEasyMode)){
           modeEnum selectedMode = modeEnum.EASY;
            new Thread(() -> notifyObserver(observer -> observer.OnUpdateGameMode(selectedMode))).start();
        }
        else if(selectedRadioButton.equals(radioButtonExpertMode)){
            modeEnum selectedMode = modeEnum.EXPERT;
            new Thread(() -> notifyObserver(observer -> observer.OnUpdateGameMode(selectedMode))).start();
        }
    }

    /**
     * this method is to handle the event of clicking on the button that makes the player get back to
     * the main menu with the mouse
     * @param event is the input given by the player's mouse
     */
    private void onBackToMainButtonClick(Event event){
        backToMainButton.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

}
