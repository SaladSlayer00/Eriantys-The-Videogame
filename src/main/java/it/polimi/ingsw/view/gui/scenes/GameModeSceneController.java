package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/* here the first player logged chooses the gamemode
*
 */
public class GameModeSceneController extends ViewObservable implements BasicSceneController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button backToMainButton;

    @FXML
    private RadioButton radioButtonROne;

    @FXML
    private RadioButton radioButtonRTwo;

    @FXML
    private ToggleGroup tG;

    private String easyMode;
    private String expertMode;

    //basic constructor
    public void GameModeSceneController(){
        easyMode = null;
        expertMode = null;
    }

    @FXML
    public void initialize(){
        radioButtonROne.setText(easyMode + " Mode");
        radioButtonRTwo.setText(expertMode + "Mode");

        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClick);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClick);
    }

    //this is to handle the clicks on the confirm button
    //the parameter is actually the event of the button clicked on by the mouse
    private void onConfirmButtonClick(Event event){
        confirmButton.setDisable(true);
        RadioButton selectedButton = (RadioButton) tG.getSelectedToggle();

    }
}
