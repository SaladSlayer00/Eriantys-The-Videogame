package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

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
        radioButtonROne.sendText(easyMode + " Mode");
        radioButtonRTwo.sendText
    }
}
