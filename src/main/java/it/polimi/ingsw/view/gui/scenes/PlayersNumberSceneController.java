package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;


/* here the first player logged chooses the number of players that are going to play the match
*
 */
public class PlayersNumberSceneController extends ViewObservable implements BasicSceneController {

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

    private int minimumPlayers;
    private int maximumPlayers;

    //basic constructor
    public void PlayerNumberSceneController(){
        minimumPlayers = 0;
        maximumPlayers = 0;
    }

    @FXML
    public void initialize(){
        radioButtonROne.setText(minimumPlayers + " players");
        radioButtonRTwo.setText(maximumPlayers + " players");

        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClick);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClick);
    }

    //this is to handle the clicks on the confirm button
    //the parameter of the method is the event of the button clicked on by the mouse
    @FXML
    private void onConfirmButtonClick(Event event){
        confirmButton.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) tG.getSelectedToggle();
        int playersNum = Character.getNumericValue(selectedRadioButton.getText().charAt(0));

        new Thread(() -> notifyObserver(observer -> observer.onUpdatePlayersNumber(playersNum))).start();
    }

    //this is to handle the clicks on the back to menu button
    //the parameter of the method is the event of the button clicked on by the mouse
    @FXML
    private void onBackToMainButtonClick(Event event){
        backToMainButton.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    //initialization of the radiobuttons
    //minimumPlayers is the minimum number of players
    //maximumPlayers is the maximum number of players
    public void setRangeForPlayers(int minimumPlayers, int maximumPlayers){
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
    }

}
