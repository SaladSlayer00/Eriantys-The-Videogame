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

/* here the first player logged chooses the gamemode
*
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

    @FXML
    public void initialization(){
        radioButtonEasyMode.setText("Easy Mode");
        radioButtonExpertMode.setText("Expert Mode");

        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClick);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClick);
    }

    //this is to handle the event of clicking on the confirm button with the mouse
    //parameter is the mouse click itself
    private void onConfirmButtonClick(Event event){
        confirmButton.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) tG.getSelectedToggle();
        //tbh i don't really know what to put here...
        //also not quite sure this is the right way to use the modeEnum...seems quite redundant (???)
        if(selectedRadioButton.equals(radioButtonEasyMode)){
           modeEnum selectedMode = modeEnum.valueOf("easy");
            new Thread(() -> notifyObserver(observer -> observer.OnUpdateGameMode(selectedMode))).start();
        }
        else if(selectedRadioButton.equals(radioButtonExpertMode)){
            modeEnum selectedMode = modeEnum.valueOf("expert");
            new Thread(() -> notifyObserver(observer -> observer.OnUpdateGameMode(selectedMode))).start();
        }
    }

    //this is to handle the event of clicking on the back to menu button with the mouse
    //parameter is the mouse click itself
    private void onBackToMainButtonClick(Event event){
        backToMainButton.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

}
